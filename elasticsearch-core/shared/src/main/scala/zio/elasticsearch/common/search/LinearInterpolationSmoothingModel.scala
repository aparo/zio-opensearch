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

package zio.elasticsearch.common.search

import zio.json._
final case class LinearInterpolationSmoothingModel(
  @jsonField("bigram_lambda") bigramLambda: Double,
  @jsonField("trigram_lambda") trigramLambda: Double,
  @jsonField("unigram_lambda") unigramLambda: Double
)

object LinearInterpolationSmoothingModel {
  implicit val jsonCodec: JsonCodec[LinearInterpolationSmoothingModel] =
    DeriveJsonCodec.gen[LinearInterpolationSmoothingModel]
}
