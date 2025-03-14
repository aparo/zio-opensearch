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

package zio.opensearch.cat.segments
import zio.json._
import zio.json.ast._
final case class SegmentsRecord(
  index: Option[String] = None,
  shard: Option[String] = None,
  prirep: Option[String] = None,
  ip: Option[String] = None,
  id: Option[String] = None,
  segment: Option[String] = None,
  generation: Option[String] = None,
  @jsonField("docs.count") `docs.count`: Option[String] = None,
  @jsonField("docs.deleted") `docs.deleted`: Option[String] = None,
  size: Option[String] = None,
  @jsonField("size.memory") `size.memory`: Option[String] = None,
  committed: Option[String] = None,
  searchable: Option[String] = None,
  version: Option[String] = None,
  compound: Option[String] = None
)

object SegmentsRecord {
  implicit lazy val jsonCodec: JsonCodec[SegmentsRecord] =
    DeriveJsonCodec.gen[SegmentsRecord]
}
