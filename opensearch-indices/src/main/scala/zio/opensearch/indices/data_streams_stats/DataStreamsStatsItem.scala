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

package zio.opensearch.indices.data_streams_stats
import zio.json._
import zio.json.ast._
final case class DataStreamsStatsItem(
  @jsonField("backing_indices") backingIndices: Int,
  @jsonField("data_stream") dataStream: String,
  @jsonField("store_size") storeSize: Option[String] = None,
  @jsonField("store_size_bytes") storeSizeBytes: Int,
  @jsonField("maximum_timestamp") maximumTimestamp: Long
)

object DataStreamsStatsItem {
  implicit lazy val jsonCodec: JsonCodec[DataStreamsStatsItem] =
    DeriveJsonCodec.gen[DataStreamsStatsItem]
}
