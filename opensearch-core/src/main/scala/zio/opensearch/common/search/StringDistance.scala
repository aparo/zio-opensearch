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

package zio.opensearch.common.search

import zio.json._

sealed trait StringDistance

object StringDistance {

  case object internal extends StringDistance

  case object damerau_levenshtein extends StringDistance

  case object levenshtein extends StringDistance

  case object jaro_winkler extends StringDistance

  case object ngram extends StringDistance

  implicit final val decoder: JsonDecoder[StringDistance] =
    DeriveJsonDecoderEnum.gen[StringDistance]
  implicit final val encoder: JsonEncoder[StringDistance] =
    DeriveJsonEncoderEnum.gen[StringDistance]
  implicit final val codec: JsonCodec[StringDistance] =
    JsonCodec(encoder, decoder)

}
