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

package zio.opensearch.client

import zio.opensearch.ZioResponse
import zio.opensearch.requests.tasks._
import zio.opensearch.responses.tasks._

trait TasksClientActions {
  def execute(request: TasksCancelRequest): ZIO[Any, FrameworkException, TasksCancelResponse]
  def execute(request: TasksGetRequest): ZIO[Any, FrameworkException, TasksGetResponse]
  def execute(request: TasksListRequest): ZIO[Any, FrameworkException, TasksListResponse]

}
