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

package zio.opensearch.security.bulk_update_api_keys
import zio.opensearch.common._
import zio.json.ast._
/*
 * Updates the attributes of multiple existing API keys.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/security-api-bulk-update-api-keys.html
 *
 * @param body body the body of the call
 */

final case class BulkUpdateApiKeysRequest(body: Json) extends ActionRequest[Json] {
  def method: Method = Method.POST

  def urlPath = "/_security/api_key/_bulk_update"

  def queryArgs: Map[String, String] = Map.empty[String, String]

  // Custom Code On
  // Custom Code Off

}
