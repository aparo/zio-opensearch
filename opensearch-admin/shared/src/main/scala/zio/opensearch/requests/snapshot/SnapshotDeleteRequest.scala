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
 * Deletes a snapshot.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/modules-snapshots.html
 *
 * @param repository A repository name
 * @param snapshot A snapshot name
 * @param masterTimeout Explicit operation timeout for connection to master node
 */
final case class SnapshotDeleteRequest(
  repository: String,
  snapshot: String,
  @jsonField("master_timeout") masterTimeout: Option[String] = None
) extends ActionRequest {
  def method: Method = Method.DELETE
  def urlPath: String = this.makeUrl("_snapshot", repository, snapshot)
  def queryArgs: Map[String, String] = {
    val queryArgs = new mutable.HashMap[String, String]()
    masterTimeout.foreach { v =>
      queryArgs += "master_timeout" -> v.toString
    }
    queryArgs.toMap
  }
  def body: Json = Json.Null
}
object SnapshotDeleteRequest {
  implicit val jsonDecoder: JsonDecoder[SnapshotDeleteRequest] = DeriveJsonDecoder.gen[SnapshotDeleteRequest]
  implicit val jsonEncoder: JsonEncoder[SnapshotDeleteRequest] = DeriveJsonEncoder.gen[SnapshotDeleteRequest]
}
