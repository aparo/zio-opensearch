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

package fixures.models

import zio.opensearch.geo.GeoPointLatLon
import zio.json._
import zio.schema.opensearch.OpenSearchSchema
import zio.schema.opensearch.annotations.{ Keyword, PK }
import zio.schema.{ DeriveSchema, Schema }
final case class GeoModel(@PK @Keyword username: String, geoPoint: GeoPointLatLon)

object GeoModel {
  implicit final val decoder: JsonDecoder[GeoModel] =
    DeriveJsonDecoder.gen[GeoModel]
  implicit final val encoder: JsonEncoder[GeoModel] =
    DeriveJsonEncoder.gen[GeoModel]
  implicit final val codec: JsonCodec[GeoModel] = JsonCodec(encoder, decoder)
  implicit val schema: Schema[GeoModel] = DeriveSchema.gen[GeoModel]
  implicit val osSchema: OpenSearchSchema[GeoModel] = OpenSearchSchema.gen[GeoModel]

}
