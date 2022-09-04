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

package elasticsearch.requests.cluster

import scala.collection.mutable

import elasticsearch.requests.ActionRequest
import io.circe._
import io.circe.derivation.annotations._

/*
 * Returns cluster settings.
 * For more info refers to https://www.elastic.co/guide/en/elasticsearch/reference/master/cluster-update-settings.html
 *
 * @param flatSettings Return settings in flat format (default: false)
 * @param includeDefaults Whether to return all default clusters setting.
 * @param masterTimeout Explicit operation timeout for connection to master node
 * @param timeout Explicit operation timeout
 */
@JsonCodec
final case class ClusterGetSettingsRequest(
  @JsonKey("flat_settings") flatSettings: Option[Boolean] = None,
  @JsonKey("include_defaults") includeDefaults: Boolean = false,
  @JsonKey("master_timeout") masterTimeout: Option[String] = None,
  timeout: Option[String] = None
) extends ActionRequest {
  def method: String = "GET"

  def urlPath = "/_cluster/settings"

  def queryArgs: Map[String, String] = {
    //managing parameters
    val queryArgs = new mutable.HashMap[String, String]()
    flatSettings.foreach { v =>
      queryArgs += ("flat_settings" -> v.toString)
    }
    if (includeDefaults != false)
      queryArgs += ("include_defaults" -> includeDefaults.toString)
    masterTimeout.foreach { v =>
      queryArgs += ("master_timeout" -> v.toString)
    }
    timeout.foreach { v =>
      queryArgs += ("timeout" -> v.toString)
    }
    // Custom Code On
    // Custom Code Off
    queryArgs.toMap
  }

  def body: Json = Json.Null

  // Custom Code On
  // Custom Code Off

}
