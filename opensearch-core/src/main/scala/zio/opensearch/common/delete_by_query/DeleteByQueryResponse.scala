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

package zio.opensearch.common.delete_by_query
import zio._
import zio.opensearch.common.{ ErrorCause, TaskFailure }
import zio.opensearch.tasks.NodeTasks
import zio.json._
import zio.json.ast._
/*
 * Deletes documents matching the provided query.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/docs-delete-by-query.html
 *
 * @param nodeFailures

 * @param taskFailures

 * @param nodes Task information grouped by node, if `group_by` was set to `node` (the default).

 * @param tasks Either a flat list of tasks if `group_by` was set to `none`, or grouped by parents if
 * `group_by` was set to `parents`.

 */
final case class DeleteByQueryResponse(
  nodeFailures: Chunk[ErrorCause] = Chunk.empty[ErrorCause],
  taskFailures: Chunk[TaskFailure] = Chunk.empty[TaskFailure],
  nodes: Map[String, NodeTasks] = Map.empty[String, NodeTasks],
  tasks: Chunk[zio.opensearch.tasks.TaskInfo] = Chunk.empty
) {}
object DeleteByQueryResponse {
  implicit lazy val jsonCodec: JsonCodec[DeleteByQueryResponse] =
    DeriveJsonCodec.gen[DeleteByQueryResponse]
}
