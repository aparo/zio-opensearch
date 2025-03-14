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

package zio.opensearch

import zio.auth.AbstractUser.SystemUser
import zio.auth.AuthContext
import zio.exception._
import zio.opensearch.aggregations.Aggregation
import zio.opensearch.client._
import zio.opensearch.common.search.Highlight
import zio.opensearch.mappings.RootDocumentMapping
import zio.opensearch.orm._
import zio.opensearch.queries.Query
import zio.opensearch.requests.cluster._
import zio.opensearch.requests.{ DeleteRequest, GetRequest, IndexRequest }
import zio.opensearch.responses._
import zio.opensearch.responses.cluster._
import zio.opensearch.sort.Sort.{ EmptySort, Sort }
import zio.json._
import zio.json.ast._
import zio._
import zio.opensearch.common.{ ExpandWildcards, Level }
import zio.opensearch.suggestion.Suggestion
import zio.stream._

trait ClusterService extends ClusterActionResolver {

  def baseOpenSearchService: OpenSearchService
  def indicesService: IndicesService

  /*
   * Provides explanations for shard allocations in the cluster.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-allocation-explain.html
   *
   * @param body body the body of the call
   * @param includeDiskInfo Return information about disk usage and shard sizes (default: false)
   * @param includeYesDecisions Return 'YES' decisions in explanation (default: false)
   */
  def allocationExplain(
    body: Option[Json.Obj] = None,
    includeDiskInfo: Option[Boolean] = None,
    includeYesDecisions: Option[Boolean] = None
  ): ZIO[Any, FrameworkException, ClusterAllocationExplainResponse] = {
    val request = ClusterAllocationExplainRequest(
      body = body,
      includeDiskInfo = includeDiskInfo,
      includeYesDecisions = includeYesDecisions
    )

    allocationExplain(request)

  }

  def allocationExplain(
    request: ClusterAllocationExplainRequest
  ): ZIO[Any, FrameworkException, ClusterAllocationExplainResponse] =
    execute(request)

  /*
   * Returns cluster settings.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-update-settings.html
   *
   * @param flatSettings Return settings in flat format (default: false)
   * @param includeDefaults Whether to return all default clusters setting.
   * @param masterTimeout Explicit operation timeout for connection to master node
   * @param timeout Explicit operation timeout
   */
  def getSettings(
    flatSettings: Option[Boolean] = None,
    includeDefaults: Boolean = false,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None
  ): ZIO[Any, FrameworkException, ClusterGetSettingsResponse] = {
    val request = ClusterGetSettingsRequest(
      flatSettings = flatSettings,
      includeDefaults = includeDefaults,
      masterTimeout = masterTimeout,
      timeout = timeout
    )

    getSettings(request)

  }

  def getSettings(request: ClusterGetSettingsRequest): ZIO[Any, FrameworkException, ClusterGetSettingsResponse] =
    execute(request)

  /*
   * Returns basic information about the health of the cluster.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-health.html
   *
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param index Limit the information returned to a specific index
   * @param level Specify the level of detail for returned information
   * @param local Return local information, do not retrieve the state from master node (default: false)
   * @param masterTimeout Explicit operation timeout for connection to master node
   * @param timeout Explicit operation timeout
   * @param waitForActiveShards Wait until the specified number of shards is active
   * @param waitForEvents Wait until all currently queued events with the given priority are processed
   * @param waitForNoInitializingShards Whether to wait until there are no initializing shards in the cluster
   * @param waitForNoRelocatingShards Whether to wait until there are no relocating shards in the cluster
   * @param waitForNodes Wait until the specified number of nodes is available
   * @param waitForStatus Wait until cluster is in a specific state
   */
  def health(
    body: Json.Obj = Json.Obj(),
    expandWildcards: Seq[ExpandWildcards] = Nil,
    index: Option[String] = None,
    level: Level = Level.cluster,
    local: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None,
    waitForActiveShards: Option[String] = None,
    waitForEvents: Seq[WaitForEvents] = Nil,
    waitForNoInitializingShards: Option[Boolean] = None,
    waitForNoRelocatingShards: Option[Boolean] = None,
    waitForNodes: Option[String] = None,
    waitForStatus: Option[WaitForStatus] = None
  ): ZIO[Any, FrameworkException, ClusterHealthResponse] = {
    val request = ClusterHealthRequest(
      body = body,
      index = index,
      expandWildcards = expandWildcards,
      level = level,
      local = local,
      masterTimeout = masterTimeout,
      timeout = timeout,
      waitForActiveShards = waitForActiveShards,
      waitForEvents = waitForEvents,
      waitForNoInitializingShards = waitForNoInitializingShards,
      waitForNoRelocatingShards = waitForNoRelocatingShards,
      waitForNodes = waitForNodes,
      waitForStatus = waitForStatus
    )

    health(request)

  }

  def health(request: ClusterHealthRequest): ZIO[Any, FrameworkException, ClusterHealthResponse] =
    execute(request)

  /*
   * Returns a list of any cluster-level changes (e.g. create index, update mapping,
allocate or fail shard) which have not yet been executed.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-pending.html
   *
   * @param local Return local information, do not retrieve the state from master node (default: false)
   * @param masterTimeout Specify timeout for connection to master
   */
  def pendingTasks(
    local: Option[Boolean] = None,
    masterTimeout: Option[String] = None
  ): ZIO[Any, FrameworkException, ClusterPendingTasksResponse] = {
    val request =
      ClusterPendingTasksRequest(local = local, masterTimeout = masterTimeout)

    pendingTasks(request)

  }

