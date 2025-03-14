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

import zio.exception.FrameworkException
import zio.opensearch.OpenSearchService
import zio.opensearch.client.IndicesActionResolver
import zio.opensearch.mappings.RootDocumentMapping
import zio.opensearch.requests.indices._
import zio.opensearch.responses.indices._
import zio.json.ast._
import zio.json._
import zio._
import zio.opensearch.common.{ DefaultOperator, ExpandWildcards, Level }

trait IndicesService extends IndicesActionResolver {
  def client: OpenSearchService
  protected var defaultSettings = Settings.OpenSearchBase
  protected var defaultTestSettings = Settings.OpenSearchTestBase
  //default settings to build index
  //  var defaultIndex = "default"
  protected var alias = Set.empty[String]

  def refreshA(): ZIO[Any, FrameworkException, Unit] =
    for {
      _ <- flushBulk()
      _ <- refresh(Nil)
      dir <- client.dirty
      _ <- dir.set(false)
    } yield ()

  def exists(
    indices: String*
  ): ZIO[Any, FrameworkException, IndicesExistsResponse] =
    exists(indices)

  def flush(
    indices: String*
  ): ZIO[Any, FrameworkException, IndicesFlushResponse] =
    flush(indices)

  def refresh(): ZIO[Any, FrameworkException, IndicesRefreshResponse] =
    refresh(Nil)

  def refresh(
    index: String,
    indices: String*
  ): ZIO[Any, FrameworkException, IndicesRefreshResponse] =
    refresh(index +: indices)

  def flushBulk(): ZIO[Any, FrameworkException, IndicesFlushResponse] =
    for {
      blkr <- client.bulker
      _ <- blkr.flushBulk()
    } yield IndicesFlushResponse()

  /*
   * Performs the analysis process on a text and return the tokens breakdown of the text.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-analyze.html
   *
   * @param body body the body of the call
   * @param index The name of the index to scope the operation
   */
  def analyze(body: Json.Obj, index: Option[String] = None): ZIO[Any, FrameworkException, IndicesAnalyzeResponse] = {
    val request = IndicesAnalyzeRequest(body = body, index = index)

    analyze(request)

  }

  def analyze(request: IndicesAnalyzeRequest): ZIO[Any, FrameworkException, IndicesAnalyzeResponse] =
    execute(request)

  /*
   * Clears all or specific caches for one or more indices.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-clearcache.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param fielddata Clear field data
   * @param fields A comma-separated list of fields to clear when using the `fielddata` parameter (default: all)
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param index A comma-separated list of index name to limit the operation
   * @param indices A comma-separated list of index name to limit the operation
   * @param query Clear query caches
   * @param request Clear request cache
   */
  def clearCache(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    fielddata: Option[Boolean] = None,
    fields: Chunk[String] = Chunk.empty,
    ignoreUnavailable: Option[Boolean] = None,
    index: Chunk[String] = Chunk.empty,
    indices: Chunk[String] = Chunk.empty,
    query: Option[Boolean] = None,
    request: Option[Boolean] = None
  ): ZIO[Any, FrameworkException, IndicesClearCacheResponse] = {
    val requestI = IndicesClearCacheRequest(
      allowNoIndices = allowNoIndices,
      expandWildcards = expandWildcards,
      fielddata = fielddata,
      fields = fields,
      ignoreUnavailable = ignoreUnavailable,
      index = index,
      indices = indices,
      query = query,
      request = request
    )

    clearCache(requestI)

  }

  def clearCache(request: IndicesClearCacheRequest): ZIO[Any, FrameworkException, IndicesClearCacheResponse] = execute(
    request
  )

  /*
   * Clones an index
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-clone-index.html
   *
   * @param index The name of the source index to clone
   * @param target The name of the target index to clone into
   * @param body body the body of the call
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Explicit operation timeout
   * @param waitForActiveShards Set the number of active shards to wait for on the cloned index before the operation returns.
   */
  def clone(
    index: String,
    target: String,
    body: Option[Json.Obj] = None,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None,
    waitForActiveShards: Option[String] = None
  ): ZIO[Any, FrameworkException, IndicesCloneResponse] = {
    val request = IndicesCloneRequest(
      index = index,
      target = target,
      body = body,
      masterTimeout = masterTimeout,
      timeout = timeout,
      waitForActiveShards = waitForActiveShards
    )

    clone(request)

  }

  def clone(request: IndicesCloneRequest): ZIO[Any, FrameworkException, IndicesCloneResponse] =
    execute(request)

  /*
   * Closes an index.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-open-close.html
   *
   * @param index A comma separated list of indices to close
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Explicit operation timeout
   * @param waitForActiveShards Sets the number of active shards to wait for before the operation returns.
   */
  def close(
    index: String,
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None,
    waitForActiveShards: Option[String] = None
  ): ZIO[Any, FrameworkException, IndicesCloseResponse] = {
    val request = IndicesCloseRequest(
      index = index,
      allowNoIndices = allowNoIndices,
      expandWildcards = expandWildcards,
      ignoreUnavailable = ignoreUnavailable,
      masterTimeout = masterTimeout,
      timeout = timeout,
      waitForActiveShards = waitForActiveShards
    )

    close(request)

  }

  def close(request: IndicesCloseRequest): ZIO[Any, FrameworkException, IndicesCloseResponse] =
    execute(request)

  /*
   * Creates an index with optional settings and mappings.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-create-index.html
   *
   * @param index The name of the index
   * @param body body the body of the call
   * @param includeTypeName Whether a type should be expected in the body of the mappings.
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Explicit operation timeout
   * @param waitForActiveShards Set the number of active shards to wait for before the operation returns.
   */
  def createIfNotExists(
    index: String,
    body: Json.Obj = Json.Obj(),
    waitForActiveShards: Option[Int] = None,
    timeout: Option[String] = None,
    masterTimeout: Option[String] = None
  ): ZIO[Any, FrameworkException, Unit] =
    for {
      existsRes <- exists(index)
      _ <- ZIO.when(!existsRes.exists)(
        create(
          IndicesCreateRequest(
            index = index,
            body = JsonUtils.cleanValue(body).asInstanceOf[Json.Obj], // we remove null
            waitForActiveShards = waitForActiveShards,
            timeout = timeout,
            masterTimeout = masterTimeout
          )
        )
      )
    } yield ()

  def create(
    index: String,
    body: Json.Obj = Json.Obj(),
    includeTypeName: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None,
    waitForActiveShards: Option[Int] = None
  ): ZIO[Any, FrameworkException, IndicesCreateResponse] = {
    val request = IndicesCreateRequest(
      index = index,
      body = body,
      includeTypeName = includeTypeName,
      masterTimeout = masterTimeout,
      timeout = timeout,
      waitForActiveShards = waitForActiveShards
    )

    create(request)

  }

  def create(request: IndicesCreateRequest): ZIO[Any, FrameworkException, IndicesCreateResponse] =
    execute(request)

  /*
   * Deletes an index.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-delete-index.html
   *
   * @param indices A comma-separated list of indices to delete; use `_all` or `*` string to delete all indices
   * @param allowNoIndices Ignore if a wildcard expression resolves to no concrete indices (default: false)
   * @param expandWildcards Whether wildcard expressions should get expanded to open or closed indices (default: open)
   * @param ignoreUnavailable Ignore unavailable indexes (default: false)
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Explicit operation timeout
   */
  def delete(
    indices: Chunk[String] = Chunk.empty,
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None
  ): ZIO[Any, FrameworkException, IndicesDeleteResponse] = {
    val request = IndicesDeleteRequest(
      indices = indices,
      allowNoIndices = allowNoIndices,
      expandWildcards = expandWildcards,
      ignoreUnavailable = ignoreUnavailable,
      masterTimeout = masterTimeout,
      timeout = timeout
    )

    delete(request)

  }

  def delete(request: IndicesDeleteRequest): ZIO[Any, FrameworkException, IndicesDeleteResponse] =
    execute(request)

  /*
   * Deletes an alias.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-aliases.html
   *
   * @param indices A comma-separated list of index names (supports wildcards); use `_all` for all indices
   * @param name A comma-separated list of aliases to delete (supports wildcards); use `_all` to delete all aliases for the specified indices.
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Explicit timestamp for the document
   */
  def deleteAlias(
    names: Chunk[String] = Chunk.empty,
    indices: Chunk[String] = Chunk.empty,
    timeout: Option[String] = None,
    masterTimeout: Option[String] = None
  ): ZIO[Any, FrameworkException, IndicesDeleteAliasResponse] = {

    val request =
      IndicesDeleteAliasRequest(
        name = names,
        indices = indices,
        timeout = timeout,
        masterTimeout = masterTimeout
      )
    deleteAlias(request)

  }

  def deleteAlias(
    request: IndicesDeleteAliasRequest
  ): ZIO[Any, FrameworkException, IndicesDeleteAliasResponse] =
    execute(request)

  def addAlias(
    alias: String,
    indices: Chunk[String]
  ): ZIO[Any, FrameworkException, IndicesPutAliasResponse] =
    putAlias(indices = indices, name = alias)

  /*
   * Deletes an index template.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-templates.html
   *
   * @param name The name of the template
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Explicit operation timeout
   */
  def deleteTemplate(
    name: String,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None
  ): ZIO[Any, FrameworkException, IndicesDeleteTemplateResponse] = {
    val request = IndicesDeleteTemplateRequest(name = name, masterTimeout = masterTimeout, timeout = timeout)

    deleteTemplate(request)

  }

  def deleteTemplate(
    request: IndicesDeleteTemplateRequest
  ): ZIO[Any, FrameworkException, IndicesDeleteTemplateResponse] =
    execute(request)

  def exists(
    index: String
  ): ZIO[Any, FrameworkException, IndicesExistsResponse] =
    exists(Seq(index))

