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

package zio.opensearch.common.delete_by_query_rethrottle
import zio._
import zio.opensearch.common._
import zio.json.ast._
/*
 * Changes the number of requests per second for a particular Delete By Query operation.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/docs-delete-by-query.html
 *
 * @param requestsPerSecond The throttle to set on this request in floating sub-requests per second. -1 means set no throttle.
 * @param taskId The task id to rethrottle
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

 */

final case class DeleteByQueryRethrottleRequest(
  requestsPerSecond: Integer,
  taskId: String,
  errorTrace: Boolean = false,
  filterPath: Chunk[String] = Chunk.empty[String],
  human: Boolean = false,
  pretty: Boolean = false
) extends ActionRequest[Json]
    with RequestBase {
  def method: Method = Method.POST

  def urlPath: String = this.makeUrl("_delete_by_query", taskId, "_rethrottle")

  def queryArgs: Map[String, String] = Map.empty[String, String]

  def body: Json = Json.Null

  // Custom Code On
  // Custom Code Off

}