  def pendingTasks(request: ClusterPendingTasksRequest): ZIO[Any, FrameworkException, ClusterPendingTasksResponse] =
    execute(request)

  /*
   * Updates the cluster settings.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-update-settings.html
   *
   * @param body body the body of the call
   * @param flatSettings Return settings in flat format (default: false)
   * @param masterTimeout Explicit operation timeout for connection to master node
   * @param timeout Explicit operation timeout
   */
  def putSettings(
    body: Json.Obj,
    flatSettings: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None
  ): ZIO[Any, FrameworkException, ClusterPutSettingsResponse] = {
    val request = ClusterPutSettingsRequest(
      body = body,
      flatSettings = flatSettings,
      masterTimeout = masterTimeout,
      timeout = timeout
    )

    putSettings(request)

  }

  def putSettings(request: ClusterPutSettingsRequest): ZIO[Any, FrameworkException, ClusterPutSettingsResponse] =
    execute(request)

  /*
   * Returns the information about configured remote clusters.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-remote-info.html
   *
   */
  def remoteInfo(
    ): ZIO[Any, FrameworkException, ClusterRemoteInfoResponse] = {
    val request = ClusterRemoteInfoRequest()

    remoteInfo(request)

  }

  def remoteInfo(request: ClusterRemoteInfoRequest): ZIO[Any, FrameworkException, ClusterRemoteInfoResponse] = execute(
    request
  )

  /*
   * Allows to manually change the allocation of individual shards in the cluster.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-reroute.html
   *
   * @param body body the body of the call
   * @param dryRun Simulate the operation only and return the resulting state
   * @param explain Return an explanation of why the commands can or cannot be executed
   * @param masterTimeout Explicit operation timeout for connection to master node
   * @param metric Limit the information returned to the specified metrics. Defaults to all but metadata
   * @param retryFailed Retries allocation of shards that are blocked due to too many subsequent allocation failures
   * @param timeout Explicit operation timeout
   */
  def reroute(
    body: Option[Json.Obj] = None,
    dryRun: Option[Boolean] = None,
    explain: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    metric: Chunk[String] = Chunk.empty,
    retryFailed: Option[Boolean] = None,
    timeout: Option[String] = None
  ): ZIO[Any, FrameworkException, ClusterRerouteResponse] = {
    val request = ClusterRerouteRequest(
      body = body,
      dryRun = dryRun,
      explain = explain,
      masterTimeout = masterTimeout,
      metric = metric,
      retryFailed = retryFailed,
      timeout = timeout
    )

    reroute(request)

  }

  def reroute(request: ClusterRerouteRequest): ZIO[Any, FrameworkException, ClusterRerouteResponse] =
    execute(request)

  /*
   * Returns a comprehensive information about the state of the cluster.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-state.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param flatSettings Return settings in flat format (default: false)
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
   * @param local Return local information, do not retrieve the state from master node (default: false)
   * @param masterTimeout Specify timeout for connection to master
   * @param metric Limit the information returned to the specified metrics
   * @param waitForMetadataVersion Wait for the metadata version to be equal or greater than the specified metadata version
   * @param waitForTimeout The maximum time to wait for wait_for_metadata_version before timing out
   */
  def state(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    flatSettings: Option[Boolean] = None,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty,
    local: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    metric: Option[String] = None,
    waitForMetadataVersion: Option[Double] = None,
    waitForTimeout: Option[String] = None
  ): ZIO[Any, FrameworkException, ClusterStateResponse] = {
    val request = ClusterStateRequest(
      allowNoIndices = allowNoIndices,
      expandWildcards = expandWildcards,
      flatSettings = flatSettings,
      ignoreUnavailable = ignoreUnavailable,
      indices = indices,
      local = local,
      masterTimeout = masterTimeout,
      metric = metric,
      waitForMetadataVersion = waitForMetadataVersion,
      waitForTimeout = waitForTimeout
    )

    state(request)

  }

  def state(request: ClusterStateRequest): ZIO[Any, FrameworkException, ClusterStateResponse] =
    execute(request)

  /*
   * Returns high-level overview of cluster statistics.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-stats.html
   *
   * @param flatSettings Return settings in flat format (default: false)
   * @param nodeId A comma-separated list of node IDs or names to limit the returned information; use `_local` to return information from the node you're connecting to, leave empty to get information from all nodes
   * @param timeout Explicit operation timeout
   */
  def stats(
    flatSettings: Option[Boolean] = None,
    nodeId: Chunk[String] = Chunk.empty,
    timeout: Option[String] = None
  ): ZIO[Any, FrameworkException, ClusterStatsResponse] = {
    val request = ClusterStatsRequest(flatSettings = flatSettings, nodeId = nodeId, timeout = timeout)

    stats(request)

  }

  def stats(request: ClusterStatsRequest): ZIO[Any, FrameworkException, ClusterStatsResponse] =
    execute(request)

  lazy val mappings =
    new _root_.zio.opensearch.mappings.MappingManager()(indicesService, this)

  def dropDatabase(index: String): ZIO[Any, FrameworkException, Unit] =
    for {
      exists <- indicesService.exists(Seq(index))
      _ <- if (exists.isExists) {
        for {
          _ <- indicesService.delete(Seq(index))
          _ <- health(waitForStatus = Some(WaitForStatus.yellow))
        } yield ()
      } else ZIO.unit
      dir <- baseOpenSearchService.dirty
      _ <- dir.set(false)
    } yield ()