  def existsAsBoolean(
    index: String
  ): ZIO[Any, FrameworkException, Boolean] =
    exists(Seq(index)).map(_.exists)

  /*
   * Returns information about whether a particular index exists.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-exists.html
   *
   * @param indices A comma-separated list of index names
   * @param allowNoIndices Ignore if a wildcard expression resolves to no concrete indices (default: false)
   * @param expandWildcards Whether wildcard expressions should get expanded to open or closed indices (default: open)
   * @param flatSettings Return settings in flat format (default: false)
   * @param ignoreUnavailable Ignore unavailable indexes (default: false)
   * @param includeDefaults Whether to return all default setting for each of the indices.
   * @param local Return local information, do not retrieve the state from master node (default: false)
   */
  def exists(
    indices: Chunk[String] = Chunk.empty,
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    flatSettings: Option[Boolean] = None,
    ignoreUnavailable: Option[Boolean] = None,
    includeDefaults: Boolean = false,
    local: Option[Boolean] = None
  ): ZIO[Any, FrameworkException, IndicesExistsResponse] = {
    val request = IndicesExistsRequest(
      indices = indices,
      allowNoIndices = allowNoIndices,
      expandWildcards = expandWildcards,
      flatSettings = flatSettings,
      ignoreUnavailable = ignoreUnavailable,
      includeDefaults = includeDefaults,
      local = local
    )

    exists(request)

  }

  def exists(request: IndicesExistsRequest): ZIO[Any, FrameworkException, IndicesExistsResponse] =
    execute(request)

  /*
   * Returns information about whether a particular alias exists.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-aliases.html
   *
   * @param name A comma-separated list of alias names to return
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names to filter aliases
   * @param local Return local information, do not retrieve the state from master node (default: false)
   */
  def existsAlias(
    name: Chunk[String] = Chunk.empty,
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty,
    local: Option[Boolean] = None
  ): ZIO[Any, FrameworkException, IndicesExistsAliasResponse] = {
    val request = IndicesExistsAliasRequest(
      name = name,
      allowNoIndices = allowNoIndices,
      expandWildcards = expandWildcards,
      ignoreUnavailable = ignoreUnavailable,
      indices = indices,
      local = local
    )

    existsAlias(request)

  }

  def existsAlias(request: IndicesExistsAliasRequest): ZIO[Any, FrameworkException, IndicesExistsAliasResponse] =
    execute(request)

  /*
   * Returns information about whether a particular index template exists.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-templates.html
   *
   * @param name The comma separated names of the index templates
   * @param flatSettings Return settings in flat format (default: false)
   * @param local Return local information, do not retrieve the state from master node (default: false)
   * @param masterTimeout Explicit operation timeout for connection to master node
   */
  def existsTemplate(
    name: String,
    flatSettings: Option[Boolean] = None,
    local: Option[Boolean] = None,
    masterTimeout: Option[String] = None
  ): ZIO[Any, FrameworkException, Boolean] = {
    val request = IndicesExistsTemplateRequest(
      name = name,
      flatSettings = flatSettings,
      local = local,
      masterTimeout = masterTimeout
    )

    existsTemplate(request).map(_.contains(name))

  }

  def existsTemplate(
    request: IndicesExistsTemplateRequest
  ): ZIO[Any, FrameworkException, IndicesExistsTemplateResponse] =
    execute(request)

  /*
   * Performs the flush operation on one or more indices.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-flush.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param force Whether a flush should be forced even if it is not necessarily needed ie. if no changes will be committed to the index. This is useful if transaction log IDs should be incremented even if no uncommitted changes are present. (This setting can be considered as internal)
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names; use `_all` or empty string for all indices
   * @param waitIfOngoing If set to true the flush operation will block until the flush can be executed if another flush operation is already executing. The default is true. If set to false the flush will be skipped iff if another flush operation is already running.
   */
  def flush(
    indices: Chunk[String] = Chunk.empty,
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    force: Option[Boolean] = None,
    ignoreUnavailable: Option[Boolean] = None,
    waitIfOngoing: Option[Boolean] = None
  ): ZIO[Any, FrameworkException, IndicesFlushResponse] = {
    val request = IndicesFlushRequest(
      allowNoIndices = allowNoIndices,
      expandWildcards = expandWildcards,
      force = force,
      ignoreUnavailable = ignoreUnavailable,
      indices = indices,
      waitIfOngoing = waitIfOngoing
    )

    flush(request)

  }

  def flush(
    request: IndicesFlushRequest
  ): ZIO[Any, FrameworkException, IndicesFlushResponse] =
    execute(request)

  /*
   * Performs a synced flush operation on one or more indices.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-synced-flush-api.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names; use `_all` or empty string for all indices
   */
  def flushSynced(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty
  ): ZIO[Any, FrameworkException, IndicesFlushSyncedResponse] = {
    val request = IndicesFlushSyncedRequest(
      allowNoIndices = allowNoIndices,
      expandWildcards = expandWildcards,
      ignoreUnavailable = ignoreUnavailable,
      indices = indices
    )

    flushSynced(request)

  }

  def flushSynced(request: IndicesFlushSyncedRequest): ZIO[Any, FrameworkException, IndicesFlushSyncedResponse] =
    execute(request)

  /*
   * Performs the force merge operation on one or more indices.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-forcemerge.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param flush Specify whether the index should be flushed after performing the operation (default: true)
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
   * @param maxNumSegments The number of segments the index should be merged into (default: dynamic)
   * @param onlyExpungeDeletes Specify whether the operation should only expunge deleted documents
   */
  def forcemerge(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    flush: Option[Boolean] = None,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty,
    maxNumSegments: Option[Double] = None,
    onlyExpungeDeletes: Option[Boolean] = None
  ): ZIO[Any, FrameworkException, IndicesForcemergeResponse] = {
    val request = IndicesForcemergeRequest(
      allowNoIndices = allowNoIndices,
      expandWildcards = expandWildcards,
      flush = flush,
      ignoreUnavailable = ignoreUnavailable,
      indices = indices,
      maxNumSegments = maxNumSegments,
      onlyExpungeDeletes = onlyExpungeDeletes
    )

    forcemerge(request)

  }

  def forcemerge(
    request: IndicesForcemergeRequest
  ): ZIO[Any, FrameworkException, IndicesForcemergeResponse] = execute(request)

  /*
   * Returns information about one or more indices.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-get-index.html
   *
   * @param indices A comma-separated list of index names
   * @param allowNoIndices Ignore if a wildcard expression resolves to no concrete indices (default: false)
   * @param expandWildcards Whether wildcard expressions should get expanded to open or closed indices (default: open)
   * @param flatSettings Return settings in flat format (default: false)
   * @param ignoreUnavailable Ignore unavailable indexes (default: false)
   * @param includeDefaults Whether to return all default setting for each of the indices.
   * @param includeTypeName Whether to add the type name to the response (default: false)
   * @param local Return local information, do not retrieve the state from master node (default: false)
   * @param masterTimeout Specify timeout for connection to master
   */
  def get(
    indices: Chunk[String] = Chunk.empty,
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    flatSettings: Option[Boolean] = None,
    ignoreUnavailable: Option[Boolean] = None,
    includeDefaults: Boolean = false,
    includeTypeName: Option[Boolean] = None,
    local: Option[Boolean] = None,
    masterTimeout: Option[String] = None
  ): ZIO[Any, FrameworkException, IndicesGetResponse] = {
    val request = IndicesGetRequest(
      indices = indices,
      allowNoIndices = allowNoIndices,
      expandWildcards = expandWildcards,
      flatSettings = flatSettings,
      ignoreUnavailable = ignoreUnavailable,
      includeDefaults = includeDefaults,
      includeTypeName = includeTypeName,
      local = local,
      masterTimeout = masterTimeout
    )

    get(request)

  }

  def get(
    request: IndicesGetRequest
  ): ZIO[Any, FrameworkException, IndicesGetResponse] =
    execute(request)

  /*
   * Returns an alias.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-aliases.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names to filter aliases
   * @param local Return local information, do not retrieve the state from master node (default: false)
   * @param name A comma-separated list of alias names to return
   */
  def getAlias(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty,
    local: Option[Boolean] = None,
    name: Chunk[String] = Chunk.empty
  ): ZIO[Any, FrameworkException, IndicesGetAliasResponse] = {
    val request = IndicesGetAliasRequest(
      allowNoIndices = allowNoIndices,
      expandWildcards = expandWildcards,
      ignoreUnavailable = ignoreUnavailable,
      indices = indices,
      local = local,
      name = name
    )

    getAlias(request)

  }

  def getAlias(request: IndicesGetAliasRequest): ZIO[Any, FrameworkException, IndicesGetAliasResponse] =
    execute(request)

  /*
   * Returns mapping for one or more fields.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-get-field-mapping.html
   *
   * @param fields A comma-separated list of fields
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param includeDefaults Whether the default mapping values should be returned as well
   * @param includeTypeName Whether a type should be returned in the body of the mappings.
   * @param indices A comma-separated list of index names
   * @param local Return local information, do not retrieve the state from master node (default: false)
   */
  def getFieldMapping(
    fields: Chunk[String] = Chunk.empty,
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    includeDefaults: Option[Boolean] = None,
    includeTypeName: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty,
    local: Option[Boolean] = None
  ): ZIO[Any, FrameworkException, IndicesGetFieldMappingResponse] = {
    val request = IndicesGetFieldMappingRequest(
      fields = fields,
      allowNoIndices = allowNoIndices,
      expandWildcards = expandWildcards,
      ignoreUnavailable = ignoreUnavailable,
      includeDefaults = includeDefaults,
      includeTypeName = includeTypeName,
      indices = indices,
      local = local
    )

    getFieldMapping(request)

  }

