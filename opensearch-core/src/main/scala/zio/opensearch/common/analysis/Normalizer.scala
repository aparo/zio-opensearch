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

package zio.opensearch.common.analysis

import zio.Chunk
import zio.json._

final case class Normalizer(
  `type`: String,
  filter: Chunk[String] = Chunk.empty,
  char_filter: Chunk[String] = Chunk.empty
)
object Normalizer {
  implicit val jsonDecoder: JsonDecoder[Normalizer] = DeriveJsonDecoder.gen[Normalizer]
  implicit val jsonEncoder: JsonEncoder[Normalizer] = DeriveJsonEncoder.gen[Normalizer]
}
