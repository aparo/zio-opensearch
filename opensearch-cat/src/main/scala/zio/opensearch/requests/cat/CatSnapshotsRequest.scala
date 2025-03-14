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

import zio.opensearch.Time
import zio.json.ast.Json
import zio.json._
import zio.json.ast._

import scala.collection.mutable
import zio.opensearch.requests.ActionRequest

/*
 * Returns all snapshots in a specific repository.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cat-snapshots.html
 *
 * @param format a short version of the Accept header, e.g. json, yaml
 * @param h Comma-separated list of column names to display
 * @param help Return help information
 * @param ignoreUnavailable Set to true to ignore unavailable snapshots
 * @param masterTimeout Explicit operation timeout for connection to master node
 * @param repository Name of repository from which to fetch the snapshot information
 * @param s Comma-separated list of column names or column aliases to sort by
 * @param time The unit in which to display time values
 * @param v Verbose mode. Display column headers
 */
final case class CatSnapshotsRequest(
  format: Option[String] = None,
  h: Chunk[String] = Chunk.empty,
  help: Boolean = false,
  @jsonField("ignore_unavailable") ignoreUnavailable: Boolean = false,
  @jsonField("master_timeout") masterTimeout: Option[String] = None,
  repository: Option[String] = None,
  s: Chunk[String] = Chunk.empty,
  time: Option[Time] = None,
  v: Boolean = false
) extends ActionRequest {
  def method: Method = Method.GET
  def urlPath: String = this.makeUrl("_cat", "snapshots", repository)
  def queryArgs: Map[String, String] = {
    val queryArgs = new mutable.HashMap[String, String]()
    format.foreach { v =>
      queryArgs += "format" -> v
    }
    if (h.nonEmpty) {
      queryArgs += "h" -> h.toList.mkString(",")
    }
    if (help != false) queryArgs += "help" -> help.toString
    if (ignoreUnavailable != false) queryArgs += "ignore_unavailable" -> ignoreUnavailable.toString
    masterTimeout.foreach { v =>
      queryArgs += "master_timeout" -> v.toString
    }
    if (s.nonEmpty) {
      queryArgs += "s" -> s.toList.mkString(",")
    }
    time.foreach { v =>
      queryArgs += "time" -> v.toString
    }
    if (v != false) queryArgs += "v" -> v.toString
    queryArgs.toMap
  }
  def body: Json = Json.Null
}
object CatSnapshotsRequest {
  implicit val jsonDecoder: JsonDecoder[CatSnapshotsRequest] = DeriveJsonDecoder.gen[CatSnapshotsRequest]
  implicit val jsonEncoder: JsonEncoder[CatSnapshotsRequest] = DeriveJsonEncoder.gen[CatSnapshotsRequest]
}
