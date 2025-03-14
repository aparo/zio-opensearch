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

sealed trait MvtGridAggregationType

object MvtGridAggregationType {

  case object geotile extends MvtGridAggregationType

  case object geohex extends MvtGridAggregationType

  implicit final val decoder: JsonDecoder[MvtGridAggregationType] =
    DeriveJsonDecoderEnum.gen[MvtGridAggregationType]
  implicit final val encoder: JsonEncoder[MvtGridAggregationType] =
    DeriveJsonEncoderEnum.gen[MvtGridAggregationType]
  implicit final val codec: JsonCodec[MvtGridAggregationType] =
    JsonCodec(encoder, decoder)

}
