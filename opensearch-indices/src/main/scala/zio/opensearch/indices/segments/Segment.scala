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

package zio.opensearch.indices.segments
import zio.json._
import zio.json.ast._
final case class Segment(
  attributes: Map[String, String],
  committed: Boolean,
  compound: Boolean,
  @jsonField("deleted_docs") deletedDocs: Long,
  generation: Int,
  search: Boolean,
  @jsonField("size_in_bytes") sizeInBytes: Double,
  @jsonField("num_docs") numDocs: Long,
  version: String
)

object Segment {
  implicit lazy val jsonCodec: JsonCodec[Segment] = DeriveJsonCodec.gen[Segment]
}
