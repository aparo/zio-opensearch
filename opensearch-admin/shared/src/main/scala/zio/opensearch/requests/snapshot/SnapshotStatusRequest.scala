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

package zio.opensearch.requests.snapshot

import scala.collection.mutable

import zio.opensearch.requests.ActionRequest
import zio.json.ast.Json
import zio.json._
import zio.json.ast._

/*
 * Returns information about the status of a snapshot.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/modules-snapshots.html
 *
 * @param ignoreUnavailable Whether to ignore unavailable snapshots, defaults to false which means a SnapshotMissingException is thrown
 * @param masterTimeout Explicit operation timeout for connection to master node
 * @param repository A repository name
 * @param snapshot A comma-separated list of snapshot names
 */
final case class SnapshotStatusRequest(
  @jsonField("ignore_unavailable") ignoreUnavailable: Option[Boolean] = None,
  @jsonField("master_timeout") masterTimeout: Option[String] = None,
  repository: Option[String] = None,
  snapshot: Chunk[String] = Chunk.empty
) extends ActionRequest {
  def method: Method = Method.GET
  def urlPath: String = this.makeUrl("_snapshot", repository, snapshot, "_status")
  def queryArgs: Map[String, String] = {
    val queryArgs = new mutable.HashMap[String, String]()
    ignoreUnavailable.foreach { v =>
      queryArgs += "ignore_unavailable" -> v.toString
    }
    masterTimeout.foreach { v =>
      queryArgs += "master_timeout" -> v.toString
    }
    queryArgs.toMap
  }
  def body: Json = Json.Null
}
object SnapshotStatusRequest {
  implicit val jsonDecoder: JsonDecoder[SnapshotStatusRequest] = DeriveJsonDecoder.gen[SnapshotStatusRequest]
  implicit val jsonEncoder: JsonEncoder[SnapshotStatusRequest] = DeriveJsonEncoder.gen[SnapshotStatusRequest]
}