  def getIndicesAlias(): ZIO[Any, FrameworkException, Map[String, Chunk[String]]] =
    state().map { response =>
      response.metadata.indices.map { i =>
        i._1 -> i._2.aliases
      }
    }

  def reindex(index: String)(
    implicit
    authContext: AuthContext
  ): ZIO[Any, FrameworkException, Unit] = {
    val qb = QueryBuilder(indices = List(index))(
      authContext.systemNoSQLContext(),
      this
    )
    ZIO
      .attempt(qb.scanHits.foreach { searchHit =>
        baseOpenSearchService.addToBulk(
          IndexRequest(
            searchHit.index,
            id = Some(searchHit.id),
            body = searchHit.source.getOrElse(Json.Obj())
          )
        )
      })
      .mapError(e => FrameworkException(e)) *>
      indicesService.refresh(index).ignore

  }

  def copyData(
    queryBuilder: QueryBuilder,
    destIndex: String,
    callbackSize: Int = 10000,
    callback: Int => URIO[Any, Unit] = { _ =>
      ZIO.unit
    },
    transformSource: HitResponse => Either[String, Json.Obj] = {
      _.source
    }
  ): ZIO[Any, FrameworkException, Int] = {

    def processUpdate(): ZIO[Any, FrameworkException, Int] =
      queryBuilder.scanHits.zipWithIndex.map {
        case (hit, count) =>
          for {
            body <- ZIO.fromEither(transformSource(hit))
            resp <- baseOpenSearchService.addToBulk(
              IndexRequest(
                destIndex,
                id = Some(hit.id),
                body = body
              )
            )
            _ <- callback(count.toInt).when(count % callbackSize == 0)
          } yield count
      }.run(ZSink.foldLeft[Any, Int](0)((i, _) => i + 1))

    for {
      size <- processUpdate()
      _ <- callback(size).when(size > 0)
      _ <- indicesService.flush(destIndex)
    } yield size
  }

  def getIds(index: String)(
    implicit
    authContext: AuthContext
  ): Stream[FrameworkException, String] =
    QueryBuilder(
      indices = List(index),
      bulkRead = 5000
    )(authContext.systemNoSQLContext(), this).valueChunk[String]("_id")

  def countAll(indices: Chunk[String], filters: Chunk[Query] = Chunk.empty)(
    implicit
    authContext: AuthContext
  ): ZIO[Any, FrameworkException, Long] = {
    val qb = QueryBuilder(indices = indices, size = 0, filters = filters)(authContext, this)
    qb.results.map(_.total.value)
  }

  def countAll(index: String)(implicit authContext: AuthContext): ZIO[Any, FrameworkException, Long] =
    countAll(indices = List(index))

  def search[T: JsonEncoder: JsonDecoder](
    queryBuilder: TypedQueryBuilder[T]
  ): ZIO[Any, FrameworkException, SearchResult[T]] =
    for {
      req <- queryBuilder.toRequest
      res <- this.execute(req).map { r =>
        SearchResult.fromResponse[T](r)
      }
    } yield res

  /* Get a typed JSON document from an index based on its id. */
  def searchScan[T: JsonEncoder](
    queryBuilder: TypedQueryBuilder[T]
  )(implicit decoderT: JsonDecoder[T]): ESCursor[T] =
    Cursors.typed[T](queryBuilder)

  def search(
    queryBuilder: QueryBuilder
  ): ZIO[Any, FrameworkException, SearchResponse] =
    for {
      req <- queryBuilder.toRequest
      res <- this.execute(req)
    } yield res

  def searchScan(queryBuilder: QueryBuilder): ESCursor[Json.Obj] =
    Cursors.searchHit(queryBuilder.setScan())

  def searchScanRaw(queryBuilder: QueryBuilder): ESCursor[Json.Obj] =
    Cursors.searchHit(queryBuilder.setScan())

  def searchScroll(queryBuilder: QueryBuilder): ESCursor[Json.Obj] =
    Cursors.searchHit(queryBuilder.setScan())

  /**
   * We ovveride methods to power up user management
   */
  /*
   * Returns a document.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/docs-get.html
   *
   * @param index The name of the index
   * @param id The document ID
   * @param preference Specify the node or shard the operation should be performed on (default: random)
   * @param realtime Specify whether to perform the operation in realtime or search mode
   * @param refresh Refresh the shard containing the document before performing the operation
   * @param routing Specific routing value
   * @param source True or false to return the _source field or not, or a list of fields to return
   * @param sourceExclude A list of fields to exclude from the returned _source field
   * @param sourceInclude A list of fields to extract and return from the _source field
   * @param storedFields A comma-separated list of stored fields to return in the response
   * @param version Explicit version number for concurrency control
   * @param versionType Specific version type
   */
  def get(
    index: String,
    id: String,
    preference: Option[String] = None,
    realtime: Option[Boolean] = None,
    refresh: Option[Boolean] = None,
    routing: Option[String] = None,
    source: Chunk[String] = Chunk.empty,
    sourceExclude: Chunk[String] = Chunk.empty,
    sourceInclude: Chunk[String] = Chunk.empty,
    storedFields: Chunk[String] = Chunk.empty,
    version: Option[Long] = None,
    versionType: Option[VersionType] = None
  )(implicit authContext: AuthContext): ZIO[Any, FrameworkException, GetResponse] = {
    // Custom Code On
    //alias expansion
    val ri = baseOpenSearchService.concreteIndex(Some(index))

    var request = GetRequest(
      index = ri,
      id = id,
      preference = preference,
      realtime = realtime,
      refresh = refresh,
      routing = routing,
      source = source,
      sourceExclude = sourceExclude,
      sourceInclude = sourceInclude,
      storedFields = storedFields,
      version = version,
      versionType = versionType
    )

    ZIO.logDebug(s"get($ri, $id)") *> (authContext.user match {
      case user if user.id == SystemUser.id =>
        baseOpenSearchService.get(request)
      case user =>
        //TODO add user to the request
        for {
          mapping <- this.mappings.get(baseOpenSearchService.concreteIndex(Some(index)))
          metaUser = mapping.meta.user
          res <- if (metaUser.auto_owner) {
            //we manage auto_owner objects
            request = request.copy(id = metaUser.processAutoOwnerId(id, user.id.toString()))
            baseOpenSearchService.get(request).flatMap { result =>
              if (result.found) {
                ZIO.succeed(result)
              } else {
                baseOpenSearchService.get(request.copy(id = id)) //TODO exception in it' missing
              }
            }

          } else {
            baseOpenSearchService.get(request)
          }

        } yield res

    })
  }

