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

package zio.opensearch.security.update_user_profile_data
import scala.collection.mutable
import zio._
import zio.opensearch.common._
import zio.opensearch.security.requests.UpdateUserProfileDataRequestBody
/*
 * Update application specific data for the user profile of the given unique ID.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/security-api-update-user-profile-data.html
 *
 * @param uid An unique identifier of the user profile
 * @param body body the body of the call
 * @param errorTrace When set to `true` OpenSearch will include the full stack trace of errors
 * when they occur.
 * @server_default false

 * @param filterPath Comma-separated list of filters in dot notation which reduce the response
 * returned by OpenSearch.

 * @param human When set to `true` will return statistics in a format suitable for humans.
 * For example `"exists_time": "1h"` for humans and
 * `"eixsts_time_in_millis": 3600000` for computers. When disabled the human
 * readable values will be omitted. This makes sense for responses being consumed
 * only by machines.
 * @server_default false

 * @param pretty If set to `true` the returned JSON will be "pretty-formatted". Only use
 * this option for debugging only.
 * @server_default false

 * @param ifPrimaryTerm only perform the update operation if the last operation that has changed the document has the specified primary term
 * @param ifSeqNo only perform the update operation if the last operation that has changed the document has the specified sequence number
 * @param refresh If `true` (the default) then refresh the affected shards to make this operation visible to search, if `wait_for` then wait for a refresh to make this operation visible to search, if `false` then do nothing with refreshes.
 */

final case class UpdateUserProfileDataRequest(
  uid: String,
  body: UpdateUserProfileDataRequestBody,
  errorTrace: Boolean = false,
  filterPath: Chunk[String] = Chunk.empty[String],
  human: Boolean = false,
  pretty: Boolean = false,
  ifPrimaryTerm: Option[Double] = None,
  ifSeqNo: Option[Double] = None,
  refresh: Option[Refresh] = None
) extends ActionRequest[UpdateUserProfileDataRequestBody]
    with RequestBase {
  def method: Method = Method.PUT

  def urlPath: String = this.makeUrl("_security", "profile", uid, "_data")

  def queryArgs: Map[String, String] = {
    // managing parameters
    val queryArgs = new mutable.HashMap[String, String]()
    ifPrimaryTerm.foreach { v =>
      queryArgs += ("if_primary_term" -> v.toString)
    }
    ifSeqNo.foreach { v =>
      queryArgs += ("if_seq_no" -> v.toString)
    }
    refresh.foreach { v =>
      queryArgs += ("refresh" -> v.toString)
    }
    // Custom Code On
    // Custom Code Off
    queryArgs.toMap
  }

  // Custom Code On
  // Custom Code Off

}
