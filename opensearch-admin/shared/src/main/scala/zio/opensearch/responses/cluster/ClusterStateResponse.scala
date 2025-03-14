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

package zio.opensearch.responses.cluster

import zio.json._

/*
 * http://www.elastic.co/guide/en/opensearch/reference/master/cluster-state.html
 *
 * @param metric Limit the information returned to the specified metrics
 * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
 * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
 * @param local Return local information, do not retrieve the state from master node (default: false)
 * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
 * @param masterTimeout Specify timeout for connection to master
 * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
 * @param flatSettings Return settings in flat format (default: false)
 */

final case class ClusterStateResponse(
  @jsonField("cluster_name") clusterName: String = "",
  version: Int = 0,
  @jsonField("master_node") masterNode: String = "",
  blocks: Option[Blocks] = None,
  nodes: Map[String, Node] = Map.empty[String, Node],
  metadata: Metadata = Metadata(),
  @jsonField("routing_table") routingTable: Option[RoutingTable] = None,
  @jsonField("routing_nodes") routingNodes: Option[RoutingNodes] = None
)
object ClusterStateResponse {
  implicit val jsonDecoder: JsonDecoder[ClusterStateResponse] = DeriveJsonDecoder.gen[ClusterStateResponse]
  implicit val jsonEncoder: JsonEncoder[ClusterStateResponse] = DeriveJsonEncoder.gen[ClusterStateResponse]
}
