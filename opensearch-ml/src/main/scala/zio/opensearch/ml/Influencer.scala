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
import zio.json._
import zio.json.ast._
final case class Influencer(
  @jsonField("bucket_span") bucketSpan: Long,
  @jsonField("influencer_score") influencerScore: Double,
  @jsonField("influencer_field_name") influencerFieldName: String,
  @jsonField("influencer_field_value") influencerFieldValue: String,
  @jsonField("initial_influencer_score") initialInfluencerScore: Double,
  @jsonField("is_interim") isInterim: Boolean,
  @jsonField("job_id") jobId: String,
  probability: Double,
  @jsonField("result_type") resultType: String,
  timestamp: Long,
  foo: Option[String] = None
)

object Influencer {
  implicit lazy val jsonCodec: JsonCodec[Influencer] =
    DeriveJsonCodec.gen[Influencer]
}
