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

package zio.schema.opensearch

import zio.json._

@jsonEnumLowerCase sealed trait OSType extends EnumLowerCase

object OSType {
  implicit final val decoder: JsonDecoder[OSType] =
    DeriveJsonDecoderEnum.gen[OSType]
  implicit final val encoder: JsonEncoder[OSType] =
    DeriveJsonEncoderEnum.gen[OSType]
  implicit final val codec: JsonCodec[OSType] = JsonCodec(encoder, decoder)

  case object none extends OSType

  case object geo_point extends OSType
  case object geo_shape extends OSType
  case object ip extends OSType

  case object binary extends OSType

  case object keyword extends OSType

  case object text extends OSType

  case object search_as_you_type extends OSType

  case object date extends OSType

  case object date_nanos extends OSType

  case object boolean extends OSType

  case object completion extends OSType

  case object nested extends OSType

  case object `object` extends OSType

  case object murmur3 extends OSType

  case object token_count extends OSType

  case object percolator extends OSType

  case object integer extends OSType

  case object long extends OSType

  case object short extends OSType

  case object byte extends OSType

  case object float extends OSType

  case object half_float extends OSType

  case object scaled_float extends OSType

  case object double extends OSType

  case object integer_range extends OSType

  case object float_range extends OSType

  case object long_range extends OSType

  case object double_range extends OSType

  case object date_range extends OSType

  case object ip_range extends OSType

  case object alias extends OSType

  case object join extends OSType

  case object rank_feature extends OSType

  case object rank_features extends OSType

  case object flattened extends OSType

  case object shape extends OSType

  case object histogram extends OSType

  case object constant_keyword extends OSType

  case object aggregate_metric_double extends OSType

  case object dense_vector extends OSType

  case object match_only_text extends OSType
}
