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

package zio.opensearch.cat.pending_tasks
import zio.Chunk

import scala.collection.mutable
import zio.opensearch.cat.CatRequestBase
import zio.opensearch.common._
import zio.json.ast._
/*
 * Returns a concise representation of the cluster pending tasks.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cat-pending-tasks.html
 *
 * @param format a short version of the Accept header, e.g. json, yaml
 * @param h Comma-separated list of column names to display
 * @param help Return help information
 * @param local Return local information, do not retrieve the state from master node (default: false)
 * @param masterTimeout Explicit operation timeout for connection to master node
 * @param s Comma-separated list of column names or column aliases to sort by
 * @param time The unit in which to display time values
 * @param v Verbose mode. Display column headers
 */

final case class PendingTasksRequest(
  format: Option[String] = None,
  h: Chunk[String] = Chunk.empty,
  help: Boolean = false,
  local: Option[Boolean] = None,
  masterTimeout: Option[String] = None,
  s: Chunk[String] = Chunk.empty,
  time: Option[Time] = None,
  v: Boolean = false
) extends ActionRequest[Json]
    with CatRequestBase {
  def method: Method = Method.GET

  def urlPath = "/_cat/pending_tasks"

  def queryArgs: Map[String, String] = {
    // managing parameters
    val queryArgs = new mutable.HashMap[String, String]()
    format.foreach { v =>
      queryArgs += ("format" -> v)
    }
    if (h.nonEmpty) {
      queryArgs += ("h" -> h.toList.mkString(","))
    }
    if (help != false) queryArgs += ("help" -> help.toString)
    local.foreach { v =>
      queryArgs += ("local" -> v.toString)
    }
    masterTimeout.foreach { v =>
      queryArgs += ("master_timeout" -> v.toString)
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
