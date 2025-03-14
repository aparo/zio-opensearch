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

package zio.opensearch.cat.indices
import scala.collection.mutable
import zio._
import zio.opensearch.cat.{ CatRequestBase, Health }
import zio.opensearch.common._
import zio.json.ast._
/*
 * Returns information about indices: number of primaries and replicas, document counts, disk size, ...
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cat-indices.html
 *
 * @param index

 * @param local If `true`, the request computes the list of selected nodes from the
 * local cluster state. If `false` the list of selected nodes are computed
 * from the cluster state of the master node. In both cases the coordinating
 * node will send requests for further information to each selected node.
 * @server_default false

 * @param bytes The unit in which to display byte values
 * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
 * @param format a short version of the Accept header, e.g. json, yaml
 * @param h Comma-separated list of column names to display
 * @param health A health status ("green", "yellow", or "red" to filter only indices matching the specified health status
 * @param help Return help information
 * @param includeUnloadedSegments If set to true segment stats will include stats for segments that are not currently loaded into memory
 * @param indices A comma-separated list of index names to limit the returned information
 * @param masterTimeout Explicit operation timeout for connection to master node
 * @param pri Set to true to return stats only for primary shards
 * @param s Comma-separated list of column names or column aliases to sort by
 * @param time The unit in which to display time values
 * @param v Verbose mode. Display column headers
 */

final case class IndicesRequest(
  index: Chunk[String],
  local: Boolean,
  bytes: Option[Bytes] = None,
  expandWildcards: Seq[ExpandWildcards] = Nil,
  format: Option[String] = None,
  h: Chunk[String] = Chunk.empty,
  health: Option[Health] = None,
  help: Boolean = false,
  includeUnloadedSegments: Boolean = false,
  indices: Chunk[String] = Chunk.empty,
  masterTimeout: Option[String] = None,
  pri: Boolean = false,
  s: Chunk[String] = Chunk.empty,
  time: Option[Time] = None,
  v: Boolean = false
) extends ActionRequest[Json]
    with CatRequestBase {
  def method: Method = Method.GET

  def urlPath: String = this.makeUrl("_cat", "indices", indices)

  def queryArgs: Map[String, String] = {
    // managing parameters
    val queryArgs = new mutable.HashMap[String, String]()
    bytes.foreach { v =>
      queryArgs += ("bytes" -> v.toString)
    }
    if (expandWildcards.nonEmpty) {
      if (expandWildcards.toSet != Set(ExpandWildcards.all)) {
        queryArgs += ("expand_wildcards" -> expandWildcards.mkString(","))
      }

    }
    format.foreach { v =>
      queryArgs += ("format" -> v)
    }
    if (h.nonEmpty) {
      queryArgs += ("h" -> h.toList.mkString(","))
    }
    health.foreach { v =>
      queryArgs += ("health" -> v.toString)
    }
    if (help != false) queryArgs += ("help" -> help.toString)
    if (includeUnloadedSegments != false)
      queryArgs += ("include_unloaded_segments" -> includeUnloadedSegments.toString)
    masterTimeout.foreach { v =>
      queryArgs += ("master_timeout" -> v.toString)
    }
    if (pri != false) queryArgs += ("pri" -> pri.toString)
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
