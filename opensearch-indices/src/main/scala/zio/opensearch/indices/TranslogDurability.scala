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

package zio.opensearch.indices

import zio.json._

sealed trait TranslogDurability

object TranslogDurability {

  case object request extends TranslogDurability

  case object REQUEST extends TranslogDurability

  case object async extends TranslogDurability

  case object ASYNC extends TranslogDurability

  implicit final val decoder: JsonDecoder[TranslogDurability] =
    DeriveJsonDecoderEnum.gen[TranslogDurability]
  implicit final val encoder: JsonEncoder[TranslogDurability] =
    DeriveJsonEncoderEnum.gen[TranslogDurability]
  implicit final val codec: JsonCodec[TranslogDurability] =
    JsonCodec(encoder, decoder)

}
