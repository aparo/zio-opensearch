/*
 * Copyright 2019 Alberto Paro
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

package zio.opensearch.requests.cat

import zio.opensearch.Bytes
import zio.json.ast.Json
import zio.json._
import zio.json.ast._

import scala.collection.mutable
import zio.opensearch.requests.ActionRequest

/*
 * Provides a snapshot of how many shards are allocated to each data node and how much disk space they are using.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cat-allocation.html
 *
 * @param bytes The unit in which to display byte values
 * @param format a short version of the Accept header, e.g. json, yaml
 * @param h Comma-separated list of column names to display
 * @param help Return help information
 * @param local Return local information, do not retrieve the state from master node (default: false)
 * @param masterTimeout Explicit operation timeout for connection to master node
 * @param nodeId A comma-separated list of node IDs or names to limit the returned information
 * @param s Comma-separated list of column names or column aliases to sort by
 * @param v Verbose mode. Display column headers
 */
final case class CatAllocationRequest(
  bytes: Option[Bytes] = None,
  format: Option[String] = None,
  h: Chunk[String] = Chunk.empty,
  help: Boolean = false,
  local: Option[Boolean] = None,
  @jsonField("master_timeout") masterTimeout: Option[String] = None,
  @jsonField("node_id") nodeId: Chunk[String] = Chunk.empty,
  s: Chunk[String] = Chunk.empty,
  v: Boolean = false
) extends ActionRequest {
  def method: Method = Method.GET
  def urlPath: String = this.makeUrl("_cat", "allocation", nodeId)
  def queryArgs: Map[String, String] = {
    val queryArgs = new mutable.HashMap[String, String]()
    bytes.foreach { v =>
      queryArgs += "bytes" -> v.toString
    }
    format.foreach { v =>
      queryArgs += "format" -> v
    }
    if (h.nonEmpty) {
      queryArgs += "h" -> h.toList.mkString(",")
    }
    if (help != false) queryArgs += "help" -> help.toString
    local.foreach { v =>
      queryArgs += "local" -> v.toString
    }
    masterTimeout.foreach { v =>
      queryArgs += "master_timeout" -> v.toString
    }
    if (s.nonEmpty) {
      queryArgs += "s" -> s.toList.mkString(",")
    }
    if (v != false) queryArgs += "v" -> v.toString
    queryArgs.toMap
  }
  def body: Json = Json.Null
}
object CatAllocationRequest {
  implicit val jsonDecoder: JsonDecoder[CatAllocationRequest] = DeriveJsonDecoder.gen[CatAllocationRequest]
  implicit val jsonEncoder: JsonEncoder[CatAllocationRequest] = DeriveJsonEncoder.gen[CatAllocationRequest]
}
