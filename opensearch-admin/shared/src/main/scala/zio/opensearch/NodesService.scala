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
import zio.opensearch.client.NodesActionResolver
import zio.opensearch.requests.nodes._
import zio.opensearch.responses.nodes._
import zio._
import zio.opensearch.common.Level
trait NodesService extends NodesActionResolver {

  /*
   * Returns information about hot threads on each node in the cluster.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-nodes-hot-threads.html
   *
   * @param ignoreIdleThreads Don't show threads that are in known-idle places, such as waiting on a socket select or pulling from an empty task queue (default: true)
   * @param interval The interval for the second sampling of threads
   * @param nodeId A comma-separated list of node IDs or names to limit the returned information; use `_local` to return information from the node you're connecting to, leave empty to get information from all nodes
   * @param snapshots Number of samples of thread stacktrace (default: 10)
   * @param threads Specify the number of threads to provide information for (default: 3)
   * @param timeout Explicit operation timeout
   * @param `type` The type to sample (default: cpu)
   */
  def hotThreads(
    ignoreIdleThreads: Option[Boolean] = None,
    interval: Option[String] = None,
    nodeId: Chunk[String] = Chunk.empty,
    snapshots: Option[Double] = None,
    threads: Option[Double] = None,
    timeout: Option[String] = None,
    `type`: Option[Type] = None
  ): ZIO[Any, FrameworkException, NodesHotThreadsResponse] = {
    val request = NodesHotThreadsRequest(
      ignoreIdleThreads = ignoreIdleThreads,
      interval = interval,
      nodeId = nodeId,
      snapshots = snapshots,
      threads = threads,
      timeout = timeout,
      `type` = `type`
    )

    hotThreads(request)

  }

  def hotThreads(request: NodesHotThreadsRequest): ZIO[Any, FrameworkException, NodesHotThreadsResponse] =
    execute(request)

  /*
   * Returns information about nodes in the cluster.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-nodes-info.html
   *
   * @param flatSettings Return settings in flat format (default: false)
   * @param metric A comma-separated list of metrics you wish returned. Leave empty to return all.
   * @param nodeId A comma-separated list of node IDs or names to limit the returned information; use `_local` to return information from the node you're connecting to, leave empty to get information from all nodes
   * @param timeout Explicit operation timeout
   */
  def info(
    flatSettings: Option[Boolean] = None,
    metric: Chunk[String] = Chunk.empty,
    nodeId: Chunk[String] = Chunk.empty,
    timeout: Option[String] = None
  ): ZIO[Any, FrameworkException, NodesInfoResponse] = {
    val request = NodesInfoRequest(flatSettings = flatSettings, metric = metric, nodeId = nodeId, timeout = timeout)

    info(request)

  }

  def info(request: NodesInfoRequest): ZIO[Any, FrameworkException, NodesInfoResponse] =
    execute(request)

  /*
   * Reloads secure settings.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/secure-settings.html#reloadable-secure-settings
   *
   * @param nodeId A comma-separated list of node IDs to span the reload/reinit call. Should stay empty because reloading usually involves all cluster nodes.
   * @param timeout Explicit operation timeout
   */
  def reloadSecureSettings(
    nodeId: Chunk[String] = Chunk.empty,
    timeout: Option[String] = None
  ): ZIO[Any, FrameworkException, NodesReloadSecureSettingsResponse] = {
    val request =
      NodesReloadSecureSettingsRequest(nodeId = nodeId, timeout = timeout)

    reloadSecureSettings(request)

  }

  def reloadSecureSettings(
    request: NodesReloadSecureSettingsRequest
  ): ZIO[Any, FrameworkException, NodesReloadSecureSettingsResponse] =
    execute(request)

