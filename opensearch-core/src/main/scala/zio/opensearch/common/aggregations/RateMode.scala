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

package zio.opensearch.common.aggregations

import zio.json._

sealed trait RateMode

object RateMode {

  case object sum extends RateMode

  case object value_count extends RateMode

  implicit final val decoder: JsonDecoder[RateMode] =
    DeriveJsonDecoderEnum.gen[RateMode]
  implicit final val encoder: JsonEncoder[RateMode] =
    DeriveJsonEncoderEnum.gen[RateMode]
  implicit final val codec: JsonCodec[RateMode] = JsonCodec(encoder, decoder)

}
