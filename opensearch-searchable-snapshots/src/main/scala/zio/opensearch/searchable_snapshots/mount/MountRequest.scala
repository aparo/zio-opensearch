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

package zio.opensearch.searchable_snapshots.mount
import scala.collection.mutable
import zio._
import zio.opensearch.common._
import zio.opensearch.searchable_snapshots.requests.MountRequestBody
/*
 * Mount a snapshot as a searchable index.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/searchable-snapshots-api-mount-snapshot.html
 *
 * @param repository The name of the repository containing the snapshot of the index to mount
 * @param snapshot The name of the snapshot of the index to mount
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

 * @param masterTimeout Explicit operation timeout for connection to master node
 * @param storage Selects the kind of local storage used to accelerate searches. Experimental, and defaults to `full_copy`
 * @param waitForCompletion Should this request wait until the operation has completed before returning
 */

final case class MountRequest(
  repository: String,
  snapshot: String,
  body: MountRequestBody,
  errorTrace: Boolean = false,
  filterPath: Chunk[String] = Chunk.empty[String],
  human: Boolean = false,
  pretty: Boolean = false,
  masterTimeout: Option[String] = None,
  storage: String = "false",
  waitForCompletion: Boolean = false
) extends ActionRequest[MountRequestBody]
    with RequestBase {
  def method: Method = Method.POST

  def urlPath: String =
    this.makeUrl("_snapshot", repository, snapshot, "_mount")

  def queryArgs: Map[String, String] = {
    // managing parameters
    val queryArgs = new mutable.HashMap[String, String]()
    masterTimeout.foreach { v =>
      queryArgs += ("master_timeout" -> v.toString)
    }
    if (storage != "false") queryArgs += ("storage" -> storage)
    if (waitForCompletion != false)
      queryArgs += ("wait_for_completion" -> waitForCompletion.toString)
    // Custom Code On
    // Custom Code Off
    queryArgs.toMap
  }

  // Custom Code On
  // Custom Code Off

}
