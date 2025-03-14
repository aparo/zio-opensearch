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

package zio.opensearch.indices.forcemerge
import zio.json._
import zio.json.ast._
/*
 * Performs the force merge operation on one or more indices.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-forcemerge.html
 *
 * @param task task contains a task id returned when wait_for_completion=false,
 * you can use the task_id to get the status of the task at _tasks/<task_id>

 */
final case class ForcemergeResponse(task: String) {}
object ForcemergeResponse {
  implicit lazy val jsonCodec: JsonCodec[ForcemergeResponse] =
    DeriveJsonCodec.gen[ForcemergeResponse]
}
