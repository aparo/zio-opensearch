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

sealed trait FunctionBoostMode

object FunctionBoostMode {

  case object multiply extends FunctionBoostMode

  case object replace extends FunctionBoostMode

  case object sum extends FunctionBoostMode

  case object avg extends FunctionBoostMode

  case object max extends FunctionBoostMode

  case object min extends FunctionBoostMode

  implicit final val decoder: JsonDecoder[FunctionBoostMode] =
    DeriveJsonDecoderEnum.gen[FunctionBoostMode]
  implicit final val encoder: JsonEncoder[FunctionBoostMode] =
    DeriveJsonEncoderEnum.gen[FunctionBoostMode]
  implicit final val codec: JsonCodec[FunctionBoostMode] =
    JsonCodec(encoder, decoder)

}
