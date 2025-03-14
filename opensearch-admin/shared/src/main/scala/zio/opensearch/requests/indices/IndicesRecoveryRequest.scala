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

package zio.opensearch.requests.indices

import scala.collection.mutable

import zio.opensearch.requests.ActionRequest
import zio.json.ast.Json
import zio.json._
import zio.json.ast._

/*
 * Returns information about ongoing index shard recoveries.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-recovery.html
 *
 * @param activeOnly Display only those recoveries that are currently on-going
 * @param detailed Whether to display detailed information about shard recovery
 * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
 */
final case class IndicesRecoveryRequest(
  @jsonField("active_only") activeOnly: Boolean = false,
  detailed: Boolean = false,
  indices: Chunk[String] = Chunk.empty
) extends ActionRequest {
  def method: Method = Method.GET
  def urlPath: String = this.makeUrl(indices, "_recovery")
  def queryArgs: Map[String, String] = {
    val queryArgs = new mutable.HashMap[String, String]()
    if (activeOnly != false) queryArgs += "active_only" -> activeOnly.toString
    if (detailed != false) queryArgs += "detailed" -> detailed.toString
    queryArgs.toMap
  }
  def body: Json = Json.Null
}
object IndicesRecoveryRequest {
  implicit val jsonDecoder: JsonDecoder[IndicesRecoveryRequest] = DeriveJsonDecoder.gen[IndicesRecoveryRequest]
  implicit val jsonEncoder: JsonEncoder[IndicesRecoveryRequest] = DeriveJsonEncoder.gen[IndicesRecoveryRequest]
}