  def getFieldMapping(
    request: IndicesGetFieldMappingRequest
  ): ZIO[Any, FrameworkException, IndicesGetFieldMappingResponse] =
    execute(request)

  /*
   * Returns mappings for one or more indices.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-get-mapping.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names
   * @param local Return local information, do not retrieve the state from master node (default: false)
   * @param masterTimeout Specify timeout for connection to master
   */
  def getMapping(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty,
    local: Option[Boolean] = None,
    masterTimeout: Option[String] = None
  ): ZIO[Any, FrameworkException, IndicesGetMappingResponse] = {
    val request = IndicesGetMappingRequest(
      allowNoIndices = allowNoIndices,
      expandWildcards = expandWildcards,
      ignoreUnavailable = ignoreUnavailable,
      indices = indices,
      local = local,
      masterTimeout = masterTimeout
    )

    getMapping(request)

  }

  def getMapping(request: IndicesGetMappingRequest): ZIO[Any, FrameworkException, IndicesGetMappingResponse] = execute(
    request
  )

  /*
   * Returns settings for one or more indices.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-get-settings.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param flatSettings Return settings in flat format (default: false)
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param includeDefaults Whether to return all default setting for each of the indices.
   * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
   * @param local Return local information, do not retrieve the state from master node (default: false)
   * @param masterTimeout Specify timeout for connection to master
   * @param name The name of the settings that should be included
   */
  def getSettings(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    flatSettings: Option[Boolean] = None,
    ignoreUnavailable: Option[Boolean] = None,
    includeDefaults: Boolean = false,
    indices: Chunk[String] = Chunk.empty,
    local: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    name: Option[String] = None
  ): ZIO[Any, FrameworkException, IndicesGetSettingsResponse] = {
    val request = IndicesGetSettingsRequest(
      allowNoIndices = allowNoIndices,
      expandWildcards = expandWildcards,
      flatSettings = flatSettings,
      ignoreUnavailable = ignoreUnavailable,
      includeDefaults = includeDefaults,
      indices = indices,
      local = local,
      masterTimeout = masterTimeout,
      name = name
    )

    getSettings(request)

  }

  def getSettings(request: IndicesGetSettingsRequest): ZIO[Any, FrameworkException, IndicesGetSettingsResponse] =
    execute(request)

  /*
   * Returns an index template.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-templates.html
   *
   * @param flatSettings Return settings in flat format (default: false)
   * @param includeTypeName Whether a type should be returned in the body of the mappings.
   * @param local Return local information, do not retrieve the state from master node (default: false)
   * @param masterTimeout Explicit operation timeout for connection to master node
   * @param name The comma separated names of the index templates
   */
  def getTemplate(
    flatSettings: Option[Boolean] = None,
    includeTypeName: Option[Boolean] = None,
    local: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    name: Option[String] = None
  ): ZIO[Any, FrameworkException, IndicesGetTemplateResponse] = {
    val request = IndicesGetTemplateRequest(
      flatSettings = flatSettings,
      includeTypeName = includeTypeName,
      local = local,
      masterTimeout = masterTimeout,
      name = name
    )

    getTemplate(request)

  }

  def getTemplate(request: IndicesGetTemplateRequest): ZIO[Any, FrameworkException, IndicesGetTemplateResponse] =
    execute(request)

  /*
   * Shortcut to return all the templates
   */
  def getTemplates(): ZIO[Any, FrameworkException, IndicesGetTemplateResponse] = {
    val request =
      IndicesGetTemplateRequest(
        name = None,
        flatSettings = Some(false),
        masterTimeout = None,
        local = Some(true)
      )

    getTemplate(request)

  }

  /*
   * The _upgrade API is no longer useful and will be removed.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-upgrade.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
   */
  def getUpgrade(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty
  ): ZIO[Any, FrameworkException, IndicesGetUpgradeResponse] = {
    val request = IndicesGetUpgradeRequest(
      allowNoIndices = allowNoIndices,
      expandWildcards = expandWildcards,
      ignoreUnavailable = ignoreUnavailable,
      indices = indices
    )

    getUpgrade(request)

  }

  def getUpgrade(request: IndicesGetUpgradeRequest): ZIO[Any, FrameworkException, IndicesGetUpgradeResponse] = execute(
    request
  )

  /*
   * Opens an index.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-open-close.html
   *
   * @param index A comma separated list of indices to open
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Explicit operation timeout
   * @param waitForActiveShards Sets the number of active shards to wait for before the operation returns.
   */
  def open(
    indices: Chunk[String],
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None,
    waitForActiveShards: Option[String] = None
  ): ZIO[Any, FrameworkException, IndicesOpenResponse] = {
    val request = IndicesOpenRequest(
      indices = indices,
      allowNoIndices = allowNoIndices,
      expandWildcards = expandWildcards,
      ignoreUnavailable = ignoreUnavailable,
      masterTimeout = masterTimeout,
      timeout = timeout,
      waitForActiveShards = waitForActiveShards
    )

    open(request)

  }

  def open(request: IndicesOpenRequest): ZIO[Any, FrameworkException, IndicesOpenResponse] =
    execute(request)

  def open(
    index: String
  ): ZIO[Any, FrameworkException, IndicesOpenResponse] =
    execute(new IndicesOpenRequest(Seq(index)))

  /*
   * http://www.elastic.co/guide/en/opensearch/reference/master/indices-aliases.html
   *
   * @param indices A list of index names the alias should point to (supports wildcards); use `_all` or omit to perform the operation on all indices.
   * @param name The name of the alias to be created or updated
   * @param body body the body of the call
   * @param timeout Explicit timestamp for the document
   * @param masterTimeout Specify timeout for connection to master
   */
  def putAlias(
    indices: Chunk[String] = Chunk.empty,
    name: String,
    body: Json.Obj = Json.Obj(),
    timeout: Option[String] = None,
    masterTimeout: Option[String] = None
  ): ZIO[Any, FrameworkException, IndicesPutAliasResponse] = {
    val request = IndicesPutAliasRequest(
      indices = indices,
      name = name,
      timeout = timeout,
      masterTimeout = masterTimeout
    )

    putAlias(request)

  }

  def putAlias(
    request: IndicesPutAliasRequest
  ): ZIO[Any, FrameworkException, IndicesPutAliasResponse] =
    execute(request)

  def putMapping(
    index: String,
    mapping: RootDocumentMapping
  ): ZIO[Any, FrameworkException, IndicesPutMappingResponse] =
    putMapping(indices = Seq(index), body = mapping.toJsonAST.toOption.get.asInstanceOf[Json.Obj])

  /*
   * Updates the index mappings.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-put-mapping.html
   *
   * @param indices A comma-separated list of index names the mapping should be added to (supports wildcards); use `_all` or omit to add the mapping on all indices.
   * @param body body the body of the call
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Explicit operation timeout
   */
  def putMapping(
    indices: Chunk[String] = Chunk.empty,
    body: Json.Obj,
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None
  ): ZIO[Any, FrameworkException, IndicesPutMappingResponse] = {
    val request = IndicesPutMappingRequest(
      indices = indices,
      body = body,
      allowNoIndices = allowNoIndices,
      expandWildcards = expandWildcards,
      ignoreUnavailable = ignoreUnavailable,
      masterTimeout = masterTimeout,
      timeout = timeout
    )

    putMapping(request)

  }

  def putMapping(
    request: IndicesPutMappingRequest
  ): ZIO[Any, FrameworkException, IndicesPutMappingResponse] =
    execute(request)

  /*
   * Updates the index settings.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-update-settings.html
   *
   * @param body body the body of the call
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param flatSettings Return settings in flat format (default: false)
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
   * @param masterTimeout Specify timeout for connection to master
   * @param preserveExisting Whether to update existing settings. If set to `true` existing settings on an index remain unchanged, the default is `false`
   * @param timeout Explicit operation timeout
   */
  def putSettings(
    body: Json.Obj,
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    flatSettings: Option[Boolean] = None,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty,
    masterTimeout: Option[String] = None,
    preserveExisting: Option[Boolean] = None,
    timeout: Option[String] = None
  ): ZIO[Any, FrameworkException, IndicesPutSettingsResponse] = {
    val request = IndicesPutSettingsRequest(
      body = body,
      allowNoIndices = allowNoIndices,
      expandWildcards = expandWildcards,
      flatSettings = flatSettings,
      ignoreUnavailable = ignoreUnavailable,
      indices = indices,
      masterTimeout = masterTimeout,
      preserveExisting = preserveExisting,
      timeout = timeout
    )

    putSettings(request)

  }

  def putSettings(
    request: IndicesPutSettingsRequest
  ): ZIO[Any, FrameworkException, IndicesPutSettingsResponse] =
    execute(request)

  /*
   * Creates or updates an index template.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-templates.html
   *
   * @param name The name of the template
   * @param body body the body of the call
   * @param create Whether the index template should only be added if new or can also replace an existing one
   * @param flatSettings Return settings in flat format (default: false)
   * @param includeTypeName Whether a type should be returned in the body of the mappings.
   * @param masterTimeout Specify timeout for connection to master
   * @param order The order for this template when merging multiple matching ones (higher numbers are merged later, overriding the lower numbers)
   * @param timeout Explicit operation timeout
   */
  def putTemplate(
    name: String,
    body: Json.Obj,
    create: Boolean = false,
    flatSettings: Option[Boolean] = None,
    includeTypeName: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    order: Option[Double] = None,
    timeout: Option[String] = None
  ): ZIO[Any, FrameworkException, IndicesPutTemplateResponse] = {
    val request = IndicesPutTemplateRequest(
      name = name,
      body = body,
      create = create,
      flatSettings = flatSettings,
      includeTypeName = includeTypeName,
      masterTimeout = masterTimeout,
      order = order,
      timeout = timeout
    )

    putTemplate(request)

    putTemplate(request)
  }

