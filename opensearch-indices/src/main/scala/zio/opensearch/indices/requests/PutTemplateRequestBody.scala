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

package zio.opensearch.indices.requests
import zio._
import zio.opensearch.indices.Alias
import zio.opensearch.mappings.TypeMapping
import zio.json._
import zio.json.ast._

final case class PutTemplateRequestBody(
  aliases: Option[Map[String, Alias]] = None,
  @jsonField("index_patterns") indexPatterns: Option[Chunk[String]] = None,
  mappings: Option[TypeMapping] = None,
  order: Option[Int] = None,
  settings: Option[Map[String, Json]] = None,
  version: Option[Int] = None
)

object PutTemplateRequestBody {
  implicit lazy val jsonCodec: JsonCodec[PutTemplateRequestBody] =
    DeriveJsonCodec.gen[PutTemplateRequestBody]
}
