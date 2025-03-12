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
import zio._
import zio.opensearch.mappings.TypeMapping
import zio.json._
import zio.json.ast._
final case class TemplateMapping(
  aliases: Map[String, Alias],
  @jsonField("index_patterns") indexPatterns: Chunk[String]=Chunk.empty,
  mappings: TypeMapping,
  order: Option[Int]=None,
  settings: Map[String, Json]=Map.empty ,
  version: Option[Int] = None
)

object TemplateMapping {
  implicit lazy val jsonCodec: JsonCodec[TemplateMapping] =
    DeriveJsonCodec.gen[TemplateMapping]
}