  def putTemplate(
    request: IndicesPutTemplateRequest
  ): ZIO[Any, FrameworkException, IndicesPutTemplateResponse] =
    execute(request)

  /*
   * Returns information about ongoing index shard recoveries.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-recovery.html
   *
   * @param activeOnly Display only those recoveries that are currently on-going
   * @param detailed Whether to display detailed information about shard recovery
   * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
   */
  def recovery(
    activeOnly: Boolean = false,
    detailed: Boolean = false,
    indices: Chunk[String] = Chunk.empty
  ): ZIO[Any, FrameworkException, IndicesRecoveryResponse] = {
    val request = IndicesRecoveryRequest(activeOnly = activeOnly, detailed = detailed, indices = indices)

    recovery(request)

  }

  def recovery(
    request: IndicesRecoveryRequest
  ): ZIO[Any, FrameworkException, IndicesRecoveryResponse] =
    execute(request)

  /*
   * Performs the refresh operation in one or more indices.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-refresh.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
   */
  def refresh(
    indices: Chunk[String],
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None
  ): ZIO[Any, FrameworkException, IndicesRefreshResponse] = {
    val request = IndicesRefreshRequest(
      allowNoIndices = allowNoIndices,
      expandWildcards = expandWildcards,
      ignoreUnavailable = ignoreUnavailable,
      indices = indices
    )

    refresh(request)

  }

  def refresh(request: IndicesRefreshRequest): ZIO[Any, FrameworkException, IndicesRefreshResponse] =
    execute(request)

  /*
   * Updates an alias to point to a new index when the existing index
  is considered to be too large or too old.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-rollover-index.html
   *
   * @param alias The name of the alias to rollover
   * @param body body the body of the call
   * @param dryRun If set to true the rollover action will only be validated but not actually performed even if a condition matches. The default is false
   * @param includeTypeName Whether a type should be included in the body of the mappings.
   * @param masterTimeout Specify timeout for connection to master
   * @param newIndex The name of the rollover index
   * @param timeout Explicit operation timeout
   * @param waitForActiveShards Set the number of active shards to wait for on the newly created rollover index before the operation returns.
   */
  def rollover(
    alias: String,
    body: Option[Json.Obj] = None,
    dryRun: Option[Boolean] = None,
    includeTypeName: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    newIndex: Option[String] = None,
    timeout: Option[String] = None,
    waitForActiveShards: Option[String] = None
  ): ZIO[Any, FrameworkException, IndicesRolloverResponse] = {
    val request = IndicesRolloverRequest(
      alias = alias,
      body = body,
      dryRun = dryRun,
      includeTypeName = includeTypeName,
      masterTimeout = masterTimeout,
      newIndex = newIndex,
      timeout = timeout,
      waitForActiveShards = waitForActiveShards
    )

    rollover(request)

  }

  def rollover(
    request: IndicesRolloverRequest
  ): ZIO[Any, FrameworkException, IndicesRolloverResponse] =
    execute(request)

  /*
   * Provides low-level information about segments in a Lucene index.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-segments.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
   * @param verbose Includes detailed memory usage by Lucene.
   */
  def segments(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty,
    verbose: Boolean = false
  ): ZIO[Any, FrameworkException, IndicesSegmentsResponse] = {
    val request = IndicesSegmentsRequest(
      allowNoIndices = allowNoIndices,
      expandWildcards = expandWildcards,
      ignoreUnavailable = ignoreUnavailable,
      indices = indices,
      verbose = verbose
    )

    segments(request)

  }

  def segments(request: IndicesSegmentsRequest): ZIO[Any, FrameworkException, IndicesSegmentsResponse] =
    execute(request)

  /*
   * Provides store information for shard copies of indices.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-shards-stores.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
   * @param status A comma-separated list of statuses used to filter on shards to get store information for
   */
  def shardStores(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty,
    status: Chunk[String] = Chunk.empty
  ): ZIO[Any, FrameworkException, IndicesShardStoresResponse] = {
    val request = IndicesShardStoresRequest(
      allowNoIndices = allowNoIndices,
      expandWildcards = expandWildcards,
      ignoreUnavailable = ignoreUnavailable,
      indices = indices,
      status = status
    )

    shardStores(request)

  }

  def shardStores(request: IndicesShardStoresRequest): ZIO[Any, FrameworkException, IndicesShardStoresResponse] =
    execute(request)

  /*
   * Allow to shrink an existing index into a new index with fewer primary shards.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-shrink-index.html
   *
   * @param index The name of the source index to shrink
   * @param target The name of the target index to shrink into
   * @param body body the body of the call
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Explicit operation timeout
   * @param waitForActiveShards Set the number of active shards to wait for on the shrunken index before the operation returns.
   */
  def shrink(
    index: String,
    target: String,
    body: Option[Json.Obj] = None,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None,
    waitForActiveShards: Option[String] = None
  ): ZIO[Any, FrameworkException, IndicesShrinkResponse] = {
    val request = IndicesShrinkRequest(
      index = index,
      target = target,
      body = body,
      masterTimeout = masterTimeout,
      timeout = timeout,
      waitForActiveShards = waitForActiveShards
    )

    shrink(request)

  }

  def shrink(request: IndicesShrinkRequest): ZIO[Any, FrameworkException, IndicesShrinkResponse] =
    execute(request)

  /*
   * Allows you to split an existing index into a new index with more primary shards.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-split-index.html
   *
   * @param index The name of the source index to split
   * @param target The name of the target index to split into
   * @param body body the body of the call
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Explicit operation timeout
   * @param waitForActiveShards Set the number of active shards to wait for on the shrunken index before the operation returns.
   */
  def split(
    index: String,
    target: String,
    body: Option[Json.Obj] = None,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None,
    waitForActiveShards: Option[String] = None
  ): ZIO[Any, FrameworkException, IndicesSplitResponse] = {
    val request = IndicesSplitRequest(
      index = index,
      target = target,
      body = body,
      masterTimeout = masterTimeout,
      timeout = timeout,
      waitForActiveShards = waitForActiveShards
    )

    split(request)

  }

  def split(request: IndicesSplitRequest): ZIO[Any, FrameworkException, IndicesSplitResponse] =
    execute(request)

  /*
   * Provides statistics on operations happening in an index.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-stats.html
   *
   * @param completionFields A comma-separated list of fields for `fielddata` and `suggest` index metric (supports wildcards)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param fielddataFields A comma-separated list of fields for `fielddata` index metric (supports wildcards)
   * @param fields A comma-separated list of fields for `fielddata` and `completion` index metric (supports wildcards)
   * @param forbidClosedIndices If set to false stats will also collected from closed indices if explicitly specified or if expand_wildcards expands to closed indices
   * @param groups A comma-separated list of search groups for `search` index metric
   * @param includeSegmentFileSizes Whether to report the aggregated disk usage of each one of the Lucene index files (only applies if segment stats are requested)
   * @param includeUnloadedSegments If set to true segment stats will include stats for segments that are not currently loaded into memory
   * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
   * @param level Return stats aggregated at cluster, index or shard level
   * @param metric Limit the information returned the specific metrics.
   * @param types A comma-separated list of document types for the `indexing` index metric
   */
  def stats(
    indices: Chunk[String] = Chunk.empty,
    completionFields: Chunk[String] = Chunk.empty,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    fielddataFields: Chunk[String] = Chunk.empty,
    fields: Chunk[String] = Chunk.empty,
    forbidClosedIndices: Boolean = true,
    groups: Chunk[String] = Chunk.empty,
    includeSegmentFileSizes: Boolean = false,
    includeUnloadedSegments: Boolean = false,
    level: Level = Level.indices,
    metric: Option[String] = None,
    types: Chunk[String] = Chunk.empty
  ): ZIO[Any, FrameworkException, IndicesStatsResponse] = {
    val request = IndicesStatsRequest(
      completionFields = completionFields,
      expandWildcards = expandWildcards,
      fielddataFields = fielddataFields,
      fields = fields,
      forbidClosedIndices = forbidClosedIndices,
      groups = groups,
      includeSegmentFileSizes = includeSegmentFileSizes,
      includeUnloadedSegments = includeUnloadedSegments,
      indices = indices,
      level = level,
      metric = metric,
      types = types
    )

    stats(request)

  }

  def stats(request: IndicesStatsRequest): ZIO[Any, FrameworkException, IndicesStatsResponse] =
    execute(request)

  /*
   * Updates index aliases.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-aliases.html
   *
   * @param body body the body of the call
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Request timeout
   */
  def updateAliases(
    body: Json.Obj,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None
  ): ZIO[Any, FrameworkException, IndicesUpdateAliasesResponse] = {
    val request = IndicesUpdateAliasesRequest(body = body, masterTimeout = masterTimeout, timeout = timeout)

    updateAliases(request)

  }

  def updateAliases(request: IndicesUpdateAliasesRequest): ZIO[Any, FrameworkException, IndicesUpdateAliasesResponse] =
    execute(request)

  /*
   * The _upgrade API is no longer useful and will be removed.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-upgrade.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
   * @param onlyAncientSegments If true, only ancient (an older Lucene major release) segments will be upgraded
   * @param waitForCompletion Specify whether the request should block until the all segments are upgraded (default: false)
   */
  def upgrade(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty,
    onlyAncientSegments: Option[Boolean] = None,
    waitForCompletion: Option[Boolean] = None
  ): ZIO[Any, FrameworkException, IndicesUpgradeResponse] = {
    val request = IndicesUpgradeRequest(
      allowNoIndices = allowNoIndices,
      expandWildcards = expandWildcards,
      ignoreUnavailable = ignoreUnavailable,
      indices = indices,
      onlyAncientSegments = onlyAncientSegments,
      waitForCompletion = waitForCompletion
    )

    upgrade(request)

  }

