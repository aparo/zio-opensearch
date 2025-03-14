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

sealed trait SnapshotSort

object SnapshotSort {

  case object start_time extends SnapshotSort

  case object duration extends SnapshotSort

  case object name extends SnapshotSort

  case object index_count extends SnapshotSort

  case object repository extends SnapshotSort

  case object shard_count extends SnapshotSort

  case object failed_shard_count extends SnapshotSort

  implicit final val decoder: JsonDecoder[SnapshotSort] =
    DeriveJsonDecoderEnum.gen[SnapshotSort]
  implicit final val encoder: JsonEncoder[SnapshotSort] =
    DeriveJsonEncoderEnum.gen[SnapshotSort]
  implicit final val codec: JsonCodec[SnapshotSort] =
    JsonCodec(encoder, decoder)

}
