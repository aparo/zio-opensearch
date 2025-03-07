/*
 * Copyright 2023-2025 Alberto Paro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package zio.opensearch.client

import _root_.zio.opensearch._
import sttp.client3._
import sttp.client3.httpclient.zio.{ HttpClientZioBackend, SttpClient }
import sttp.client3.prometheus.PrometheusBackend
import zio._
import zio.opensearch.OpenSearch
import zio.opensearch.common.Method
import zio.exception._

import java.net.{ CookieManager, CookiePolicy }
import java.net.http.HttpClient
import java.net.http.HttpClient.{ Redirect, Version }
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.{ SSLContext, SSLParameters, TrustManager, X509TrustManager }
case class ZioSttpClient(
  openSearchConfig: OpenSearchConfig,
  sttpClient: SttpClient
) extends OpenSearchHttpService {

  override def doCall(
    method: Method,
    url: String,
    body: Option[String],
    queryArgs: Map[String, String],
    headers: Map[String, String]
  ): ZIO[Any, FrameworkException, ESResponse] = {
    val path: String = if (url.startsWith("/")) url else "/" + url
    val newPath = openSearchConfig.getHost + path.replaceAll("//", "/")

    val uri = uri"$newPath?$queryArgs"

    var request = method match {
      case Method.GET                    => basicRequest.get(uri)
      case Method.POST                   => basicRequest.post(uri)
      case Method.PUT                    => basicRequest.put(uri)
      case Method.DELETE                 => basicRequest.delete(uri)
      case Method.HEAD                   => basicRequest.head(uri)
      case Method.PATCH                  => basicRequest.patch(uri)
      case Method.OPTIONS                => basicRequest.options(uri)
      case Method.CONNECT | Method.TRACE => basicRequest.post(uri)
      case Method.CUSTOM(_)              => basicRequest.post(uri)
    }

    if (headers.nonEmpty)
      request = request.headers(headers)

    // we manage headers
    if (url.contains("_bulk")) {
      request = request.contentType("application/x-ndjson", "UTF-8")
      //      headers ::= RawHeader("Content-Type", "application/x-ndjson")

      //      OpenSearchKamon.bulk.withoutTags().increment()
    } else {
      request = request.contentType("application/json", "UTF-8")

      val specials = url.split("/").filter(_.startsWith("_"))
      if (specials.nonEmpty) {
        specials.last match {
          case "_search" | "_scan" | "_scroll" =>
          //            OpenSearchKamon.search.withoutTags().increment()
          case "_update" =>
          //            OpenSearchKamon.update.withoutTags().increment()
          case "_settings" | "_mappings" | "_status" | "_state" | "_node" | "_nodes" =>
          //            OpenSearchKamon.admin.withoutTags().increment()
          case _ if method == Method.DELETE =>
          //            OpenSearchKamon.delete.withoutTags().increment()
          case _ =>
        }
      }
    }

    if (openSearchConfig.user.isDefined && openSearchConfig.user.get.nonEmpty) {
      request = request.auth.basic(openSearchConfig.user.get, openSearchConfig.password.getOrElse(""))
    }

    if (body.nonEmpty && method != Method.HEAD)
      request = request.body(body.getOrElse(""))

    val result = for {
      _ <- ZIO.logDebug(s"${request.toCurl}")
      response <- sttpClient.send(request).mapError(e => FrameworkException(e))
    } yield ESResponse(
      status = response.code.code,
      body = response.body match {
        case Left(value)  => value
        case Right(value) => value
      }
    )
    result.mapError(e => FrameworkException(e))
  }

  def close(): ZIO[Any, Nothing, Unit] =
    sttpClient.close().mapError(e => FrameworkException(e)).ignore

}

object ZioSttpClient {

  def buildClientFromConfig(openSearchConfig: OpenSearchConfig): HttpClient = {
    val cfg = HttpClient.newBuilder()
    // we setup SSL
    if (openSearchConfig.useSSL) {
      if (!openSearchConfig.validateSSLCertificates) {
        //        cfg.sslParameters(true)
        //      } else {
        // we disable certificate check
        val trustAllCerts = Array[TrustManager](new X509TrustManager() {
          def getAcceptedIssuers: Array[X509Certificate] = new Array[X509Certificate](0)

          def checkClientTrusted(certs: Array[X509Certificate], authType: String): Unit = {}

          def checkServerTrusted(certs: Array[X509Certificate], authType: String): Unit = {}
        })

        // context.init(null, trustAllCerts, new java.security.SecureRandom());
        val sc = SSLContext.getInstance("SSL")
        sc.init(null, trustAllCerts, new SecureRandom)

        val parameters = new SSLParameters
        parameters.setEndpointIdentificationAlgorithm("HTTPS")

        cfg.sslContext(sc).sslContext(sc).sslParameters(parameters)
      }
    }

    // we setup auth

    // we setup cookies
    val manager = new CookieManager(null, CookiePolicy.ACCEPT_ALL)
    cfg
      .version(Version.HTTP_2)
      .connectTimeout(openSearchConfig.timeout)
      .followRedirects(Redirect.ALWAYS)
      .cookieHandler(manager)

    cfg.build()
  }

  val liveBackend: ZLayer[OpenSearchConfig, Throwable, SttpClient] =
    ZLayer.scoped(
      ZIO.acquireRelease(
        for {
          openSearchConfig <- ZIO.service[OpenSearchConfig]
          client <- ZIO.attempt(buildClientFromConfig(openSearchConfig))
          bck <- ZIO.attempt(
            HttpClientZioBackend.usingClient(
              client
            )
          )
          withProm = if (openSearchConfig.prometheus) {
            PrometheusBackend(bck)
          } else bck
        } yield withProm
      )(_.close().ignore)
    )

  val live: ZLayer[SttpClient with OpenSearchConfig, Nothing, OpenSearchHttpService] =
    ZLayer {
      for {
        sttpClient <- ZIO.service[SttpClient]
        openSearchConfig <- ZIO.service[OpenSearchConfig]
      } yield ZioSttpClient(sttpClient = sttpClient, openSearchConfig = openSearchConfig)
    }

  val configFromOpenSearch: ZLayer[OpenSearch, Nothing, OpenSearchConfig] = {
    ZLayer {
      for {
        openSearch <- ZIO.service[OpenSearch]
        osConfig <- openSearch.osConfig
      } yield osConfig
    }
  }

  type OpenSearchEnvironment = OpenSearchService with OpenSearchHttpService
  val fullLayer: ZLayer[OpenSearchConfig, Throwable, OpenSearchEnvironment] = {
    ZioSttpClient.liveBackend >+> ZioSttpClient.live >+> OpenSearchService.live
  }

  def fullFromConfig(
    openSearchConfig: OpenSearchConfig
  ): ZLayer[Any, Throwable, OpenSearchEnvironment] = ZLayer.make[OpenSearchEnvironment](
    ZLayer.succeed[OpenSearchConfig](openSearchConfig),
    ZioSttpClient.liveBackend,
    ZioSttpClient.live,
    OpenSearchService.live
  )

  def buildFromOpenSearch(
    osEmbedded: ZLayer[Any, Throwable, OpenSearch]
  ): ZLayer[Any, Throwable, OpenSearchEnvironment] =
    ZLayer.make[OpenSearchEnvironment](
      osEmbedded,
      ZioSttpClient.configFromOpenSearch,
      ZioSttpClient.liveBackend,
      ZioSttpClient.live,
      OpenSearchService.live
    )
}
