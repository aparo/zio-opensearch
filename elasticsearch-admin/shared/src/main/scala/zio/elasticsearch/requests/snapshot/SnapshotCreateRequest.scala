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

package zio.elasticsearch.requests.snapshot

import scala.collection.mutable

import zio.elasticsearch.requests.ActionRequest
import zio.json.ast.Json
import zio.json._
import zio.json.ast._

/*
 * Creates a snapshot in a repository.
 * For more info refers to https://www.elastic.co/guide/en/elasticsearch/reference/master/modules-snapshots.html
 *
 * @param repository A repository name
 * @param snapshot A snapshot name
 * @param body body the body of the call
 * @param masterTimeout Explicit operation timeout for connection to master node
 * @param waitForCompletion Should this request wait until the operation has completed before returning
 */
final case class SnapshotCreateRequest(
  repository: String,
  snapshot: String,
  body: Option[Json.Obj] = None,
  @jsonField("master_timeout") masterTimeout: Option[String] = None,
  @jsonField("wait_for_completion") waitForCompletion: Boolean = false
) extends ActionRequest {
  def method: String = "PUT"
  def urlPath: String = this.makeUrl("_snapshot", repository, snapshot)
  def queryArgs: Map[String, String] = {
    val queryArgs = new mutable.HashMap[String, String]()
    body.foreach { v =>
      queryArgs += "body" -> v.toString
    }
    masterTimeout.foreach { v =>
      queryArgs += "master_timeout" -> v.toString
    }
    if (waitForCompletion != false) queryArgs += "wait_for_completion" -> waitForCompletion.toString
    queryArgs.toMap
  }
}
object SnapshotCreateRequest {
  implicit val jsonDecoder: JsonDecoder[SnapshotCreateRequest] = DeriveJsonDecoder.gen[SnapshotCreateRequest]
  implicit val jsonEncoder: JsonEncoder[SnapshotCreateRequest] = DeriveJsonEncoder.gen[SnapshotCreateRequest]
}
