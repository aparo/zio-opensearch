/*
 * Copyright 2018-2022 - Alberto Paro on Apache 2 Licence. All Rights Reserved.
 */

package zio.opensearch.client

import java.security.SecureRandom
import java.security.cert.X509Certificate
import _root_.zio.opensearch.{ OpenSearchService, IngestService, _ }
import cats.effect._
import zio.opensearch.OpenSearch
import zio.opensearch.client.RequestToCurl.toCurl
import zio.opensearch.orm.ORMService

import javax.net.ssl.{ SSLContext, X509TrustManager }
import org.http4s.client.Client
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.headers.{ Authorization, `Content-Type` }
import org.http4s.{ Request, _ }
import zio._

import zio.console.Console
import zio.exception._
import zio.interop.catz._

import zio.logging.{ LogLevel, Logging }
import zio.schema.SchemaService
import zio.opensearch.schema.OpenSearchSchemaManagerService
import org.typelevel.ci._
import zio.Duration

import scala.concurrent.ExecutionContext

private[client] case class ZioHTTP4SClient(
  blockingEC: ExecutionContext,
  openSearchConfig: OpenSearchService
)(implicit val runtime: Runtime[Any])
    extends opensearch.HTTPService {
  private lazy val _http4sClient: Resource[Task, Client[Task]] = {
    val sslContext: SSLContext =
      if (openSearchConfig.useSSL) {
        if (openSearchConfig.validateSSLCertificates) {
          SSLContext.getDefault
        } else {
          // we disable certificate check
          // You need to create a SSLContext with your own TrustManager and create HTTPS scheme using this context. Here is the code,

          val sslContext = SSLContext.getInstance("SSL");

          // set up a TrustManager that trusts everything
          sslContext.init(
            null,
            Array {
              new X509TrustManager() {
                override def checkClientTrusted(chain: Array[X509Certificate], authType: String): Unit = {}

                override def checkServerTrusted(chain: Array[X509Certificate], authType: String): Unit = {}

                override def getAcceptedIssuers: Array[X509Certificate] = null
              }
            },
            new SecureRandom()
          )
          sslContext
          //.setAcceptAnyCertificate(true)
          //          cfg.build()
        }
      } else SSLContext.getDefault

    BlazeClientBuilder[Task](blockingEC)
      .withSslContext(sslContext)
      .withIdleTimeout(openSearchConfig.timeout.asScala)
      .withMaxTotalConnections(openSearchConfig.concurrentConnections)
      .resource
  }

  private def resolveMethod(method: String): Method =
    method.toUpperCase() match {
      case "GET"    => Method.GET
      case "POST"   => Method.POST
      case "PUT"    => Method.PUT
      case "DELETE" => Method.DELETE
      case "HEAD"   => Method.HEAD
      case "PATCH"  => Method.PATCH
      //            case "CONNECT" => request.connect(uri)
      case "OPTIONS" => Method.OPTIONS
      //            case "TRACE"   => request.trace(uri)
    }

  private val jsonContent =
    `Content-Type`(MediaType.application.json, Charset.`UTF-8`)
  private val ndjsonContent =
    `Content-Type`(new MediaType("application", "x-ndjson"), Charset.`UTF-8`)

  override def doCall(
    method: String,
    url: String,
    body: Option[String],
    queryArgs: Map[String, String],
    headers: Map[String, String]
  ): ZIO[Any, FrameworkException, ESResponse] = {
    val path: String = if (url.startsWith("/")) url else "/" + url
    val newPath = openSearchConfig.getHost + path.replaceAll("//", "/")
    var uri = Uri.unsafeFromString(s"$newPath")

    queryArgs.foreach(v => uri = uri.withQueryParam(v._1, v._2))
    val headersObjects: List[Header.Raw] =
      (Header.Raw(CIString("Accept"), "application/json") :: headers.map {
        case (key, value) =>
          Header.Raw(CIString(key), value)
      }.toList) ++ (if (openSearchConfig.user.isDefined && openSearchConfig.user.get.nonEmpty) {
                      List(
                        Header.Raw(
                          ci"Authorization",
                          BasicCredentials(
                            openSearchConfig.user.get,
                            openSearchConfig.password.getOrElse("")
                          ).token
                        )
                      )
                    } else Nil)

    var request = Request[Task](
      resolveMethod(method),
      uri,
      headers = Headers(headersObjects),
      body = body.map(a => fs2.Stream(a).through(fs2.text.utf8Encode)).getOrElse(EmptyBody)
    )

    // we manage headers
    if (url.contains("_bulk")) {
      request = request.withContentType(ndjsonContent)
    } else {
      request = request.withContentType(jsonContent)

      val specials = url.split("/").filter(_.startsWith("_"))
      if (specials.nonEmpty) {
        specials.last match {
          case "_search" | "_scan" | "_scroll" =>
          //            OpenSearchKamon.search.withoutTags().increment()
          case "_update" =>
          //            OpenSearchKamon.update.withoutTags().increment()
          case "_settings" | "_mappings" | "_status" | "_state" | "_node" | "_nodes" =>
          //            OpenSearchKamon.admin.withoutTags().increment()
          case _ if method == "delete" =>
          //            OpenSearchKamon.delete.withoutTags().increment()
          case _ =>
        }
      }
    }

    ZIO.logDebug(s"${toCurl(request)}") *>
      _http4sClient
        .use(_.run(request).use { response =>
          for {
            body <- response.bodyText.compile.toList
            _ <- ZIO.logDebug(RequestToCurl.responseToString(response, body.mkString("")))
          } yield ESResponse(status = response.status.code, body = body.mkString(""))

        })
        .mapError(e => FrameworkException(e))
  }

}

