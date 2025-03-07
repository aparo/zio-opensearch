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

package zio.opensearch.orm

import zio._
import zio.auth.AuthContext
import zio.opensearch.aggregations.Aggregation
import zio.opensearch.{ ESCursor, ESCursorTyped, OpenSearchService }
import zio.opensearch.cluster.ClusterManager
import zio.opensearch.common.{ ResultDocument, SourceConfig, WaitForStatus }
import zio.opensearch.common.index.IndexRequest
import zio.opensearch.common.search.SearchResponse
import zio.opensearch.common.search.Highlight
import zio.opensearch.indices.IndicesManager
import zio.opensearch.indices.create.CreateResponse
import zio.opensearch.indices.delete.DeleteResponse
import zio.opensearch.indices.refresh.RefreshResponse
import zio.opensearch.indices.requests.CreateRequestBody
import zio.opensearch.mappings.RootDocumentMapping
import zio.opensearch.queries.Query
import zio.opensearch.sort.Sort.{ EmptySort, Sort }
import zio.opensearch.suggestion.Suggestion
import zio.exception.FrameworkException
import zio.json._
import zio.json.ast.Json
import zio.schema.Schema
import zio.schema.opensearch.OpenSearchSchema
import zio.stream.ZStream

object OrmManager {
  lazy val live
    : ZLayer[OpenSearchService with IndicesManager with ClusterManager with MappingManager, Nothing, OrmManager] =
    ZLayer {
      for {
        osService <- ZIO.service[OpenSearchService]
        iManager <- ZIO.service[IndicesManager]
        cManager <- ZIO.service[ClusterManager]
        mManager <- ZIO.service[MappingManager]
      } yield new OrmManager {
        def openSearchService: OpenSearchService = osService

        def clusterManager: ClusterManager = cManager

        def indicesManager: IndicesManager = iManager
        def mappingManager: MappingManager = mManager

      }
    }
}
trait OrmManager {

  def openSearchService: OpenSearchService
  def clusterManager: ClusterManager
  def indicesManager: IndicesManager
  def mappingManager: MappingManager

  def search[T: JsonEncoder: JsonDecoder](
    queryBuilder: TypedQueryBuilder[T]
  ): ZIO[Any, FrameworkException, SearchResponse] =
    openSearchService.search(queryBuilder.toRequest)

  /* Get a typed JSON document from an index based on its id. */
  def searchScan[T: JsonEncoder](
    queryBuilder: TypedQueryBuilder[T]
  )(implicit decoderT: JsonDecoder[T]): ESCursorTyped[T] =
    openSearchService.searchStreamTyped[T](queryBuilder.toRequest)

  def search(
    queryBuilder: QueryBuilder
  ): ZIO[Any, FrameworkException, SearchResponse] =
    openSearchService.search(queryBuilder.toRequest)

  def searchScan(queryBuilder: QueryBuilder): ESCursor =
    openSearchService.searchStream(queryBuilder.setScan().toRequest)

  def dropDatabase(index: String): ZIO[Any, FrameworkException, Unit] =
    for {
      exists <- indicesManager.exists(Chunk(index))
      _ <- (for {
        _ <- indicesManager.delete(Chunk(index))
        _ <- clusterManager.health(waitForStatus = Some(WaitForStatus.yellow))
        _ <- mappingManager.isDirtRef.set(true)
      } yield ()).when(exists)
    } yield ()

  def getIndicesAlias(): ZIO[Any, FrameworkException, Map[String, Chunk[String]]] =
    clusterManager.state().map { response =>
      response.metadata.indices.map { i =>
        i._1 -> i._2.aliases
      }
    }

  def reindex(index: String)(
    implicit
    authContext: AuthContext
  ): ZIO[Any, FrameworkException, Unit] = {
    val qb = QueryBuilder(indices = Chunk(index))(
      authContext.systemNoSQLContext(),
      this
    )
    ZIO
      .attempt(qb.scan.foreach { searchHit =>
        openSearchService.addToBulk(
          IndexRequest(
            searchHit.index,
            id = Some(searchHit.id),
            body = searchHit.source.getOrElse(Json.Obj())
          )
        )
      })
      .mapError(e => FrameworkException(e)) *>
      indicesManager.refresh(index).ignore

  }

  def copyData(
    queryBuilder: QueryBuilder,
    destIndex: String,
    callbackSize: Int = 10000,
    callback: Long => URIO[Any, Unit] = { _ =>
      ZIO.unit
    },
    transformSource: ResultDocument => Option[Json.Obj] = {
      _.source
    }
  ): ZIO[Any, FrameworkException, Long] = {

    def processUpdate(): ZIO[Any, FrameworkException, Long] =
      queryBuilder.scan.zipWithIndex.map {
        case (hit, count) =>
          for {
            body <- ZIO.fromOption(transformSource(hit))
            _ <- openSearchService.addToBulk(
              IndexRequest(
                destIndex,
                id = Some(hit.id),
                body = body
              )
            )
            _ <- callback(count).when(count % callbackSize == 0)
          } yield count
      }.runCount

    for {
      size <- processUpdate()
      _ <- callback(size).when(size > 0).ignore
      _ <- indicesManager.flush(Chunk(destIndex))
    } yield size
  }

