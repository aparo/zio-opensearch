/*
 * Copyright 2019-2023 Alberto Paro
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

package zio.elasticsearch.cat.ml_jobs
import scala.collection.mutable
import zio.elasticsearch.cat.CatRequestBase
import zio.elasticsearch.common._
import zio.json.ast._
/*
 * Gets configuration and usage information about anomaly detection jobs.
 * For more info refers to http://www.elastic.co/guide/en/elasticsearch/reference/current/cat-anomaly-detectors.html
 *
 * @param local If `true`, the request computes the list of selected nodes from the
 * local cluster state. If `false` the list of selected nodes are computed
 * from the cluster state of the master node. In both cases the coordinating
 * node will send requests for further information to each selected node.
 * @server_default false

 * @param masterTimeout Period to wait for a connection to the master node.
 * @server_default 30s

 * @param allowNoMatch Whether to ignore if a wildcard expression matches no jobs. (This includes `_all` string or when no jobs have been specified)
 * @param bytes The unit in which to display byte values
 * @param format a short version of the Accept header, e.g. json, yaml
 * @param h Comma-separated list of column names to display
 * @param help Return help information
 * @param jobId The ID of the jobs stats to fetch
 * @param s Comma-separated list of column names or column aliases to sort by
 * @param time The unit in which to display time values
 * @param v Verbose mode. Display column headers
 */

final case class MlJobsRequest(
  local: Boolean,
  masterTimeout: Option[String] = None,
  allowNoMatch: Option[Boolean] = None,
  bytes: Option[Bytes] = None,
  format: Option[String] = None,
  h: Seq[String] = Nil,
  help: Boolean = false,
  jobId: Option[String] = None,
  s: Seq[String] = Nil,
  time: Option[Time] = None,
  v: Boolean = false
) extends ActionRequest[Json]
    with CatRequestBase {
  def method: Method = Method.GET

  def urlPath: String = this.makeUrl("_cat", "ml", "anomaly_detectors", jobId)

  def queryArgs: Map[String, String] = {
    // managing parameters
    val queryArgs = new mutable.HashMap[String, String]()
    allowNoMatch.foreach { v =>
      queryArgs += ("allow_no_match" -> v.toString)
    }
    bytes.foreach { v =>
      queryArgs += ("bytes" -> v.toString)
    }
    format.foreach { v =>
      queryArgs += ("format" -> v)
    }
    if (h.nonEmpty) {
      queryArgs += ("h" -> h.toList.mkString(","))
    }
    if (help != false) queryArgs += ("help" -> help.toString)
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
