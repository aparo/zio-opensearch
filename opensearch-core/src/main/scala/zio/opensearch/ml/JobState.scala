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

sealed trait JobState

object JobState {

  case object closing extends JobState

  case object closed extends JobState

  case object opened extends JobState

  case object failed extends JobState

  case object opening extends JobState

  implicit final val decoder: JsonDecoder[JobState] =
    DeriveJsonDecoderEnum.gen[JobState]
  implicit final val encoder: JsonEncoder[JobState] =
    DeriveJsonEncoderEnum.gen[JobState]
  implicit final val codec: JsonCodec[JobState] = JsonCodec(encoder, decoder)

}
