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

package zio.opensearch.responses.cluster

import zio.json._
/*
 * Returns a list of any cluster-level changes (e.g. create index, update mapping,
allocate or fail shard) which have not yet been executed.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-pending.html
 *
 * @param local Return local information, do not retrieve the state from master node (default: false)
 * @param masterTimeout Specify timeout for connection to master
 */
final case class ClusterPendingTasksResponse(_ok: Option[Boolean] = None)
object ClusterPendingTasksResponse {
  implicit val jsonDecoder: JsonDecoder[ClusterPendingTasksResponse] =
    DeriveJsonDecoder.gen[ClusterPendingTasksResponse]
  implicit val jsonEncoder: JsonEncoder[ClusterPendingTasksResponse] =
    DeriveJsonEncoder.gen[ClusterPendingTasksResponse]
}
