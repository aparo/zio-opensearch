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

package zio.opensearch.common.query

import zio.json._

sealed trait MultiValueMode

object MultiValueMode {

  case object min extends MultiValueMode

  case object max extends MultiValueMode

  case object avg extends MultiValueMode

  case object sum extends MultiValueMode

  implicit final val decoder: JsonDecoder[MultiValueMode] =
    DeriveJsonDecoderEnum.gen[MultiValueMode]
  implicit final val encoder: JsonEncoder[MultiValueMode] =
    DeriveJsonEncoderEnum.gen[MultiValueMode]
  implicit final val codec: JsonCodec[MultiValueMode] =
    JsonCodec(encoder, decoder)

}
