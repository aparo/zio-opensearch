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

package zio.opensearch.cat.recovery
import zio.Chunk

import scala.collection.mutable
import zio.opensearch.cat.CatRequestBase
import zio.opensearch.common._
import zio.json.ast._
/*
 * Returns information about index shard recoveries, both on-going completed.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cat-recovery.html
 *
 * @param local If `true`, the request computes the list of selected nodes from the
 * local cluster state. If `false` the list of selected nodes are computed
 * from the cluster state of the master node. In both cases the coordinating
 * node will send requests for further information to each selected node.
 * @server_default false

 * @param masterTimeout Period to wait for a connection to the master node.
 * @server_default 30s

 * @param activeOnly If `true`, the response only includes ongoing shard recoveries
 * @param bytes The unit in which to display byte values
 * @param detailed If `true`, the response includes detailed information about shard recoveries
 * @param format a short version of the Accept header, e.g. json, yaml
 * @param h Comma-separated list of column names to display
 * @param help Return help information
 * @param index Comma-separated list or wildcard expression of index names to limit the returned information
 * @param s Comma-separated list of column names or column aliases to sort by
 * @param time The unit in which to display time values
 * @param v Verbose mode. Display column headers
 */

final case class RecoveryRequest(
  local: Boolean,
  masterTimeout: Option[String] = None,
  activeOnly: Boolean = false,
  bytes: Option[Bytes] = None,
  detailed: Boolean = false,
  format: Option[String] = None,
  h: Chunk[String] = Chunk.empty,
  help: Boolean = false,
  index: Chunk[String] = Chunk.empty,
  s: Chunk[String] = Chunk.empty,
  time: Option[Time] = None,
  v: Boolean = false
) extends ActionRequest[Json]
    with CatRequestBase {
  def method: Method = Method.GET

  def urlPath: String = this.makeUrl("_cat", "recovery", index)

  def queryArgs: Map[String, String] = {
    // managing parameters
    val queryArgs = new mutable.HashMap[String, String]()
    if (activeOnly != false) queryArgs += ("active_only" -> activeOnly.toString)
    bytes.foreach { v =>
      queryArgs += ("bytes" -> v.toString)
    }
    if (detailed != false) queryArgs += ("detailed" -> detailed.toString)
    format.foreach { v =>
      queryArgs += ("format" -> v)
    }
    if (h.nonEmpty) {
      queryArgs += ("h" -> h.toList.mkString(","))
    }
    if (help != false) queryArgs += ("help" -> help.toString)
    if (index.nonEmpty) {
      queryArgs += ("index" -> index.toList.mkString(","))
    }
    if (s.nonEmpty) {
      queryArgs += ("s" -> s.toList.mkString(","))
    }
    time.foreach { v =>
      queryArgs += ("time" -> v.toString)
    }
    if (v != false) queryArgs += ("v" -> v.toString)
    // Custom Code On
    // Custom Code Off
    queryArgs.toMap
  }

  def body: Json = Json.Null

  // Custom Code On
  // Custom Code Off

}
