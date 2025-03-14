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

package zio.opensearch.nodes.stats
import zio.opensearch.nodes.RepositoryMeteringInformation
import zio.json._
import zio.json.ast._
/*
 * Returns statistical information about nodes in the cluster.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-nodes-stats.html
 *
 * @param clusterName Name of the cluster. Based on the [Cluster name setting](https://www.elastic.co/guide/en/opensearch/reference/current/important-settings.html#cluster-name).

 * @param nodes Contains repositories metering information for the nodes selected by the request.

 */
final case class StatsResponse(
  clusterName: String,
  nodes: Map[String, RepositoryMeteringInformation] = Map.empty[String, RepositoryMeteringInformation]
) {}
object StatsResponse {
  implicit lazy val jsonCodec: JsonCodec[StatsResponse] =
    DeriveJsonCodec.gen[StatsResponse]
}
