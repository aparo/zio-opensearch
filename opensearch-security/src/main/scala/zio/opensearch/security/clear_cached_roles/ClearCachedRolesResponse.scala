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

package zio.opensearch.security.clear_cached_roles
import zio.opensearch.common.NodeStatistics
import zio.opensearch.security.ClusterNode
import zio.json._
import zio.json.ast._
/*
 * Evicts roles from the native role cache.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/security-api-clear-role-cache.html
 *
 * @param nodes
@codegen_name node_stats

 * @param clusterName

 * @param nodes

 */
final case class ClearCachedRolesResponse(
  nodes: NodeStatistics,
  clusterName: String,
  _nodes: Map[String, ClusterNode] = Map.empty[String, ClusterNode]
) {}
object ClearCachedRolesResponse {
  implicit lazy val jsonCodec: JsonCodec[ClearCachedRolesResponse] =
    DeriveJsonCodec.gen[ClearCachedRolesResponse]
}
