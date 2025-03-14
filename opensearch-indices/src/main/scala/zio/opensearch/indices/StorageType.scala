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

sealed trait StorageType

object StorageType {

  case object fs extends StorageType

  case object niofs extends StorageType

  case object mmapfs extends StorageType

  case object hybridfs extends StorageType

  case object trin extends StorageType

  implicit final val decoder: JsonDecoder[StorageType] =
    DeriveJsonDecoderEnum.gen[StorageType]
  implicit final val encoder: JsonEncoder[StorageType] =
    DeriveJsonEncoderEnum.gen[StorageType]
  implicit final val codec: JsonCodec[StorageType] = JsonCodec(encoder, decoder)

}
