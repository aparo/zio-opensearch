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

package zio.opensearch.responses.tasks

import zio.json._
/*
 * Returns information about a task.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/tasks.html
 *
 * @param taskId Return the task with specified id (node_id:task_number)
 * @param timeout Explicit operation timeout
 * @param waitForCompletion Wait for the matching tasks to complete (default: false)
 */
final case class TasksGetResponse(_ok: Option[Boolean] = None)
object TasksGetResponse {
  implicit val jsonDecoder: JsonDecoder[TasksGetResponse] = DeriveJsonDecoder.gen[TasksGetResponse]
  implicit val jsonEncoder: JsonEncoder[TasksGetResponse] = DeriveJsonEncoder.gen[TasksGetResponse]
}