  def upgrade(request: IndicesUpgradeRequest): ZIO[Any, FrameworkException, IndicesUpgradeResponse] =
    execute(request)

  /*
   * Allows a user to validate a potentially expensive query without executing it.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/search-validate.html
   *
   * @param allShards Execute validation on all shards instead of one random shard per index
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param analyzeWildcard Specify whether wildcard and prefix queries should be analyzed (default: false)
   * @param analyzer The analyzer to use for the query string
   * @param body body the body of the call
   * @param defaultOperator The default operator for query string query (AND or OR)
   * @param df The field to use as default where no field prefix is given in the query string
   * @param docTypes A comma-separated list of document types to restrict the operation; leave empty to perform the operation on all types
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param explain Return detailed information about the error
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names to restrict the operation; use `_all` or empty string to perform the operation on all indices
   * @param lenient Specify whether format-based query failures (such as providing text to a numeric field) should be ignored
   * @param q Query in the Lucene query string syntax
   * @param rewrite Provide a more detailed explanation showing the actual Lucene query that will be executed.
   */
  def validateQuery(
    body: Json.Obj,
    allShards: Option[Boolean] = None,
    allowNoIndices: Option[Boolean] = None,
    analyzeWildcard: Option[Boolean] = None,
    analyzer: Option[String] = None,
    defaultOperator: DefaultOperator = DefaultOperator.OR,
    df: Option[String] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    explain: Option[Boolean] = None,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty,
    lenient: Option[Boolean] = None,
    q: Option[String] = None,
    rewrite: Option[Boolean] = None
  ): ZIO[Any, FrameworkException, IndicesValidateQueryResponse] = {
    val request = IndicesValidateQueryRequest(
      allShards = allShards,
      allowNoIndices = allowNoIndices,
      analyzeWildcard = analyzeWildcard,
      analyzer = analyzer,
      body = body,
      defaultOperator = defaultOperator,
      df = df,
      expandWildcards = expandWildcards,
      explain = explain,
      ignoreUnavailable = ignoreUnavailable,
      indices = indices,
      lenient = lenient,
      q = q,
      rewrite = rewrite
    )

    validateQuery(request)
  }

  def validateQuery(request: IndicesValidateQueryRequest): ZIO[Any, FrameworkException, IndicesValidateQueryResponse] =
    execute(request)

  def createWithSettingsAndMappings(
    index: String,
    settings: Settings = Settings(),
    mappings: Option[RootDocumentMapping] = None
  ): ZIO[Any, FrameworkException, IndicesCreateResponse] = {
    /*
          Creates an index with optional settings.
          :ref:`qdb-guide-reference-api-admin-indices-create-index`

          :param index: the name of the index
          :keyword settings: a settings object or a dict containing settings
     */

    val request = IndicesCreateRequest(
      index,
      body = JsonUtils
        .cleanValue(
          Json.Obj("settings" -> settings.asJson, "mappings" -> mappings.map(_.asJson).getOrElse(Json.Obj()))
        )
        .asInstanceOf[Json.Obj]
    )

    execute(request)
  }

  def putMapping(
    indices: Chunk[String],
    mapping: RootDocumentMapping
  ): ZIO[Any, FrameworkException, IndicesPutMappingResponse] =
    putMapping(
      indices = indices,
      body = JsonUtils.joClean(mapping.toJsonAST.toOption.get).asInstanceOf[Json.Obj]
    )
}

object IndicesService {

  // services
  private case class Live(
    client: OpenSearchService,
    httpService: OpenSearchHttpService
  ) extends IndicesService

  val live: ZLayer[OpenSearchService, Nothing, IndicesService] =
    ZLayer {
      for { baseOpenSearchService <- ZIO.service[OpenSearchService] } yield Live(
        baseOpenSearchService,
        baseOpenSearchService.httpService
      )
    }

  // access methods

  def createWithSettingsAndMappings(
    index: String,
    settings: Settings = Settings(),
    mappings: Option[RootDocumentMapping] = None
  ): ZIO[IndicesService, FrameworkException, IndicesCreateResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.createWithSettingsAndMappings(index, settings, mappings))

  /*
   * Performs the analysis process on a text and return the tokens breakdown of the text.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-analyze.html
   *
   * @param body body the body of the call
   * @param index The name of the index to scope the operation
   */
  def analyze(
    body: Json.Obj,
    index: Option[String] = None
  ): ZIO[IndicesService, FrameworkException, IndicesAnalyzeResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.analyze(body = body, index = index))

