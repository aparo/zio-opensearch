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

package zio.opensearch.common.reindex
import zio._
import zio.opensearch.common.{ RuntimeFields, SlicedScroll }
import zio.opensearch.queries.Query
import zio.opensearch.sort.Sort.Sort
import zio.json._
import zio.json.ast._
final case class Source(
  index: Chunk[String],
  query: Option[Query] = None,
  remote: Option[RemoteSource] = None,
  size: Option[Int] = None,
  slice: Option[SlicedScroll] = None,
  sort: Option[Sort] = None,
  @jsonField("_source") source: Option[Chunk[String]] = None,
  @jsonField("runtime_mappings") runtimeMappings: Option[RuntimeFields] = None
)

object Source {
  implicit lazy val jsonCodec: JsonCodec[Source] = DeriveJsonCodec.gen[Source]
}
