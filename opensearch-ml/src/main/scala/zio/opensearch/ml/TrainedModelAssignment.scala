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

package zio.opensearch.ml
import java.time._
import zio.json._
import zio.json.ast._
final case class TrainedModelAssignment(
  @jsonField("assignment_state") assignmentState: DeploymentAssignmentState,
  @jsonField("max_assigned_allocations") maxAssignedAllocations: Option[Int] = None,
  @jsonField("routing_table") routingTable: Map[
    String,
    TrainedModelAssignmentRoutingTable
  ],
  @jsonField("start_time") startTime: LocalDateTime,
  @jsonField(
    "task_parameters"
  ) taskParameters: TrainedModelAssignmentTaskParameters
)

object TrainedModelAssignment {
  implicit lazy val jsonCodec: JsonCodec[TrainedModelAssignment] =
    DeriveJsonCodec.gen[TrainedModelAssignment]
}
