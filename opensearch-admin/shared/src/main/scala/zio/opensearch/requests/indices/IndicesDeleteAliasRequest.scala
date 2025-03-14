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
 * Deletes an alias.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-aliases.html
 *
 * @param indices A comma-separated list of index names (supports wildcards); use `_all` for all indices
 * @param name A comma-separated list of aliases to delete (supports wildcards); use `_all` to delete all aliases for the specified indices.
 * @param masterTimeout Specify timeout for connection to master
 * @param timeout Explicit timestamp for the document
 */
final case class IndicesDeleteAliasRequest(
  indices: Chunk[String] = Chunk.empty,
  name: Chunk[String] = Chunk.empty,
  @jsonField("master_timeout") masterTimeout: Option[String] = None,
  timeout: Option[String] = None
) extends ActionRequest {
  def method: Method = Method.DELETE
  def urlPath: String = this.makeUrl(indices, "_aliases", name)
  def queryArgs: Map[String, String] = {
    val queryArgs = new mutable.HashMap[String, String]()
    masterTimeout.foreach { v =>
      queryArgs += "master_timeout" -> v.toString
    }
    timeout.foreach { v =>
      queryArgs += "timeout" -> v.toString
    }
    queryArgs.toMap
  }
  def body: Json = Json.Null
}
object IndicesDeleteAliasRequest {
  implicit val jsonDecoder: JsonDecoder[IndicesDeleteAliasRequest] = DeriveJsonDecoder.gen[IndicesDeleteAliasRequest]
  implicit val jsonEncoder: JsonEncoder[IndicesDeleteAliasRequest] = DeriveJsonEncoder.gen[IndicesDeleteAliasRequest]
}