  /*
   * Removes a document from the index.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/docs-delete.html
   *
   * @param index The name of the index
   * @param id The document ID
   * @param ifPrimaryTerm only perform the delete operation if the last operation that has changed the document has the specified primary term
   * @param ifSeqNo only perform the delete operation if the last operation that has changed the document has the specified sequence number
   * @param refresh If `true` then refresh the effected shards to make this operation visible to search, if `wait_for` then wait for a refresh to make this operation visible to search, if `false` (the default) then do nothing with refreshes.
   * @param routing Specific routing value
   * @param timeout Explicit operation timeout
   * @param version Explicit version number for concurrency control
   * @param versionType Specific version type
   * @param waitForActiveShards Sets the number of shard copies that must be active before proceeding with the delete operation. Defaults to 1, meaning the primary shard only. Set to `all` for all shard copies, otherwise set to any non-negative value less than or equal to the total number of copies for the shard (number of replicas + 1)
   */
  def delete(
    index: String,
    id: String,
    ifPrimaryTerm: Option[Double] = None,
    ifSeqNo: Option[Double] = None,
    refresh: Option[_root_.zio.opensearch.common.Refresh] = None,
    routing: Option[String] = None,
    timeout: Option[String] = None,
    version: Option[Long] = None,
    versionType: Option[VersionType] = None,
    waitForActiveShards: Option[String] = None,
    bulk: Boolean = false
  )(implicit authContext: AuthContext): ZIO[Any, FrameworkException, DeleteResponse] = {
    //alias expansion
    //    val realDocType = this.mappings.expandAliasType(concreteIndex(Some(index)))
    val ri = baseOpenSearchService.concreteIndex(Some(index))

    def buildRequest: ZIO[Any, FrameworkException, DeleteRequest] = {
      val request = DeleteRequest(
        index = ri,
        id = id,
        ifPrimaryTerm = ifPrimaryTerm,
        ifSeqNo = ifSeqNo,
        refresh = refresh,
        version = version,
        versionType = versionType,
        routing = routing,
        timeout = timeout,
        waitForActiveShards = waitForActiveShards
      )
      if (authContext.user.id != SystemUser.id) {
        for {
          map <- this.mappings.get(ri)
          metaUser = map.meta.user
        } yield {
          if (metaUser.auto_owner) {
            //we manage auto_owner objects
            request.copy(id = metaUser.processAutoOwnerId(id, authContext.user.id.toString()))
          } else request
        }
      } else ZIO.succeed(request)

    }

    for {
      _ <- ZIO.logDebug(s"delete($ri, $id)")
      request <- buildRequest
      resp <- if (bulk) {
        baseOpenSearchService.addToBulk(request) *>
          ZIO.succeed(DeleteResponse(index = request.index, id = request.id))

      } else baseOpenSearchService.delete(request)

    } yield resp

  }

