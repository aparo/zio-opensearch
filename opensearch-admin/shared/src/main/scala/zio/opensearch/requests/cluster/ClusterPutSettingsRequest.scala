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

package zio.opensearch.requests.cluster

import scala.collection.mutable

import zio.opensearch.requests.ActionRequest
import zio.json.ast.Json
import zio.json._
import zio.json.ast._

/*
 * Updates the cluster settings.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-update-settings.html
 *
 * @param body body the body of the call
 * @param flatSettings Return settings in flat format (default: false)
 * @param masterTimeout Explicit operation timeout for connection to master node
 * @param timeout Explicit operation timeout
 */
final case class ClusterPutSettingsRequest(
  body: Json.Obj,
  @jsonField("flat_settings") flatSettings: Option[Boolean] = None,
  @jsonField("master_timeout") masterTimeout: Option[String] = None,
  timeout: Option[String] = None
) extends ActionRequest {
  def method: Method = Method.PUT
  def urlPath = "/_cluster/settings"
  def queryArgs: Map[String, String] = {
    val queryArgs = new mutable.HashMap[String, String]()
    flatSettings.foreach { v =>
      queryArgs += "flat_settings" -> v.toString
    }
    masterTimeout.foreach { v =>
      queryArgs += "master_timeout" -> v.toString
    }
    timeout.foreach { v =>
      queryArgs += "timeout" -> v.toString
    }
    queryArgs.toMap
  }
}
object ClusterPutSettingsRequest {
  implicit val jsonDecoder: JsonDecoder[ClusterPutSettingsRequest] = DeriveJsonDecoder.gen[ClusterPutSettingsRequest]
  implicit val jsonEncoder: JsonEncoder[ClusterPutSettingsRequest] = DeriveJsonEncoder.gen[ClusterPutSettingsRequest]
}