  def getIds(index: String)(
    implicit
    authContext: AuthContext
  ): ZStream[Any, FrameworkException, String] =
    QueryBuilder(
      indices = Chunk(index),
      bulkRead = 5000
    )(authContext.systemNoSQLContext(), this).valueList[String]("_id")

  def countAll(indices: Chunk[String], filters: Chunk[Query] = Chunk.empty)(
    implicit
    authContext: AuthContext
  ): ZIO[Any, FrameworkException, Long] = {
    val qb = QueryBuilder(indices = indices, size = 0, filters = filters)(authContext, this)
    qb.count
  }

  def countAll(index: String)(implicit authContext: AuthContext): ZIO[Any, FrameworkException, Long] =
    countAll(indices = Chunk(index))

  def queryBuilder(
    indices: Chunk[String] = Chunk.empty,
    docTypes: Chunk[String] = Chunk.empty,
    queries: Chunk[Query] = Chunk.empty,
    filters: Chunk[Query] = Chunk.empty,
    postFilters: Chunk[Query] = Chunk.empty,
    fields: Chunk[String] = Chunk.empty,
    from: Int = 0,
    size: Int = -1,
    highlight: Highlight = Highlight(),
    explain: Boolean = false,
    bulkRead: Int = -1,
    sort: Sort = EmptySort,
    searchType: Option[String] = None,
    scrollTime: Option[String] = None,
    timeout: Long = 0,
    version: Boolean = true,
    trackScore: Boolean = false,
    searchAfter: Chunk[Json] = Chunk.empty,
    source: SourceConfig = SourceConfig.all,
    suggestions: Map[String, Suggestion] = Map.empty[String, Suggestion],
    aggregations: Aggregation.Aggregations = Aggregation.EmptyAggregations,
    isSingleIndex: Boolean = true,
    extraBody: Option[Json.Obj] = None
  )(implicit authContext: AuthContext): ZIO[Any, FrameworkException, QueryBuilder] =
    ZIO.succeed(
      QueryBuilder(
        indices = indices,
        docTypes = docTypes,
        queries = queries,
        filters = filters,
        postFilters = postFilters,
        fields = fields,
        from = from,
        size = size,
        highlight = highlight,
        explain = explain,
        bulkRead = bulkRead,
        sort = sort,
        searchType = searchType,
        scrollTime = scrollTime,
        timeout = timeout,
        version = version,
        trackScore = trackScore,
        searchAfter = searchAfter,
        source = source,
        suggestions = suggestions,
        aggregations = aggregations,
        isSingleIndex = isSingleIndex,
        extraBody = extraBody
      )(authContext, this)
    )

  def deleteMapping[T](
    implicit openSearchSchema: OpenSearchSchema[T]
  ): ZIO[Any, FrameworkException, DeleteResponse] =
    indicesManager.delete(Chunk(openSearchSchema.indexName))

  def getMapping[T](
    implicit openSearchSchema: OpenSearchSchema[T]
  ): ZIO[Any, FrameworkException, RootDocumentMapping] =
    ZIO.succeed(OpenSearchSchema2Mapping.toRootMapping(openSearchSchema))

  def createIndex[T](
    implicit openSearchSchema: OpenSearchSchema[T]
  ): ZIO[Any, FrameworkException, CreateResponse] =
    indicesManager.create(
      index = openSearchSchema.indexName,
      body = CreateRequestBody(mappings = Some(OpenSearchSchema2Mapping.toRootMapping(openSearchSchema)))
    )

  def refresh[T](
    implicit openSearchSchema: OpenSearchSchema[T]
  ): ZIO[Any, FrameworkException, RefreshResponse] =
    indicesManager.refresh(openSearchSchema.indexName)

  def count[T](
    implicit openSearchSchema: OpenSearchSchema[T]
  ): ZIO[Any, FrameworkException, Long] =
    openSearchService.count(indices = Chunk(openSearchSchema.indexName)).map(_.count)

  def bulkStream[T: JsonEncoder](value: ZStream[Any, Nothing, T], size: Int = 1000)(
    implicit osSchema: OpenSearchSchema[T]
  ): ZIO[Any, FrameworkException, Int] = {
    val indexName = osSchema.indexName
    openSearchService.bulkStream(
      value.mapConcat { v =>
        v.toJsonAST.toOption.map { j =>
          val jo = j.asInstanceOf[Json.Obj]
          val id = osSchema.resolveId(jo, None)
          IndexRequest(index = indexName, body = j, id = id)
        }
      },
      size = size
    )
  }

  def query[T](
    implicit decoder: JsonDecoder[T],
    encoder: JsonEncoder[T],
    authContext: AuthContext,
    osSchema: OpenSearchSchema[T]
  ): ZIO[Any, FrameworkException, TypedQueryBuilder[T]] =
    ZIO.succeed(
      TypedQueryBuilder(indices = Chunk(osSchema.indexName))(
        authContext = authContext,
        encode = encoder,
        decoder = decoder,
        ormManager = this
      )
    )

}
