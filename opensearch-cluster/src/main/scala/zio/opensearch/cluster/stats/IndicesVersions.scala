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

package zio.opensearch.cluster.stats
import zio.json._
import zio.json.ast._
final case class IndicesVersions(
  @jsonField("index_count") indexCount: Int,
  @jsonField("primary_shard_count") primaryShardCount: Int,
  @jsonField("total_primary_bytes") totalPrimaryBytes: Long,
  version: String
)

object IndicesVersions {
  implicit lazy val jsonCodec: JsonCodec[IndicesVersions] =
    DeriveJsonCodec.gen[IndicesVersions]
}
