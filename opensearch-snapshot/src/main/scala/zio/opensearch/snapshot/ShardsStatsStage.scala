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

package zio.opensearch.snapshot

import zio.json._

sealed trait ShardsStatsStage

object ShardsStatsStage {

  case object DONE extends ShardsStatsStage

  case object FAILURE extends ShardsStatsStage

  case object FINALIZE extends ShardsStatsStage

  case object INIT extends ShardsStatsStage

  case object STARTED extends ShardsStatsStage

  implicit final val decoder: JsonDecoder[ShardsStatsStage] =
    DeriveJsonDecoderEnum.gen[ShardsStatsStage]
  implicit final val encoder: JsonEncoder[ShardsStatsStage] =
    DeriveJsonEncoderEnum.gen[ShardsStatsStage]
  implicit final val codec: JsonCodec[ShardsStatsStage] =
    JsonCodec(encoder, decoder)

}