  /*
   * Returns statistical information about nodes in the cluster.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-nodes-stats.html
   *
   * @param completionFields A comma-separated list of fields for `fielddata` and `suggest` index metric (supports wildcards)
   * @param fielddataFields A comma-separated list of fields for `fielddata` index metric (supports wildcards)
   * @param fields A comma-separated list of fields for `fielddata` and `completion` index metric (supports wildcards)
   * @param groups A comma-separated list of search groups for `search` index metric
   * @param includeSegmentFileSizes Whether to report the aggregated disk usage of each one of the Lucene index files (only applies if segment stats are requested)
   * @param indexMetric Limit the information returned for `indices` metric to the specific index metrics. Isn't used if `indices` (or `all`) metric isn't specified.
   * @param level Return indices stats aggregated at index, node or shard level
   * @param metric Limit the information returned to the specified metrics
   * @param nodeId A comma-separated list of node IDs or names to limit the returned information; use `_local` to return information from the node you're connecting to, leave empty to get information from all nodes
   * @param timeout Explicit operation timeout
   * @param types A comma-separated list of document types for the `indexing` index metric
   */
  def stats(
    completionFields: Chunk[String] = Chunk.empty,
    fielddataFields: Chunk[String] = Chunk.empty,
    fields: Chunk[String] = Chunk.empty,
    groups: Chunk[String] = Chunk.empty,
    includeSegmentFileSizes: Boolean = false,
    indexMetric: Option[String] = None,
    level: Level = Level.node,
    metric: Option[String] = None,
    nodeId: Chunk[String] = Chunk.empty,
    timeout: Option[String] = None,
    types: Chunk[String] = Chunk.empty
  ): ZIO[Any, FrameworkException, NodesStatsResponse] = {
    val request = NodesStatsRequest(
      completionFields = completionFields,
      fielddataFields = fielddataFields,
      fields = fields,
      groups = groups,
      includeSegmentFileSizes = includeSegmentFileSizes,
      indexMetric = indexMetric,
      level = level,
      metric = metric,
      nodeId = nodeId,
      timeout = timeout,
      types = types
    )

    stats(request)

  }

  def stats(request: NodesStatsRequest): ZIO[Any, FrameworkException, NodesStatsResponse] =
    execute(request)

  /*
   * Returns low-level information about REST actions usage on nodes.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-nodes-usage.html
   *
   * @param metric Limit the information returned to the specified metrics
   * @param nodeId A comma-separated list of node IDs or names to limit the returned information; use `_local` to return information from the node you're connecting to, leave empty to get information from all nodes
   * @param timeout Explicit operation timeout
   */
  def usage(
    metric: Option[String] = None,
    nodeId: Chunk[String] = Chunk.empty,
    timeout: Option[String] = None
  ): ZIO[Any, FrameworkException, NodesUsageResponse] = {
    val request =
      NodesUsageRequest(metric = metric, nodeId = nodeId, timeout = timeout)

    usage(request)

  }

  def usage(request: NodesUsageRequest): ZIO[Any, FrameworkException, NodesUsageResponse] =
    execute(request)

}

object NodesService {

  // services

  private case class Live(
    baseOpenSearchService: OpenSearchService,
    httpService: OpenSearchHttpService
  ) extends NodesService

  val live: ZLayer[OpenSearchService, Nothing, NodesService] =
    ZLayer {
      for { baseOpenSearchService <- ZIO.service[OpenSearchService] } yield Live(
        baseOpenSearchService,
        baseOpenSearchService.httpService
      )
    }

  // access methods

  /*
   * Returns information about hot threads on each node in the cluster.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-nodes-hot-threads.html
   *
   * @param ignoreIdleThreads Don't show threads that are in known-idle places, such as waiting on a socket select or pulling from an empty task queue (default: true)
   * @param interval The interval for the second sampling of threads
   * @param nodeId A comma-separated list of node IDs or names to limit the returned information; use `_local` to return information from the node you're connecting to, leave empty to get information from all nodes
   * @param snapshots Number of samples of thread stacktrace (default: 10)
   * @param threads Specify the number of threads to provide information for (default: 3)
   * @param timeout Explicit operation timeout
   * @param `type` The type to sample (default: cpu)
   */
  def hotThreads(
    ignoreIdleThreads: Option[Boolean] = None,
    interval: Option[String] = None,
    nodeId: Chunk[String] = Chunk.empty,
    snapshots: Option[Double] = None,
    threads: Option[Double] = None,
    timeout: Option[String] = None,
    `type`: Option[Type] = None
  ): ZIO[NodesService, FrameworkException, NodesHotThreadsResponse] =
    ZIO.environmentWithZIO[NodesService](
      _.get.hotThreads(
        ignoreIdleThreads = ignoreIdleThreads,
        interval = interval,
        nodeId = nodeId,
        snapshots = snapshots,
        threads = threads,
        timeout = timeout,
        `type` = `type`
      )
    )

  def hotThreads(request: NodesHotThreadsRequest): ZIO[NodesService, FrameworkException, NodesHotThreadsResponse] =
    ZIO.environmentWithZIO[NodesService](_.get.execute(request))

  /*
   * Returns information about nodes in the cluster.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-nodes-info.html
   *
   * @param flatSettings Return settings in flat format (default: false)
   * @param metric A comma-separated list of metrics you wish returned. Leave empty to return all.
   * @param nodeId A comma-separated list of node IDs or names to limit the returned information; use `_local` to return information from the node you're connecting to, leave empty to get information from all nodes
   * @param timeout Explicit operation timeout
   */
  def info(
    flatSettings: Option[Boolean] = None,
    metric: Chunk[String] = Chunk.empty,
    nodeId: Chunk[String] = Chunk.empty,
    timeout: Option[String] = None
  ): ZIO[NodesService, FrameworkException, NodesInfoResponse] =
    ZIO.environmentWithZIO[NodesService](
      _.get.info(flatSettings = flatSettings, metric = metric, nodeId = nodeId, timeout = timeout)
    )

