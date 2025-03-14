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
import zio._
import zio.json._
import zio.json.ast._
final case class BucketSummary(
  @jsonField("anomaly_score") anomalyScore: Double,
  @jsonField("bucket_influencers") bucketInfluencers: Chunk[BucketInfluencer],
  @jsonField("bucket_span") bucketSpan: Long,
  @jsonField("event_count") eventCount: Long,
  @jsonField("initial_anomaly_score") initialAnomalyScore: Double,
  @jsonField("is_interim") isInterim: Boolean,
  @jsonField("job_id") jobId: String,
  @jsonField("processing_time_ms") processingTimeMs: Long,
  @jsonField("result_type") resultType: String,
  timestamp: Long,
  @jsonField("timestamp_string") timestampString: Option[LocalDateTime] = None
)

object BucketSummary {
  implicit lazy val jsonCodec: JsonCodec[BucketSummary] =
    DeriveJsonCodec.gen[BucketSummary]
}
