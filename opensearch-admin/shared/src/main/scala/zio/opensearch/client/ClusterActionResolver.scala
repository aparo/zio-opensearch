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
import zio.opensearch.requests.cluster._
import zio.opensearch.responses.cluster._

trait ClusterActionResolver extends ClusterClientActions with ClientActionResolver {

  def execute(
    request: ClusterAllocationExplainRequest
  ): ZIO[Any, FrameworkException, ClusterAllocationExplainResponse] =
    doCall(request).flatMap(convertResponse[ClusterAllocationExplainResponse](request))

  def execute(
    request: ClusterGetSettingsRequest
  ): ZIO[Any, FrameworkException, ClusterGetSettingsResponse] =
    doCall(request).flatMap(convertResponse[ClusterGetSettingsResponse](request))

  def execute(
    request: ClusterHealthRequest
  ): ZIO[Any, FrameworkException, ClusterHealthResponse] =
    doCall(request).flatMap(convertResponse[ClusterHealthResponse](request))

  def execute(
    request: ClusterPendingTasksRequest
  ): ZIO[Any, FrameworkException, ClusterPendingTasksResponse] =
    doCall(request).flatMap(convertResponse[ClusterPendingTasksResponse](request))

  def execute(
    request: ClusterPutSettingsRequest
  ): ZIO[Any, FrameworkException, ClusterPutSettingsResponse] =
    doCall(request).flatMap(convertResponse[ClusterPutSettingsResponse](request))

  def execute(
    request: ClusterRemoteInfoRequest
  ): ZIO[Any, FrameworkException, ClusterRemoteInfoResponse] =
    doCall(request).flatMap(convertResponse[ClusterRemoteInfoResponse](request))

  def execute(
    request: ClusterRerouteRequest
  ): ZIO[Any, FrameworkException, ClusterRerouteResponse] =
    doCall(request).flatMap(convertResponse[ClusterRerouteResponse](request))

  def execute(
    request: ClusterStateRequest
  ): ZIO[Any, FrameworkException, ClusterStateResponse] =
    doCall(request).flatMap(convertResponse[ClusterStateResponse](request))

  def execute(
    request: ClusterStatsRequest
  ): ZIO[Any, FrameworkException, ClusterStatsResponse] =
    doCall(request).flatMap(convertResponse[ClusterStatsResponse](request))

}