object ZioHTTP4SClient {
  val live: ZLayer[OpenSearchService, Nothing, HTTPService] =
    ZLayer.fromServices[Logger[String], Blocking.Service, OpenSearchService, HTTPService] {
      (loggingService, blockingService, openSearchConfig) =>
        ZioHTTP4SClient(loggingService, blockingService.blockingExecutor.asEC, openSearchConfig)(Runtime.default)
    }

  val fromOpenSearch: ZLayer[OpenSearch, Nothing, HTTPService] =
    ZLayer.fromServicesM[Logger[String], Blocking.Service, OpenSearch, Any, Nothing, HTTPService] {
      (loggingService, blockingService, openSearch) =>
        for {
          osConfig <- openSearch.osConfig
        } yield ZioHTTP4SClient(loggingService, blockingService.blockingExecutor.asEC, osConfig)(Runtime.default)
    }

  type OpenSearchEnvironment = OpenSearchService
    with IndicesService
    with ORMService
    with ClusterService
    with IngestService
    with NodesService
    with SnapshotService
    with TasksService
//    with SchemaService
    with OpenSearchSchemaManagerService
    with ORMService

  val fullLayer: ZLayer[Console with clock.Clock with OpenSearchService, Nothing, OpenSearchEnvironment] = {
    ZioHTTP4SClient.live >+> OpenSearchService.live >+> IndicesService.live >+>
      ClusterService.live >+> IngestService.live >+> NodesService.live >+> SnapshotService.live >+>
      TasksService.live >+> OpenSearchSchemaManagerService.liveInMemory >+> ORMService.live
  }

  def fullFromConfig(
    openSearchConfig: OpenSearchService,
    loggingService: ZLayer[Console with clock.Clock, Nothing, Logging]
  ): ZLayer[Console with clock.Clock, Nothing, OpenSearchEnvironment] = {
    val configService: Layer[Nothing, OpenSearchService] =
      ZLayer.succeed[OpenSearchService](openSearchConfig)
    val blockingService: Layer[Nothing, Blocking] = Blocking.live
    val httpService: ZLayer[Console with clock.Clock, Nothing, HTTPService] =
      (loggingService ++ blockingService ++ configService) >>> ZioHTTP4SClient.live
    val baseOpenSearchService: ZLayer[Console with clock.Clock, Nothing, Has[
      OpenSearchService
    ]] = (loggingService ++ httpService ++ configService) >>> OpenSearchService.live
    val indicesService: ZLayer[Console with clock.Clock, Nothing, IndicesService] =
      baseOpenSearchService >>> IndicesService.live
    val clusterService = indicesService >>> ClusterService.live
    val ingestService = baseOpenSearchService >>> IngestService.live
    val nodesService = baseOpenSearchService >>> NodesService.live
    val snapshotService = baseOpenSearchService >>> SnapshotService.live
    val tasksService = baseOpenSearchService >>> TasksService.live
    // with in memory SchemaService
    //OpenSearchSchemaManagerService
    val schemaService = loggingService >>> SchemaService.inMemory
    val openSearchSchemaManagerService = (indicesService ++ schemaService) >>> OpenSearchSchemaManagerService.live

    val ormService = (schemaService ++ clusterService) >>> ORMService.live

    baseOpenSearchService ++ indicesService ++ clusterService ++ ingestService ++ nodesService ++ snapshotService ++
      tasksService ++ schemaService ++ openSearchSchemaManagerService ++ ormService
  }

  def buildFromOpenSearch(
    logLayer: ZLayer[Console with clock.Clock, Nothing, Logging],
    osEmbedded: ZLayer[Any, Throwable, OpenSearch]
  ): ZLayer[Console with clock.Clock, Throwable, OpenSearchEnvironment] = {
    val blockingService: Layer[Nothing, Blocking] = Blocking.live
    val httpLayer = (logLayer ++ osEmbedded ++ blockingService) >>> ZioHTTP4SClient.fromOpenSearch
    val baseOpenSearchService = (logLayer ++ httpLayer ++ osEmbedded) >>> OpenSearchService.fromOpenSearch
    val indicesService = baseOpenSearchService >>> IndicesService.live
    val clusterService = indicesService >>> ClusterService.live
    val ingestService = baseOpenSearchService >>> IngestService.live
    val nodesService = baseOpenSearchService >>> NodesService.live
    val snapshotService = baseOpenSearchService >>> SnapshotService.live
    val tasksService = baseOpenSearchService >>> TasksService.live
    // with in memory SchemaService
    //OpenSearchSchemaManagerService
    val schemaService = logLayer >>> SchemaService.inMemory
    val openSearchSchemaManagerService = (indicesService ++ schemaService) >>> OpenSearchSchemaManagerService.live
    val ormService = (schemaService ++ clusterService) >>> ORMService.live

    baseOpenSearchService ++ indicesService ++ clusterService ++ ingestService ++ nodesService ++ snapshotService ++
      tasksService ++ schemaService ++ openSearchSchemaManagerService ++ ormService
  }

}