  /*
   * Creates or updates a document in an index.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/docs-index_.html
   *
   * @param index The name of the index
   * @param id Document ID
   * @param body body the body of the call
   * @param ifPrimaryTerm only perform the index operation if the last operation that has changed the document has the specified primary term
   * @param ifSeqNo only perform the index operation if the last operation that has changed the document has the specified sequence number
   * @param opType Explicit operation type. Defaults to `index` for requests with an explicit document ID, and to `create`for requests without an explicit document ID
   * @param pipeline The pipeline id to preprocess incoming documents with
   * @param refresh If `true` then refresh the affected shards to make this operation visible to search, if `wait_for` then wait for a refresh to make this operation visible to search, if `false` (the default) then do nothing with refreshes.
   * @param routing Specific routing value
   * @param timeout Explicit operation timeout
   * @param version Explicit version number for concurrency control
   * @param versionType Specific version type
   * @param waitForActiveShards Sets the number of shard copies that must be active before proceeding with the index operation. Defaults to 1, meaning the primary shard only. Set to `all` for all shard copies, otherwise set to any non-negative value less than or equal to the total number of copies for the shard (number of replicas + 1)
   */
  def indexDocument(
    index: String,
    body: Json.Obj,
    id: Option[String] = None,
    ifPrimaryTerm: Option[Double] = None,
    ifSeqNo: Option[Double] = None,
    opType: OpType = OpType.index,
    pipeline: Option[String] = None,
    refresh: Option[_root_.zio.opensearch.common.Refresh] = None,
    routing: Option[String] = None,
    timeout: Option[String] = None,
    version: Option[Long] = None,
    versionType: Option[VersionType] = None,
    waitForActiveShards: Option[Int] = None,
    bulk: Boolean = false
  )(implicit noSQLContextManager: AuthContext): ZIO[Any, FrameworkException, IndexResponse] = {
    val request = IndexRequest(
      index = index,
      body = body,
      id = id,
      ifPrimaryTerm = ifPrimaryTerm,
      ifSeqNo = ifSeqNo,
      opType = opType,
      pipeline = pipeline,
      refresh = refresh,
      routing = routing,
      timeout = timeout,
      version = version,
      //versionType = versionType,
      waitForActiveShards = waitForActiveShards
    )

    def applyMappingChanges(mapping: RootDocumentMapping, request: IndexRequest): IndexRequest =
      if (id.isDefined) {
        noSQLContextManager.user match {
          case u if u.id == SystemUser.id => request
          case u =>
            val metaUser = mapping.meta.user
            if (metaUser.auto_owner) {
              request.copy(id = Some(metaUser.processAutoOwnerId(id.get, u.id.toString())))
            } else request
        }
      } else {
        noSQLContextManager.user match {
          case user if user.id == SystemUser.id => request
          case u =>
            val metaUser = mapping.meta.user
            if (metaUser.auto_owner) {
              request.copy(id = Some(u.id.toString()))
            } else request
        }
      }

    def applyReqOrBulk(request: IndexRequest, bulk: Boolean): ZIO[Any, FrameworkException, IndexResponse] =
      if (bulk) {
        baseOpenSearchService.addToBulk(request) *>
          ZIO.succeed(
            IndexResponse(
              shards = opensearch.responses.Shards.empty,
              index = request.index,
              id = request.id.getOrElse(""),
              version = 0
            )
          )

      } else
        baseOpenSearchService.indexDocument(request)

    for {
      req <- this.mappings
        .get(baseOpenSearchService.concreteIndex(Some(index)))
        .fold[IndexRequest](_ => request, m => applyMappingChanges(m, request))
      res <- applyReqOrBulk(req, bulk)
    } yield res

  }
}

object ClusterService {

  // services

  private case class Live(
    indicesService: IndicesService,
    httpService: OpenSearchHttpService,
    baseOpenSearchService: OpenSearchService
  ) extends ClusterService

  val live: ZLayer[IndicesService, Nothing, ClusterService] =
    ZLayer {
      for { indicesService <- ZIO.service[IndicesService] } yield Live(
        indicesService,
        indicesService.httpService,
        indicesService.client
      )
    }

  // access methods

  /*
   * Provides explanations for shard allocations in the cluster.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-allocation-explain.html
   *
   * @param body body the body of the call
   * @param includeDiskInfo Return information about disk usage and shard sizes (default: false)
   * @param includeYesDecisions Return 'YES' decisions in explanation (default: false)
   */
  def allocationExplain(
    body: Option[Json.Obj] = None,
    includeDiskInfo: Option[Boolean] = None,
    includeYesDecisions: Option[Boolean] = None
  ): ZIO[ClusterService, FrameworkException, ClusterAllocationExplainResponse] =
    ZIO.environmentWithZIO[ClusterService](
      _.get.allocationExplain(body = body, includeDiskInfo = includeDiskInfo, includeYesDecisions = includeYesDecisions)
    )

  def allocationExplain(
    request: ClusterAllocationExplainRequest
  ): ZIO[ClusterService, FrameworkException, ClusterAllocationExplainResponse] =
    ZIO.environmentWithZIO[ClusterService](_.get.execute(request))

  /*
   * Returns cluster settings.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-update-settings.html
   *
   * @param flatSettings Return settings in flat format (default: false)
   * @param includeDefaults Whether to return all default clusters setting.
   * @param masterTimeout Explicit operation timeout for connection to master node
   * @param timeout Explicit operation timeout
   */
  def getSettings(
    flatSettings: Option[Boolean] = None,
    includeDefaults: Boolean = false,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None
  ): ZIO[ClusterService, FrameworkException, ClusterGetSettingsResponse] =
    ZIO.environmentWithZIO[ClusterService](
      _.get.getSettings(
        flatSettings = flatSettings,
        includeDefaults = includeDefaults,
        masterTimeout = masterTimeout,
        timeout = timeout
      )
    )

  def getSettings(
    request: ClusterGetSettingsRequest
  ): ZIO[ClusterService, FrameworkException, ClusterGetSettingsResponse] =
    ZIO.environmentWithZIO[ClusterService](_.get.execute(request))

  /*
   * Returns basic information about the health of the cluster.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-health.html
   *
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param index Limit the information returned to a specific index
   * @param level Specify the level of detail for returned information
   * @param local Return local information, do not retrieve the state from master node (default: false)
   * @param masterTimeout Explicit operation timeout for connection to master node
   * @param timeout Explicit operation timeout
   * @param waitForActiveShards Wait until the specified number of shards is active
   * @param waitForEvents Wait until all currently queued events with the given priority are processed
   * @param waitForNoInitializingShards Whether to wait until there are no initializing shards in the cluster
   * @param waitForNoRelocatingShards Whether to wait until there are no relocating shards in the cluster
   * @param waitForNodes Wait until the specified number of nodes is available
   * @param waitForStatus Wait until cluster is in a specific state
   */
  def health(
    expandWildcards: Seq[ExpandWildcards] = Nil,
    index: Option[String] = None,
    level: Level = Level.cluster,
    local: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None,
    waitForActiveShards: Option[String] = None,
    waitForEvents: Seq[WaitForEvents] = Nil,
    waitForNoInitializingShards: Option[Boolean] = None,
    waitForNoRelocatingShards: Option[Boolean] = None,
    waitForNodes: Option[String] = None,
    waitForStatus: Option[WaitForStatus] = None
  ): ZIO[ClusterService, FrameworkException, ClusterHealthResponse] =
    ZIO.environmentWithZIO[ClusterService](
      _.get.health(
        expandWildcards = expandWildcards,
        index = index,
        level = level,
        local = local,
        masterTimeout = masterTimeout,
        timeout = timeout,
        waitForActiveShards = waitForActiveShards,
        waitForEvents = waitForEvents,
        waitForNoInitializingShards = waitForNoInitializingShards,
        waitForNoRelocatingShards = waitForNoRelocatingShards,
        waitForNodes = waitForNodes,
        waitForStatus = waitForStatus
      )
    )

