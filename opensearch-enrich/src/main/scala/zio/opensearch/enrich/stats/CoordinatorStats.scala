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

package zio.opensearch.enrich.stats
import zio.json._
import zio.json.ast._
final case class CoordinatorStats(
  @jsonField("executed_searches_total") executedSearchesTotal: Long,
  @jsonField("node_id") nodeId: String,
  @jsonField("queue_size") queueSize: Int,
  @jsonField("remote_requests_current") remoteRequestsCurrent: Int,
  @jsonField("remote_requests_total") remoteRequestsTotal: Long
)

object CoordinatorStats {
  implicit lazy val jsonCodec: JsonCodec[CoordinatorStats] =
    DeriveJsonCodec.gen[CoordinatorStats]
}
