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

package zio.opensearch.responses.nodes

import zio.json._
/*
 * Returns low-level information about REST actions usage on nodes.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-nodes-usage.html
 *
 * @param metric Limit the information returned to the specified metrics
 * @param nodeId A comma-separated list of node IDs or names to limit the returned information; use `_local` to return information from the node you're connecting to, leave empty to get information from all nodes
 * @param timeout Explicit operation timeout
 */
final case class NodesUsageResponse(_ok: Option[Boolean] = None)
object NodesUsageResponse {
  implicit val jsonDecoder: JsonDecoder[NodesUsageResponse] = DeriveJsonDecoder.gen[NodesUsageResponse]
  implicit val jsonEncoder: JsonEncoder[NodesUsageResponse] = DeriveJsonEncoder.gen[NodesUsageResponse]
}
