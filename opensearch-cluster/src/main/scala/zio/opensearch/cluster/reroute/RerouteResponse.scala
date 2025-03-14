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

package zio.opensearch.cluster.reroute
import zio._
import zio.json._
import zio.json.ast._
/*
 * Allows to manually change the allocation of individual shards in the cluster.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-reroute.html
 *
 * @param acknowledged

 * @param explanations

 * @param state There aren't any guarantees on the output/structure of the raw cluster state.
 * Here you will find the internal representation of the cluster, which can
 * differ from the external representation.

 */
final case class RerouteResponse(
  acknowledged: Boolean = true,
  explanations: Chunk[RerouteExplanation] = Chunk.empty[RerouteExplanation],
  state: Json
) {}
object RerouteResponse {
  implicit lazy val jsonCodec: JsonCodec[RerouteResponse] =
    DeriveJsonCodec.gen[RerouteResponse]
}
