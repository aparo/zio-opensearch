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

package zio.opensearch.responses.cluster

import zio.json._

/*
 * http://www.elastic.co/guide/en/opensearch/reference/master/cluster-update-settings.html
 *
 * @param body body the body of the call
 * @param flatSettings Return settings in flat format (default: false)
 * @param masterTimeout Explicit operation timeout for connection to master node
 * @param timeout Explicit operation timeout
 */
final case class ClusterUpdateSettingsResponse(_ok: Option[Boolean] = None)
object ClusterUpdateSettingsResponse {
  implicit val jsonDecoder: JsonDecoder[ClusterUpdateSettingsResponse] =
    DeriveJsonDecoder.gen[ClusterUpdateSettingsResponse]
  implicit val jsonEncoder: JsonEncoder[ClusterUpdateSettingsResponse] =
    DeriveJsonEncoder.gen[ClusterUpdateSettingsResponse]
}
