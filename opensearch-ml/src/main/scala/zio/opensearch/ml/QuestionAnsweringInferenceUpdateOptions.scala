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
final case class QuestionAnsweringInferenceUpdateOptions(
  question: String,
  @jsonField("num_top_classes") numTopClasses: Option[Int] = None,
  tokenization: Option[NlpTokenizationUpdateOptions] = None,
  @jsonField("results_field") resultsField: Option[String] = None,
  @jsonField("max_answer_length") maxAnswerLength: Option[Int] = None
)

object QuestionAnsweringInferenceUpdateOptions {
  implicit lazy val jsonCodec: JsonCodec[QuestionAnsweringInferenceUpdateOptions] =
    DeriveJsonCodec.gen[QuestionAnsweringInferenceUpdateOptions]
}