  def health(request: ClusterHealthRequest): ZIO[ClusterService, FrameworkException, ClusterHealthResponse] =
    ZIO.environmentWithZIO[ClusterService](_.get.execute(request))

  /*
   * Returns a list of any cluster-level changes (e.g. create index, update mapping,
allocate or fail shard) which have not yet been executed.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-pending.html
   *
   * @param local Return local information, do not retrieve the state from master node (default: false)
   * @param masterTimeout Specify timeout for connection to master
   */
  def pendingTasks(
    local: Option[Boolean] = None,
    masterTimeout: Option[String] = None
  ): ZIO[ClusterService, FrameworkException, ClusterPendingTasksResponse] =
    ZIO.environmentWithZIO[ClusterService](_.get.pendingTasks(local = local, masterTimeout = masterTimeout))

  def pendingTasks(
    request: ClusterPendingTasksRequest
  ): ZIO[ClusterService, FrameworkException, ClusterPendingTasksResponse] =
    ZIO.environmentWithZIO[ClusterService](_.get.execute(request))

  /*
   * Updates the cluster settings.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-update-settings.html
   *
   * @param body body the body of the call
   * @param flatSettings Return settings in flat format (default: false)
   * @param masterTimeout Explicit operation timeout for connection to master node
   * @param timeout Explicit operation timeout
   */
  def putSettings(
    body: Json.Obj,
    flatSettings: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None
  ): ZIO[ClusterService, FrameworkException, ClusterPutSettingsResponse] =
    ZIO.environmentWithZIO[ClusterService](
      _.get.putSettings(body = body, flatSettings = flatSettings, masterTimeout = masterTimeout, timeout = timeout)
    )

  def putSettings(
    request: ClusterPutSettingsRequest
  ): ZIO[ClusterService, FrameworkException, ClusterPutSettingsResponse] =
    ZIO.environmentWithZIO[ClusterService](_.get.execute(request))

  /*
   * Returns the information about configured remote clusters.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-remote-info.html
   *

   */
  def remoteInfo(): ZIO[ClusterService, FrameworkException, ClusterRemoteInfoResponse] =
    ZIO.environmentWithZIO[ClusterService](_.get.remoteInfo())

  def remoteInfo(
    request: ClusterRemoteInfoRequest
  ): ZIO[ClusterService, FrameworkException, ClusterRemoteInfoResponse] =
    ZIO.environmentWithZIO[ClusterService](_.get.execute(request))

  /*
   * Allows to manually change the allocation of individual shards in the cluster.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-reroute.html
   *
   * @param body body the body of the call
   * @param dryRun Simulate the operation only and return the resulting state
   * @param explain Return an explanation of why the commands can or cannot be executed
   * @param masterTimeout Explicit operation timeout for connection to master node
   * @param metric Limit the information returned to the specified metrics. Defaults to all but metadata
   * @param retryFailed Retries allocation of shards that are blocked due to too many subsequent allocation failures
   * @param timeout Explicit operation timeout
   */
  def reroute(
    body: Option[Json.Obj] = None,
    dryRun: Option[Boolean] = None,
    explain: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    metric: Chunk[String] = Chunk.empty,
    retryFailed: Option[Boolean] = None,
    timeout: Option[String] = None
  ): ZIO[ClusterService, FrameworkException, ClusterRerouteResponse] =
    ZIO.environmentWithZIO[ClusterService](
      _.get.reroute(
        body = body,
        dryRun = dryRun,
        explain = explain,
        masterTimeout = masterTimeout,
        metric = metric,
        retryFailed = retryFailed,
        timeout = timeout
      )
    )

  def reroute(request: ClusterRerouteRequest): ZIO[ClusterService, FrameworkException, ClusterRerouteResponse] =
    ZIO.environmentWithZIO[ClusterService](_.get.execute(request))

  /*
   * Returns a comprehensive information about the state of the cluster.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-state.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param flatSettings Return settings in flat format (default: false)
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
   * @param local Return local information, do not retrieve the state from master node (default: false)
   * @param masterTimeout Specify timeout for connection to master
   * @param metric Limit the information returned to the specified metrics
   * @param waitForMetadataVersion Wait for the metadata version to be equal or greater than the specified metadata version
   * @param waitForTimeout The maximum time to wait for wait_for_metadata_version before timing out
   */
  def state(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    flatSettings: Option[Boolean] = None,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty,
    local: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    metric: Option[String] = None,
    waitForMetadataVersion: Option[Double] = None,
    waitForTimeout: Option[String] = None
  ): ZIO[ClusterService, FrameworkException, ClusterStateResponse] =
    ZIO.environmentWithZIO[ClusterService](
      _.get.state(
        allowNoIndices = allowNoIndices,
        expandWildcards = expandWildcards,
        flatSettings = flatSettings,
        ignoreUnavailable = ignoreUnavailable,
        indices = indices,
        local = local,
        masterTimeout = masterTimeout,
        metric = metric,
        waitForMetadataVersion = waitForMetadataVersion,
        waitForTimeout = waitForTimeout
      )
    )

