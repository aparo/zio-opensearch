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
 * http://www.elastic.co/guide/en/opensearch/reference/master/cluster-reroute.html
 *
 * @param body body the body of the call
 * @param explain Return an explanation of why the commands can or cannot be executed
 * @param dryRun Simulate the operation only and return the resulting state
 * @param metric Limit the information returned to the specified metrics. Defaults to all but metadata
 * @param retryFailed Retries allocation of shards that are blocked due to too many subsequent allocation failures
 * @param masterTimeout Explicit operation timeout for connection to master node
 * @param timeout Explicit operation timeout
 */
final case class ClusterRerouteResponse(_ok: Option[Boolean] = None)
object ClusterRerouteResponse {
  implicit val jsonDecoder: JsonDecoder[ClusterRerouteResponse] = DeriveJsonDecoder.gen[ClusterRerouteResponse]
  implicit val jsonEncoder: JsonEncoder[ClusterRerouteResponse] = DeriveJsonEncoder.gen[ClusterRerouteResponse]
}
