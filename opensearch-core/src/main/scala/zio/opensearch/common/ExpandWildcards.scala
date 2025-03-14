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

package zio.opensearch.common

import zio.json.{ DeriveJsonDecoderEnum, DeriveJsonEncoderEnum, JsonCodec, JsonDecoder, JsonEncoder }

sealed trait ExpandWildcards

object ExpandWildcards {

  case object open extends ExpandWildcards

  case object closed extends ExpandWildcards

  case object none extends ExpandWildcards

  case object all extends ExpandWildcards

  implicit final val decoder: JsonDecoder[ExpandWildcards] =
    DeriveJsonDecoderEnum.gen[ExpandWildcards]
  implicit final val encoder: JsonEncoder[ExpandWildcards] =
    DeriveJsonEncoderEnum.gen[ExpandWildcards]
  implicit final val codec: JsonCodec[ExpandWildcards] = JsonCodec(encoder, decoder)
}