  def state(request: ClusterStateRequest): ZIO[ClusterService, FrameworkException, ClusterStateResponse] =
    ZIO.environmentWithZIO[ClusterService](_.get.execute(request))

  /*
   * Returns high-level overview of cluster statistics.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-stats.html
   *
   * @param flatSettings Return settings in flat format (default: false)
   * @param nodeId A comma-separated list of node IDs or names to limit the returned information; use `_local` to return information from the node you're connecting to, leave empty to get information from all nodes
   * @param timeout Explicit operation timeout
   */
  def stats(
    flatSettings: Option[Boolean] = None,
    nodeId: Chunk[String] = Chunk.empty,
    timeout: Option[String] = None
  ): ZIO[ClusterService, FrameworkException, ClusterStatsResponse] =
    ZIO.environmentWithZIO[ClusterService](_.get.stats(flatSettings = flatSettings, nodeId = nodeId, timeout = timeout))

  def stats(request: ClusterStatsRequest): ZIO[ClusterService, FrameworkException, ClusterStatsResponse] =
    ZIO.environmentWithZIO[ClusterService](_.get.execute(request))

  // cluster service custom

  def dropDatabase(index: String): ZIO[ClusterService, FrameworkException, Unit] =
    ZIO.environmentWithZIO[ClusterService](_.get.dropDatabase(index))

  def getIndicesAlias(): ZIO[ClusterService, FrameworkException, Map[String, Chunk[String]]] =
    ZIO.environmentWithZIO[ClusterService](_.get.getIndicesAlias())

  def reindex(index: String)(
    implicit
    authContext: AuthContext
  ): ZIO[ClusterService, FrameworkException, Unit] =
    ZIO.environmentWithZIO[ClusterService](_.get.reindex(index))

  def copyData(
    queryBuilder: QueryBuilder,
    destIndex: String,
    callbackSize: Int = 10000,
    callback: Int => URIO[Any, Unit] = { _ =>
      ZIO.unit
    },
    transformSource: HitResponse => Either[String, Json.Obj] = {
      _.source
    }
  ): ZIO[ClusterService, FrameworkException, Int] =
    ZIO.environmentWithZIO[ClusterService](
      _.get.copyData(
        queryBuilder,
        destIndex,
        callbackSize = callbackSize,
        callback = callback,
        transformSource = transformSource
      )
    )
//
//  def getIds(index: String)(
//    implicit authContext: AuthContext
//  ): Stream[FrameworkException, String] =
//    ZIO.environmentWithZIO[ClusterService](_.get.getIds(index))

  def countAll(indices: Chunk[String], filters: Chunk[Query] = Chunk.empty)(
    implicit
    authContext: AuthContext
  ): ZIO[ClusterService, FrameworkException, Long] =
    ZIO.environmentWithZIO[ClusterService](_.get.countAll(indices, filters))

  def countAll(index: String)(implicit authContext: AuthContext): ZIO[ClusterService, FrameworkException, Long] =
    countAll(indices = List(index))

  def search[T: JsonEncoder: JsonDecoder](
    queryBuilder: TypedQueryBuilder[T]
  ): ZIO[ClusterService, FrameworkException, SearchResult[T]] =
    ZIO.environmentWithZIO[ClusterService](_.get.search[T](queryBuilder))

  /* Get a typed JSON document from an index based on its id. */
  def searchScan[T: JsonEncoder](
    queryBuilder: TypedQueryBuilder[T]
  )(implicit decoderT: JsonDecoder[T]): ESCursor[T] =
    Cursors.typed[T](queryBuilder)

  def search(
    queryBuilder: QueryBuilder
  ): ZIO[ClusterService, FrameworkException, SearchResponse] =
    ZIO.environmentWithZIO[ClusterService](_.get.search(queryBuilder))

  def searchScan(queryBuilder: QueryBuilder): ESCursor[Json.Obj] =
    Cursors.searchHit(queryBuilder.setScan())

  def searchScanRaw(queryBuilder: QueryBuilder): ESCursor[Json.Obj] =
    Cursors.searchHit(queryBuilder.setScan())

  def searchScroll(queryBuilder: QueryBuilder): ESCursor[Json.Obj] =
    Cursors.searchHit(queryBuilder.setScan())

  /**
   * We ovveride methods to power up user management
   */
  /*
   * Returns a document.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/docs-get.html
   *
   * @param index The name of the index
   * @param id The document ID
   * @param preference Specify the node or shard the operation should be performed on (default: random)
   * @param realtime Specify whether to perform the operation in realtime or search mode
   * @param refresh Refresh the shard containing the document before performing the operation
   * @param routing Specific routing value
   * @param source True or false to return the _source field or not, or a list of fields to return
   * @param sourceExclude A list of fields to exclude from the returned _source field
   * @param sourceInclude A list of fields to extract and return from the _source field
   * @param storedFields A comma-separated list of stored fields to return in the response
   * @param version Explicit version number for concurrency control
   * @param versionType Specific version type
   */
  def get(
    index: String,
    id: String,
    preference: Option[String] = None,
    realtime: Option[Boolean] = None,
    refresh: Option[Boolean] = None,
    routing: Option[String] = None,
    source: Chunk[String] = Chunk.empty,
    sourceExclude: Chunk[String] = Chunk.empty,
    sourceInclude: Chunk[String] = Chunk.empty,
    storedFields: Chunk[String] = Chunk.empty,
    version: Option[Long] = None,
    versionType: Option[VersionType] = None
  )(implicit authContext: AuthContext): ZIO[ClusterService, FrameworkException, GetResponse] =
    ZIO.environmentWithZIO[ClusterService](
      _.get.get(
        index,
        id,
        preference = preference,
        realtime = realtime,
        refresh = refresh,
        routing = routing,
        source = source,
        sourceExclude = sourceExclude,
        sourceInclude = sourceInclude,
        storedFields = storedFields,
        version = version,
        versionType = versionType
      )
    )

