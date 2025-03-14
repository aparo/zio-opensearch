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

package zio.opensearch.responses.cluster

import zio.opensearch.Settings
import zio.opensearch.mappings.RootDocumentMapping
import zio.json.ast._
import zio.json._

final case class IndexTemplate(
  @jsonField("index_patterns") indexPatterns: Chunk[String],
  aliases: Map[String, Json] = Map.empty[String, Json],
  settings: Settings = Settings.OpenSearchBase,
  mappings: Map[String, RootDocumentMapping] = Map.empty[String, RootDocumentMapping],
  order: Int = 1000
)
object IndexTemplate {
  implicit val jsonDecoder: JsonDecoder[IndexTemplate] = DeriveJsonDecoder.gen[IndexTemplate]
  implicit val jsonEncoder: JsonEncoder[IndexTemplate] = DeriveJsonEncoder.gen[IndexTemplate]
}
