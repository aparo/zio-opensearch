/*
 * Copyright 2019-2023 Alberto Paro
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

package zio.elasticsearch.ml
import java.time._
import zio.json._
import zio.json.ast._
final case class TrainedModelInferenceStats(
  @jsonField("cache_miss_count") cacheMissCount: Int,
  @jsonField("failure_count") failureCount: Int,
  @jsonField("inference_count") inferenceCount: Int,
  @jsonField("missing_all_fields_count") missingAllFieldsCount: Int,
  timestamp: LocalDateTime
)

object TrainedModelInferenceStats {
  implicit val jsonCodec: JsonCodec[TrainedModelInferenceStats] =
    DeriveJsonCodec.gen[TrainedModelInferenceStats]
}