  /*
   * Removes a document from the index.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/docs-delete.html
   *
   * @param index The name of the index
   * @param id The document ID
   * @param ifPrimaryTerm only perform the delete operation if the last operation that has changed the document has the specified primary term
   * @param ifSeqNo only perform the delete operation if the last operation that has changed the document has the specified sequence number
   * @param refresh If `true` then refresh the effected shards to make this operation visible to search, if `wait_for` then wait for a refresh to make this operation visible to search, if `false` (the default) then do nothing with refreshes.
   * @param routing Specific routing value
   * @param timeout Explicit operation timeout
   * @param version Explicit version number for concurrency control
   * @param versionType Specific version type
   * @param waitForActiveShards Sets the number of shard copies that must be active before proceeding with the delete operation. Defaults to 1, meaning the primary shard only. Set to `all` for all shard copies, otherwise set to any non-negative value less than or equal to the total number of copies for the shard (number of replicas + 1)
   */
  def delete(
    index: String,
    id: String,
    ifPrimaryTerm: Option[Double] = None,
    ifSeqNo: Option[Double] = None,
    refresh: Option[_root_.zio.opensearch.common.Refresh] = None,
    routing: Option[String] = None,
    timeout: Option[String] = None,
    version: Option[Long] = None,
    versionType: Option[VersionType] = None,
    waitForActiveShards: Option[String] = None,
    bulk: Boolean = false
  )(implicit authContext: AuthContext): ZIO[ClusterService, FrameworkException, DeleteResponse] =
    ZIO.environmentWithZIO[ClusterService](
      _.get.delete(
        index = index,
        id = id,
        ifPrimaryTerm = ifPrimaryTerm,
        ifSeqNo = ifSeqNo,
        refresh = refresh,
        routing = routing,
        timeout = timeout,
        version = version,
        versionType = versionType,
        waitForActiveShards = waitForActiveShards,
        bulk = bulk
      )
    )

  /*
   * Creates or updates a document in an index.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/docs-index_.html
   *
   * @param index The name of the index
   * @param id Document ID
   * @param body body the body of the call
   * @param ifPrimaryTerm only perform the index operation if the last operation that has changed the document has the specified primary term
   * @param ifSeqNo only perform the index operation if the last operation that has changed the document has the specified sequence number
   * @param opType Explicit operation type. Defaults to `index` for requests with an explicit document ID, and to `create`for requests without an explicit document ID
   * @param pipeline The pipeline id to preprocess incoming documents with
   * @param refresh If `true` then refresh the affected shards to make this operation visible to search, if `wait_for` then wait for a refresh to make this operation visible to search, if `false` (the default) then do nothing with refreshes.
   * @param routing Specific routing value
   * @param timeout Explicit operation timeout
   * @param version Explicit version number for concurrency control
   * @param versionType Specific version type
   * @param waitForActiveShards Sets the number of shard copies that must be active before proceeding with the index operation. Defaults to 1, meaning the primary shard only. Set to `all` for all shard copies, otherwise set to any non-negative value less than or equal to the total number of copies for the shard (number of replicas + 1)
   */
  def indexDocument(
    index: String,
    body: Json.Obj,
    id: Option[String] = None,
    ifPrimaryTerm: Option[Double] = None,
    ifSeqNo: Option[Double] = None,
    opType: OpType = OpType.index,
    pipeline: Option[String] = None,
    refresh: Option[_root_.zio.opensearch.common.Refresh] = None,
    routing: Option[String] = None,
    timeout: Option[String] = None,
    version: Option[Long] = None,
    versionType: Option[VersionType] = None,
    waitForActiveShards: Option[Int] = None,
    bulk: Boolean = false
  )(implicit noSQLContextManager: AuthContext): ZIO[ClusterService, FrameworkException, IndexResponse] =
    ZIO.environmentWithZIO[ClusterService](
      _.get.indexDocument(
        index = index,
        body = body,
        id = id,
        ifPrimaryTerm = ifPrimaryTerm,
        ifSeqNo = ifSeqNo,
        opType = opType,
        pipeline = pipeline,
        refresh = refresh,
        routing = routing,
        timeout = timeout,
        version = version,
        versionType = versionType,
        waitForActiveShards = waitForActiveShards,
        bulk = bulk
      )
    )

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
    searchAfter: Array[AnyRef] = Array(),
    source: SourceConfig = SourceConfig(),
    suggestions: Map[String, Suggestion] = Map.empty[String, Suggestion],
    aggregations: Aggregation.Aggregations = Aggregation.EmptyAggregations,
    isSingleIndex: Boolean = true,
    extraBody: Option[Json.Obj] = None
  )(implicit authContext: AuthContext): ZIO[ClusterService, FrameworkException, QueryBuilder] =
    ZIO.environmentWithZIO[ClusterService](
      cs =>
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
          )(authContext, cs.get)
        )
    )

}