  def info(request: NodesInfoRequest): ZIO[NodesService, FrameworkException, NodesInfoResponse] =
    ZIO.environmentWithZIO[NodesService](_.get.execute(request))

  /*
   * Reloads secure settings.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/secure-settings.html#reloadable-secure-settings
   *
   * @param nodeId A comma-separated list of node IDs to span the reload/reinit call. Should stay empty because reloading usually involves all cluster nodes.
   * @param timeout Explicit operation timeout
   */
  def reloadSecureSettings(
    nodeId: Chunk[String] = Chunk.empty,
    timeout: Option[String] = None
  ): ZIO[NodesService, FrameworkException, NodesReloadSecureSettingsResponse] =
    ZIO.environmentWithZIO[NodesService](_.get.reloadSecureSettings(nodeId = nodeId, timeout = timeout))

  def reloadSecureSettings(
    request: NodesReloadSecureSettingsRequest
  ): ZIO[NodesService, FrameworkException, NodesReloadSecureSettingsResponse] =
    ZIO.environmentWithZIO[NodesService](_.get.execute(request))

  /*
   * Returns statistical information about nodes in the cluster.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-nodes-stats.html
   *
   * @param completionFields A comma-separated list of fields for `fielddata` and `suggest` index metric (supports wildcards)
   * @param fielddataFields A comma-separated list of fields for `fielddata` index metric (supports wildcards)
   * @param fields A comma-separated list of fields for `fielddata` and `completion` index metric (supports wildcards)
   * @param groups A comma-separated list of search groups for `search` index metric
   * @param includeSegmentFileSizes Whether to report the aggregated disk usage of each one of the Lucene index files (only applies if segment stats are requested)
   * @param indexMetric Limit the information returned for `indices` metric to the specific index metrics. Isn't used if `indices` (or `all`) metric isn't specified.
   * @param level Return indices stats aggregated at index, node or shard level
   * @param metric Limit the information returned to the specified metrics
   * @param nodeId A comma-separated list of node IDs or names to limit the returned information; use `_local` to return information from the node you're connecting to, leave empty to get information from all nodes
   * @param timeout Explicit operation timeout
   * @param types A comma-separated list of document types for the `indexing` index metric
   */
  def stats(
    completionFields: Chunk[String] = Chunk.empty,
    fielddataFields: Chunk[String] = Chunk.empty,
    fields: Chunk[String] = Chunk.empty,
    groups: Chunk[String] = Chunk.empty,
    includeSegmentFileSizes: Boolean = false,
    indexMetric: Option[String] = None,
    level: Level = Level.node,
    metric: Option[String] = None,
    nodeId: Chunk[String] = Chunk.empty,
    timeout: Option[String] = None,
    types: Chunk[String] = Chunk.empty
  ): ZIO[NodesService, FrameworkException, NodesStatsResponse] =
    ZIO.environmentWithZIO[NodesService](
      _.get.stats(
        completionFields = completionFields,
        fielddataFields = fielddataFields,
        fields = fields,
        groups = groups,
        includeSegmentFileSizes = includeSegmentFileSizes,
        indexMetric = indexMetric,
        level = level,
        metric = metric,
        nodeId = nodeId,
        timeout = timeout,
        types = types
      )
    )

  def stats(request: NodesStatsRequest): ZIO[NodesService, FrameworkException, NodesStatsResponse] =
    ZIO.environmentWithZIO[NodesService](_.get.execute(request))

  /*
   * Returns low-level information about REST actions usage on nodes.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-nodes-usage.html
   *
   * @param metric Limit the information returned to the specified metrics
   * @param nodeId A comma-separated list of node IDs or names to limit the returned information; use `_local` to return information from the node you're connecting to, leave empty to get information from all nodes
   * @param timeout Explicit operation timeout
   */
  def usage(
    metric: Option[String] = None,
    nodeId: Chunk[String] = Chunk.empty,
    timeout: Option[String] = None
  ): ZIO[NodesService, FrameworkException, NodesUsageResponse] =
    ZIO.environmentWithZIO[NodesService](_.get.usage(metric = metric, nodeId = nodeId, timeout = timeout))

  def usage(request: NodesUsageRequest): ZIO[NodesService, FrameworkException, NodesUsageResponse] =
    ZIO.environmentWithZIO[NodesService](_.get.execute(request))

}
