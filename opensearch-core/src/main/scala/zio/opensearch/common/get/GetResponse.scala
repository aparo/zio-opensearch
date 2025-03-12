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

package zio.opensearch.common.get
import zio.json._
import zio.json.ast._
import zio.opensearch.common.ShardStatistics
/*
 * Returns a document.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/docs-get.html
 *
 * @param languageContexts

 * @param typesAllowed

 */
final case class GetResponse(
  @jsonField("_index") index: String,
  @jsonField("_id") id: String,
  @jsonField("_version") version: Long = 1,
  @jsonField("_shards") shards: ShardStatistics = ShardStatistics(),
  found: Boolean = false,
  @jsonField("_source") source: Option[Json.Obj] = None,
  @jsonField("fields") fields: Json.Obj = Json.Obj()
//                              error: Option[ErrorResponse] = None
) {
  def getId: String = id
  def getIndex: String = index
  def getVersion: Long = version
}
object GetResponse {
  implicit lazy val jsonCodec: JsonCodec[GetResponse] =
    DeriveJsonCodec.gen[GetResponse]
}