  def analyze(request: IndicesAnalyzeRequest): ZIO[IndicesService, FrameworkException, IndicesAnalyzeResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Clears all or specific caches for one or more indices.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-clearcache.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param fielddata Clear field data
   * @param fields A comma-separated list of fields to clear when using the `fielddata` parameter (default: all)
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param index A comma-separated list of index name to limit the operation
   * @param indices A comma-separated list of index name to limit the operation
   * @param query Clear query caches
   * @param request Clear request cache
   */
  def clearCache(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    fielddata: Option[Boolean] = None,
    fields: Chunk[String] = Chunk.empty,
    ignoreUnavailable: Option[Boolean] = None,
    index: Chunk[String] = Chunk.empty,
    indices: Chunk[String] = Chunk.empty,
    query: Option[Boolean] = None,
    request: Option[Boolean] = None
  ): ZIO[IndicesService, FrameworkException, IndicesClearCacheResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.clearCache(
        allowNoIndices = allowNoIndices,
        expandWildcards = expandWildcards,
        fielddata = fielddata,
        fields = fields,
        ignoreUnavailable = ignoreUnavailable,
        index = index,
        indices = indices,
        query = query,
        request = request
      )
    )

  def clearCache(
    request: IndicesClearCacheRequest
  ): ZIO[IndicesService, FrameworkException, IndicesClearCacheResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Clones an index
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-clone-index.html
   *
   * @param index The name of the source index to clone
   * @param target The name of the target index to clone into
   * @param body body the body of the call
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Explicit operation timeout
   * @param waitForActiveShards Set the number of active shards to wait for on the cloned index before the operation returns.
   */
  def clone(
    index: String,
    target: String,
    body: Option[Json.Obj] = None,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None,
    waitForActiveShards: Option[String] = None
  ): ZIO[IndicesService, FrameworkException, IndicesCloneResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.clone(
        index = index,
        target = target,
        body = body,
        masterTimeout = masterTimeout,
        timeout = timeout,
        waitForActiveShards = waitForActiveShards
      )
    )

  def clone(request: IndicesCloneRequest): ZIO[IndicesService, FrameworkException, IndicesCloneResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Closes an index.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-open-close.html
   *
   * @param index A comma separated list of indices to close
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Explicit operation timeout
   * @param waitForActiveShards Sets the number of active shards to wait for before the operation returns.
   */
  def close(
    index: String,
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None,
    waitForActiveShards: Option[String] = None
  ): ZIO[IndicesService, FrameworkException, IndicesCloseResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.close(
        index = index,
        allowNoIndices = allowNoIndices,
        expandWildcards = expandWildcards,
        ignoreUnavailable = ignoreUnavailable,
        masterTimeout = masterTimeout,
        timeout = timeout,
        waitForActiveShards = waitForActiveShards
      )
    )

  def close(request: IndicesCloseRequest): ZIO[IndicesService, FrameworkException, IndicesCloseResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Creates an index with optional settings and mappings.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-create-index.html
   *
   * @param index The name of the index
   * @param body body the body of the call
   * @param includeTypeName Whether a type should be expected in the body of the mappings.
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Explicit operation timeout
   * @param waitForActiveShards Set the number of active shards to wait for before the operation returns.
   */
  def create(
    index: String,
    body: Json.Obj,
    includeTypeName: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None,
    waitForActiveShards: Option[Int] = None
  ): ZIO[IndicesService, FrameworkException, IndicesCreateResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.create(
        index = index,
        body = body,
        includeTypeName = includeTypeName,
        masterTimeout = masterTimeout,
        timeout = timeout,
        waitForActiveShards = waitForActiveShards
      )
    )

  def create(request: IndicesCreateRequest): ZIO[IndicesService, FrameworkException, IndicesCreateResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Deletes an index.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-delete-index.html
   *
   * @param indices A comma-separated list of indices to delete; use `_all` or `*` string to delete all indices
   * @param allowNoIndices Ignore if a wildcard expression resolves to no concrete indices (default: false)
   * @param expandWildcards Whether wildcard expressions should get expanded to open or closed indices (default: open)
   * @param ignoreUnavailable Ignore unavailable indexes (default: false)
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Explicit operation timeout
   */
  def delete(
    indices: Chunk[String] = Chunk.empty,
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None
  ): ZIO[IndicesService, FrameworkException, IndicesDeleteResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.delete(
        indices = indices,
        allowNoIndices = allowNoIndices,
        expandWildcards = expandWildcards,
        ignoreUnavailable = ignoreUnavailable,
        masterTimeout = masterTimeout,
        timeout = timeout
      )
    )

  def delete(request: IndicesDeleteRequest): ZIO[IndicesService, FrameworkException, IndicesDeleteResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Deletes an alias.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-aliases.html
   *
   * @param indices A comma-separated list of index names (supports wildcards); use `_all` for all indices
   * @param name A comma-separated list of aliases to delete (supports wildcards); use `_all` to delete all aliases for the specified indices.
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Explicit timestamp for the document
   */
  def deleteAlias(
    indices: Chunk[String] = Chunk.empty,
    names: Chunk[String] = Chunk.empty,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None
  ): ZIO[IndicesService, FrameworkException, IndicesDeleteAliasResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.deleteAlias(indices = indices, names = names, masterTimeout = masterTimeout, timeout = timeout)
    )

  def deleteAlias(
    request: IndicesDeleteAliasRequest
  ): ZIO[IndicesService, FrameworkException, IndicesDeleteAliasResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Deletes an index template.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-templates.html
   *
   * @param name The name of the template
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Explicit operation timeout
   */
  def deleteTemplate(
    name: String,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None
  ): ZIO[IndicesService, FrameworkException, IndicesDeleteTemplateResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.deleteTemplate(name = name, masterTimeout = masterTimeout, timeout = timeout)
    )

  def deleteTemplate(
    request: IndicesDeleteTemplateRequest
  ): ZIO[IndicesService, FrameworkException, IndicesDeleteTemplateResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Returns information about whether a particular index exists.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-exists.html
   *
   * @param indices A comma-separated list of index names
   * @param allowNoIndices Ignore if a wildcard expression resolves to no concrete indices (default: false)
   * @param expandWildcards Whether wildcard expressions should get expanded to open or closed indices (default: open)
   * @param flatSettings Return settings in flat format (default: false)
   * @param ignoreUnavailable Ignore unavailable indexes (default: false)
   * @param includeDefaults Whether to return all default setting for each of the indices.
   * @param local Return local information, do not retrieve the state from master node (default: false)
   */
  def exists(
    indices: Chunk[String] = Chunk.empty,
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    flatSettings: Option[Boolean] = None,
    ignoreUnavailable: Option[Boolean] = None,
    includeDefaults: Boolean = false,
    local: Option[Boolean] = None
  ): ZIO[IndicesService, FrameworkException, IndicesExistsResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.exists(
        indices = indices,
        allowNoIndices = allowNoIndices,
        expandWildcards = expandWildcards,
        flatSettings = flatSettings,
        ignoreUnavailable = ignoreUnavailable,
        includeDefaults = includeDefaults,
        local = local
      )
    )

  def exists(request: IndicesExistsRequest): ZIO[IndicesService, FrameworkException, IndicesExistsResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Returns information about whether a particular alias exists.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-aliases.html
   *
   * @param name A comma-separated list of alias names to return
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names to filter aliases
   * @param local Return local information, do not retrieve the state from master node (default: false)
   */
  def existsAlias(
    name: Chunk[String] = Chunk.empty,
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty,
    local: Option[Boolean] = None
  ): ZIO[IndicesService, FrameworkException, IndicesExistsAliasResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.existsAlias(
        name = name,
        allowNoIndices = allowNoIndices,
        expandWildcards = expandWildcards,
        ignoreUnavailable = ignoreUnavailable,
        indices = indices,
        local = local
      )
    )

  def existsAlias(
    request: IndicesExistsAliasRequest
  ): ZIO[IndicesService, FrameworkException, IndicesExistsAliasResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Returns information about whether a particular index template exists.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-templates.html
   *
   * @param name The comma separated names of the index templates
   * @param flatSettings Return settings in flat format (default: false)
   * @param local Return local information, do not retrieve the state from master node (default: false)
   * @param masterTimeout Explicit operation timeout for connection to master node
   */
  def existsTemplate(
    name: String,
    flatSettings: Option[Boolean] = None,
    local: Option[Boolean] = None,
    masterTimeout: Option[String] = None
  ): ZIO[IndicesService, FrameworkException, Boolean] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.existsTemplate(name = name, flatSettings = flatSettings, local = local, masterTimeout = masterTimeout)
    )

  def existsTemplate(
    request: IndicesExistsTemplateRequest
  ): ZIO[IndicesService, FrameworkException, IndicesExistsTemplateResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Performs the flush operation on one or more indices.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-flush.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param force Whether a flush should be forced even if it is not necessarily needed ie. if no changes will be committed to the index. This is useful if transaction log IDs should be incremented even if no uncommitted changes are present. (This setting can be considered as internal)
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names; use `_all` or empty string for all indices
   * @param waitIfOngoing If set to true the flush operation will block until the flush can be executed if another flush operation is already executing. The default is true. If set to false the flush will be skipped iff if another flush operation is already running.
   */
  def flush(
    indices: Chunk[String] = Chunk.empty,
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    force: Option[Boolean] = None,
    ignoreUnavailable: Option[Boolean] = None,
    waitIfOngoing: Option[Boolean] = None
  ): ZIO[IndicesService, FrameworkException, IndicesFlushResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.flush(
        allowNoIndices = allowNoIndices,
        expandWildcards = expandWildcards,
        force = force,
        ignoreUnavailable = ignoreUnavailable,
        indices = indices,
        waitIfOngoing = waitIfOngoing
      )
    )

  def flush(index: String): ZIO[IndicesService, FrameworkException, IndicesFlushResponse] =
    flush(Seq(index))

  def flush(request: IndicesFlushRequest): ZIO[IndicesService, FrameworkException, IndicesFlushResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Performs a synced flush operation on one or more indices.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-synced-flush-api.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names; use `_all` or empty string for all indices
   */
  def flushSynced(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty
  ): ZIO[IndicesService, FrameworkException, IndicesFlushSyncedResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.flushSynced(
        allowNoIndices = allowNoIndices,
        expandWildcards = expandWildcards,
        ignoreUnavailable = ignoreUnavailable,
        indices = indices
      )
    )

  def flushSynced(
    request: IndicesFlushSyncedRequest
  ): ZIO[IndicesService, FrameworkException, IndicesFlushSyncedResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Performs the force merge operation on one or more indices.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-forcemerge.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param flush Specify whether the index should be flushed after performing the operation (default: true)
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
   * @param maxNumSegments The number of segments the index should be merged into (default: dynamic)
   * @param onlyExpungeDeletes Specify whether the operation should only expunge deleted documents
   */
  def forcemerge(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    flush: Option[Boolean] = None,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty,
    maxNumSegments: Option[Double] = None,
    onlyExpungeDeletes: Option[Boolean] = None
  ): ZIO[IndicesService, FrameworkException, IndicesForcemergeResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.forcemerge(
        allowNoIndices = allowNoIndices,
        expandWildcards = expandWildcards,
        flush = flush,
        ignoreUnavailable = ignoreUnavailable,
        indices = indices,
        maxNumSegments = maxNumSegments,
        onlyExpungeDeletes = onlyExpungeDeletes
      )
    )

  def forcemerge(
    request: IndicesForcemergeRequest
  ): ZIO[IndicesService, FrameworkException, IndicesForcemergeResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Returns information about one or more indices.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-get-index.html
   *
   * @param indices A comma-separated list of index names
   * @param allowNoIndices Ignore if a wildcard expression resolves to no concrete indices (default: false)
   * @param expandWildcards Whether wildcard expressions should get expanded to open or closed indices (default: open)
   * @param flatSettings Return settings in flat format (default: false)
   * @param ignoreUnavailable Ignore unavailable indexes (default: false)
   * @param includeDefaults Whether to return all default setting for each of the indices.
   * @param includeTypeName Whether to add the type name to the response (default: false)
   * @param local Return local information, do not retrieve the state from master node (default: false)
   * @param masterTimeout Specify timeout for connection to master
   */
  def get(
    indices: Chunk[String] = Chunk.empty,
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    flatSettings: Option[Boolean] = None,
    ignoreUnavailable: Option[Boolean] = None,
    includeDefaults: Boolean = false,
    includeTypeName: Option[Boolean] = None,
    local: Option[Boolean] = None,
    masterTimeout: Option[String] = None
  ): ZIO[IndicesService, FrameworkException, IndicesGetResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.get(
        indices = indices,
        allowNoIndices = allowNoIndices,
        expandWildcards = expandWildcards,
        flatSettings = flatSettings,
        ignoreUnavailable = ignoreUnavailable,
        includeDefaults = includeDefaults,
        includeTypeName = includeTypeName,
        local = local,
        masterTimeout = masterTimeout
      )
    )

  def get(request: IndicesGetRequest): ZIO[IndicesService, FrameworkException, IndicesGetResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Returns an alias.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-aliases.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names to filter aliases
   * @param local Return local information, do not retrieve the state from master node (default: false)
   * @param name A comma-separated list of alias names to return
   */
  def getAlias(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty,
    local: Option[Boolean] = None,
    name: Chunk[String] = Chunk.empty
  ): ZIO[IndicesService, FrameworkException, IndicesGetAliasResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.getAlias(
        allowNoIndices = allowNoIndices,
        expandWildcards = expandWildcards,
        ignoreUnavailable = ignoreUnavailable,
        indices = indices,
        local = local,
        name = name
      )
    )

  def getAlias(request: IndicesGetAliasRequest): ZIO[IndicesService, FrameworkException, IndicesGetAliasResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Returns mapping for one or more fields.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-get-field-mapping.html
   *
   * @param fields A comma-separated list of fields
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param includeDefaults Whether the default mapping values should be returned as well
   * @param includeTypeName Whether a type should be returned in the body of the mappings.
   * @param indices A comma-separated list of index names
   * @param local Return local information, do not retrieve the state from master node (default: false)
   */
  def getFieldMapping(
    fields: Chunk[String] = Chunk.empty,
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    includeDefaults: Option[Boolean] = None,
    includeTypeName: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty,
    local: Option[Boolean] = None
  ): ZIO[IndicesService, FrameworkException, IndicesGetFieldMappingResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.getFieldMapping(
        fields = fields,
        allowNoIndices = allowNoIndices,
        expandWildcards = expandWildcards,
        ignoreUnavailable = ignoreUnavailable,
        includeDefaults = includeDefaults,
        includeTypeName = includeTypeName,
        indices = indices,
        local = local
      )
    )

  def getFieldMapping(
    request: IndicesGetFieldMappingRequest
  ): ZIO[IndicesService, FrameworkException, IndicesGetFieldMappingResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Returns mappings for one or more indices.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-get-mapping.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names
   * @param local Return local information, do not retrieve the state from master node (default: false)
   * @param masterTimeout Specify timeout for connection to master
   */
  def getMapping(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty,
    local: Option[Boolean] = None,
    masterTimeout: Option[String] = None
  ): ZIO[IndicesService, FrameworkException, IndicesGetMappingResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.getMapping(
        allowNoIndices = allowNoIndices,
        expandWildcards = expandWildcards,
        ignoreUnavailable = ignoreUnavailable,
        indices = indices,
        local = local,
        masterTimeout = masterTimeout
      )
    )

  def getMapping(
    request: IndicesGetMappingRequest
  ): ZIO[IndicesService, FrameworkException, IndicesGetMappingResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Returns settings for one or more indices.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-get-settings.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param flatSettings Return settings in flat format (default: false)
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param includeDefaults Whether to return all default setting for each of the indices.
   * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
   * @param local Return local information, do not retrieve the state from master node (default: false)
   * @param masterTimeout Specify timeout for connection to master
   * @param name The name of the settings that should be included
   */
  def getSettings(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    flatSettings: Option[Boolean] = None,
    ignoreUnavailable: Option[Boolean] = None,
    includeDefaults: Boolean = false,
    indices: Chunk[String] = Chunk.empty,
    local: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    name: Option[String] = None
  ): ZIO[IndicesService, FrameworkException, IndicesGetSettingsResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.getSettings(
        allowNoIndices = allowNoIndices,
        expandWildcards = expandWildcards,
        flatSettings = flatSettings,
        ignoreUnavailable = ignoreUnavailable,
        includeDefaults = includeDefaults,
        indices = indices,
        local = local,
        masterTimeout = masterTimeout,
        name = name
      )
    )

  def getSettings(
    request: IndicesGetSettingsRequest
  ): ZIO[IndicesService, FrameworkException, IndicesGetSettingsResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Returns an index template.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-templates.html
   *
   * @param flatSettings Return settings in flat format (default: false)
   * @param includeTypeName Whether a type should be returned in the body of the mappings.
   * @param local Return local information, do not retrieve the state from master node (default: false)
   * @param masterTimeout Explicit operation timeout for connection to master node
   * @param name The comma separated names of the index templates
   */
  def getTemplate(
    flatSettings: Option[Boolean] = None,
    includeTypeName: Option[Boolean] = None,
    local: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    name: Option[String] = None
  ): ZIO[IndicesService, FrameworkException, IndicesGetTemplateResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.getTemplate(
        flatSettings = flatSettings,
        includeTypeName = includeTypeName,
        local = local,
        masterTimeout = masterTimeout,
        name = name
      )
    )

  def getTemplate(
    request: IndicesGetTemplateRequest
  ): ZIO[IndicesService, FrameworkException, IndicesGetTemplateResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * The _upgrade API is no longer useful and will be removed.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-upgrade.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
   */
  def getUpgrade(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty
  ): ZIO[IndicesService, FrameworkException, IndicesGetUpgradeResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.getUpgrade(
        allowNoIndices = allowNoIndices,
        expandWildcards = expandWildcards,
        ignoreUnavailable = ignoreUnavailable,
        indices = indices
      )
    )

  def getUpgrade(
    request: IndicesGetUpgradeRequest
  ): ZIO[IndicesService, FrameworkException, IndicesGetUpgradeResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Opens an index.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-open-close.html
   *
   * @param index A comma separated list of indices to open
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Explicit operation timeout
   * @param waitForActiveShards Sets the number of active shards to wait for before the operation returns.
   */
  def open(
    indices: Chunk[String],
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None,
    waitForActiveShards: Option[String] = None
  ): ZIO[IndicesService, FrameworkException, IndicesOpenResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.open(
        indices = indices,
        allowNoIndices = allowNoIndices,
        expandWildcards = expandWildcards,
        ignoreUnavailable = ignoreUnavailable,
        masterTimeout = masterTimeout,
        timeout = timeout,
        waitForActiveShards = waitForActiveShards
      )
    )

  def open(request: IndicesOpenRequest): ZIO[IndicesService, FrameworkException, IndicesOpenResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Creates or updates an alias.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-aliases.html
   *
   * @param indices A comma-separated list of index names the alias should point to (supports wildcards); use `_all` to perform the operation on all indices.
   * @param name The name of the alias to be created or updated
   * @param body body the body of the call
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Explicit timestamp for the document
   */
  def putAlias(
    indices: Chunk[String] = Chunk.empty,
    name: String,
    body: Json.Obj,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None
  ): ZIO[IndicesService, FrameworkException, IndicesPutAliasResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.putAlias(indices = indices, name = name, body = body, masterTimeout = masterTimeout, timeout = timeout)
    )

  def putAlias(request: IndicesPutAliasRequest): ZIO[IndicesService, FrameworkException, IndicesPutAliasResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Updates the index mappings.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-put-mapping.html
   *
   * @param indices A comma-separated list of index names the mapping should be added to (supports wildcards); use `_all` or omit to add the mapping on all indices.
   * @param body body the body of the call
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Explicit operation timeout
   */
  def putMapping(
    indices: Chunk[String] = Chunk.empty,
    body: Json.Obj,
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None
  ): ZIO[IndicesService, FrameworkException, IndicesPutMappingResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.putMapping(
        indices = indices,
        body = body,
        allowNoIndices = allowNoIndices,
        expandWildcards = expandWildcards,
        ignoreUnavailable = ignoreUnavailable,
        masterTimeout = masterTimeout,
        timeout = timeout
      )
    )

  def putMapping(
    request: IndicesPutMappingRequest
  ): ZIO[IndicesService, FrameworkException, IndicesPutMappingResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Updates the index settings.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-update-settings.html
   *
   * @param body body the body of the call
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param flatSettings Return settings in flat format (default: false)
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
   * @param masterTimeout Specify timeout for connection to master
   * @param preserveExisting Whether to update existing settings. If set to `true` existing settings on an index remain unchanged, the default is `false`
   * @param timeout Explicit operation timeout
   */
  def putSettings(
    body: Json.Obj,
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    flatSettings: Option[Boolean] = None,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty,
    masterTimeout: Option[String] = None,
    preserveExisting: Option[Boolean] = None,
    timeout: Option[String] = None
  ): ZIO[IndicesService, FrameworkException, IndicesPutSettingsResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.putSettings(
        body = body,
        allowNoIndices = allowNoIndices,
        expandWildcards = expandWildcards,
        flatSettings = flatSettings,
        ignoreUnavailable = ignoreUnavailable,
        indices = indices,
        masterTimeout = masterTimeout,
        preserveExisting = preserveExisting,
        timeout = timeout
      )
    )

  def putSettings(
    request: IndicesPutSettingsRequest
  ): ZIO[IndicesService, FrameworkException, IndicesPutSettingsResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Creates or updates an index template.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-templates.html
   *
   * @param name The name of the template
   * @param body body the body of the call
   * @param create Whether the index template should only be added if new or can also replace an existing one
   * @param flatSettings Return settings in flat format (default: false)
   * @param includeTypeName Whether a type should be returned in the body of the mappings.
   * @param masterTimeout Specify timeout for connection to master
   * @param order The order for this template when merging multiple matching ones (higher numbers are merged later, overriding the lower numbers)
   * @param timeout Explicit operation timeout
   */
  def putTemplate(
    name: String,
    body: Json.Obj,
    create: Boolean = false,
    flatSettings: Option[Boolean] = None,
    includeTypeName: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    order: Option[Double] = None,
    timeout: Option[String] = None
  ): ZIO[IndicesService, FrameworkException, IndicesPutTemplateResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.putTemplate(
        name = name,
        body = body,
        create = create,
        flatSettings = flatSettings,
        includeTypeName = includeTypeName,
        masterTimeout = masterTimeout,
        order = order,
        timeout = timeout
      )
    )

  def putTemplate(
    request: IndicesPutTemplateRequest
  ): ZIO[IndicesService, FrameworkException, IndicesPutTemplateResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Returns information about ongoing index shard recoveries.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-recovery.html
   *
   * @param activeOnly Display only those recoveries that are currently on-going
   * @param detailed Whether to display detailed information about shard recovery
   * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
   */
  def recovery(
    activeOnly: Boolean = false,
    detailed: Boolean = false,
    indices: Chunk[String] = Chunk.empty
  ): ZIO[IndicesService, FrameworkException, IndicesRecoveryResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.recovery(activeOnly = activeOnly, detailed = detailed, indices = indices)
    )

  def recovery(request: IndicesRecoveryRequest): ZIO[IndicesService, FrameworkException, IndicesRecoveryResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Performs the refresh operation in one or more indices.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-refresh.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
   */
  def refresh(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty
  ): ZIO[IndicesService, FrameworkException, IndicesRefreshResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.refresh(
        allowNoIndices = allowNoIndices,
        expandWildcards = expandWildcards,
        ignoreUnavailable = ignoreUnavailable,
        indices = indices
      )
    )

  def refresh(index: String): ZIO[IndicesService, FrameworkException, IndicesRefreshResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.refresh(
        indices = Seq(index)
      )
    )

  def refresh(request: IndicesRefreshRequest): ZIO[IndicesService, FrameworkException, IndicesRefreshResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Updates an alias to point to a new index when the existing index
is considered to be too large or too old.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-rollover-index.html
   *
   * @param alias The name of the alias to rollover
   * @param body body the body of the call
   * @param dryRun If set to true the rollover action will only be validated but not actually performed even if a condition matches. The default is false
   * @param includeTypeName Whether a type should be included in the body of the mappings.
   * @param masterTimeout Specify timeout for connection to master
   * @param newIndex The name of the rollover index
   * @param timeout Explicit operation timeout
   * @param waitForActiveShards Set the number of active shards to wait for on the newly created rollover index before the operation returns.
   */
  def rollover(
    alias: String,
    body: Option[Json.Obj] = None,
    dryRun: Option[Boolean] = None,
    includeTypeName: Option[Boolean] = None,
    masterTimeout: Option[String] = None,
    newIndex: Option[String] = None,
    timeout: Option[String] = None,
    waitForActiveShards: Option[String] = None
  ): ZIO[IndicesService, FrameworkException, IndicesRolloverResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.rollover(
        alias = alias,
        body = body,
        dryRun = dryRun,
        includeTypeName = includeTypeName,
        masterTimeout = masterTimeout,
        newIndex = newIndex,
        timeout = timeout,
        waitForActiveShards = waitForActiveShards
      )
    )

  def rollover(request: IndicesRolloverRequest): ZIO[IndicesService, FrameworkException, IndicesRolloverResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Provides low-level information about segments in a Lucene index.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-segments.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
   * @param verbose Includes detailed memory usage by Lucene.
   */
  def segments(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty,
    verbose: Boolean = false
  ): ZIO[IndicesService, FrameworkException, IndicesSegmentsResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.segments(
        allowNoIndices = allowNoIndices,
        expandWildcards = expandWildcards,
        ignoreUnavailable = ignoreUnavailable,
        indices = indices,
        verbose = verbose
      )
    )

  def segments(request: IndicesSegmentsRequest): ZIO[IndicesService, FrameworkException, IndicesSegmentsResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Provides store information for shard copies of indices.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-shards-stores.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
   * @param status A comma-separated list of statuses used to filter on shards to get store information for
   */
  def shardStores(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty,
    status: Chunk[String] = Chunk.empty
  ): ZIO[IndicesService, FrameworkException, IndicesShardStoresResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.shardStores(
        allowNoIndices = allowNoIndices,
        expandWildcards = expandWildcards,
        ignoreUnavailable = ignoreUnavailable,
        indices = indices,
        status = status
      )
    )

  def shardStores(
    request: IndicesShardStoresRequest
  ): ZIO[IndicesService, FrameworkException, IndicesShardStoresResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Allow to shrink an existing index into a new index with fewer primary shards.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-shrink-index.html
   *
   * @param index The name of the source index to shrink
   * @param target The name of the target index to shrink into
   * @param body body the body of the call
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Explicit operation timeout
   * @param waitForActiveShards Set the number of active shards to wait for on the shrunken index before the operation returns.
   */
  def shrink(
    index: String,
    target: String,
    body: Option[Json.Obj] = None,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None,
    waitForActiveShards: Option[String] = None
  ): ZIO[IndicesService, FrameworkException, IndicesShrinkResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.shrink(
        index = index,
        target = target,
        body = body,
        masterTimeout = masterTimeout,
        timeout = timeout,
        waitForActiveShards = waitForActiveShards
      )
    )

  def shrink(request: IndicesShrinkRequest): ZIO[IndicesService, FrameworkException, IndicesShrinkResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Allows you to split an existing index into a new index with more primary shards.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-split-index.html
   *
   * @param index The name of the source index to split
   * @param target The name of the target index to split into
   * @param body body the body of the call
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Explicit operation timeout
   * @param waitForActiveShards Set the number of active shards to wait for on the shrunken index before the operation returns.
   */
  def split(
    index: String,
    target: String,
    body: Option[Json.Obj] = None,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None,
    waitForActiveShards: Option[String] = None
  ): ZIO[IndicesService, FrameworkException, IndicesSplitResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.split(
        index = index,
        target = target,
        body = body,
        masterTimeout = masterTimeout,
        timeout = timeout,
        waitForActiveShards = waitForActiveShards
      )
    )

  def split(request: IndicesSplitRequest): ZIO[IndicesService, FrameworkException, IndicesSplitResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Provides statistics on operations happening in an index.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-stats.html
   *
   * @param completionFields A comma-separated list of fields for `fielddata` and `suggest` index metric (supports wildcards)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param fielddataFields A comma-separated list of fields for `fielddata` index metric (supports wildcards)
   * @param fields A comma-separated list of fields for `fielddata` and `completion` index metric (supports wildcards)
   * @param forbidClosedIndices If set to false stats will also collected from closed indices if explicitly specified or if expand_wildcards expands to closed indices
   * @param groups A comma-separated list of search groups for `search` index metric
   * @param includeSegmentFileSizes Whether to report the aggregated disk usage of each one of the Lucene index files (only applies if segment stats are requested)
   * @param includeUnloadedSegments If set to true segment stats will include stats for segments that are not currently loaded into memory
   * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
   * @param level Return stats aggregated at cluster, index or shard level
   * @param metric Limit the information returned the specific metrics.
   * @param types A comma-separated list of document types for the `indexing` index metric
   */
  def stats(
    completionFields: Chunk[String] = Chunk.empty,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    fielddataFields: Chunk[String] = Chunk.empty,
    fields: Chunk[String] = Chunk.empty,
    forbidClosedIndices: Boolean = true,
    groups: Chunk[String] = Chunk.empty,
    includeSegmentFileSizes: Boolean = false,
    includeUnloadedSegments: Boolean = false,
    indices: Chunk[String] = Chunk.empty,
    level: Level = Level.indices,
    metric: Option[String] = None,
    types: Chunk[String] = Chunk.empty
  ): ZIO[IndicesService, FrameworkException, IndicesStatsResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.stats(
        completionFields = completionFields,
        expandWildcards = expandWildcards,
        fielddataFields = fielddataFields,
        fields = fields,
        forbidClosedIndices = forbidClosedIndices,
        groups = groups,
        includeSegmentFileSizes = includeSegmentFileSizes,
        includeUnloadedSegments = includeUnloadedSegments,
        indices = indices,
        level = level,
        metric = metric,
        types = types
      )
    )

  def stats(request: IndicesStatsRequest): ZIO[IndicesService, FrameworkException, IndicesStatsResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Updates index aliases.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-aliases.html
   *
   * @param body body the body of the call
   * @param masterTimeout Specify timeout for connection to master
   * @param timeout Request timeout
   */
  def updateAliases(
    body: Json.Obj,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None
  ): ZIO[IndicesService, FrameworkException, IndicesUpdateAliasesResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.updateAliases(body = body, masterTimeout = masterTimeout, timeout = timeout)
    )

  def updateAliases(
    request: IndicesUpdateAliasesRequest
  ): ZIO[IndicesService, FrameworkException, IndicesUpdateAliasesResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * The _upgrade API is no longer useful and will be removed.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-upgrade.html
   *
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
   * @param onlyAncientSegments If true, only ancient (an older Lucene major release) segments will be upgraded
   * @param waitForCompletion Specify whether the request should block until the all segments are upgraded (default: false)
   */
  def upgrade(
    allowNoIndices: Option[Boolean] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty,
    onlyAncientSegments: Option[Boolean] = None,
    waitForCompletion: Option[Boolean] = None
  ): ZIO[IndicesService, FrameworkException, IndicesUpgradeResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.upgrade(
        allowNoIndices = allowNoIndices,
        expandWildcards = expandWildcards,
        ignoreUnavailable = ignoreUnavailable,
        indices = indices,
        onlyAncientSegments = onlyAncientSegments,
        waitForCompletion = waitForCompletion
      )
    )

  def upgrade(request: IndicesUpgradeRequest): ZIO[IndicesService, FrameworkException, IndicesUpgradeResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

  /*
   * Allows a user to validate a potentially expensive query without executing it.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/search-validate.html
   *
   * @param allShards Execute validation on all shards instead of one random shard per index
   * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
   * @param analyzeWildcard Specify whether wildcard and prefix queries should be analyzed (default: false)
   * @param analyzer The analyzer to use for the query string
   * @param body body the body of the call
   * @param defaultOperator The default operator for query string query (AND or OR)
   * @param df The field to use as default where no field prefix is given in the query string
   * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
   * @param explain Return detailed information about the error
   * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
   * @param indices A comma-separated list of index names to restrict the operation; use `_all` or empty string to perform the operation on all indices
   * @param lenient Specify whether format-based query failures (such as providing text to a numeric field) should be ignored
   * @param q Query in the Lucene query string syntax
   * @param rewrite Provide a more detailed explanation showing the actual Lucene query that will be executed.
   */
  def validateQuery(
    body: Json.Obj,
    allShards: Option[Boolean] = None,
    allowNoIndices: Option[Boolean] = None,
    analyzeWildcard: Option[Boolean] = None,
    analyzer: Option[String] = None,
    defaultOperator: DefaultOperator = DefaultOperator.OR,
    df: Option[String] = None,
    expandWildcards: Seq[ExpandWildcards] = Nil,
    explain: Option[Boolean] = None,
    ignoreUnavailable: Option[Boolean] = None,
    indices: Chunk[String] = Chunk.empty,
    lenient: Option[Boolean] = None,
    q: Option[String] = None,
    rewrite: Option[Boolean] = None
  ): ZIO[IndicesService, FrameworkException, IndicesValidateQueryResponse] =
    ZIO.environmentWithZIO[IndicesService](
      _.get.validateQuery(
        allShards = allShards,
        allowNoIndices = allowNoIndices,
        analyzeWildcard = analyzeWildcard,
        analyzer = analyzer,
        body = body,
        defaultOperator = defaultOperator,
        df = df,
        expandWildcards = expandWildcards,
        explain = explain,
        ignoreUnavailable = ignoreUnavailable,
        indices = indices,
        lenient = lenient,
        q = q,
        rewrite = rewrite
      )
    )

  def validateQuery(
    request: IndicesValidateQueryRequest
  ): ZIO[IndicesService, FrameworkException, IndicesValidateQueryResponse] =
    ZIO.environmentWithZIO[IndicesService](_.get.execute(request))

}
