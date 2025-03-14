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
final case class DataframeAnalysisFeatureProcessor(
  @jsonField("frequency_encoding") frequencyEncoding: Option[
    DataframeAnalysisFeatureProcessorFrequencyEncoding
  ] = None,
  @jsonField("multi_encoding") multiEncoding: Option[
    DataframeAnalysisFeatureProcessorMultiEncoding
  ] = None,
  @jsonField("n_gram_encoding") nGramEncoding: Option[
    DataframeAnalysisFeatureProcessorNGramEncoding
  ] = None,
  @jsonField("one_hot_encoding") oneHotEncoding: Option[
    DataframeAnalysisFeatureProcessorOneHotEncoding
  ] = None,
  @jsonField("target_mean_encoding") targetMeanEncoding: Option[
    DataframeAnalysisFeatureProcessorTargetMeanEncoding
  ] = None
)

object DataframeAnalysisFeatureProcessor {
  implicit lazy val jsonCodec: JsonCodec[DataframeAnalysisFeatureProcessor] =
    DeriveJsonCodec.gen[DataframeAnalysisFeatureProcessor]
}
