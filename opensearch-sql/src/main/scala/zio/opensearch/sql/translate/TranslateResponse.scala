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

package zio.opensearch.sql.translate
import zio._
import zio.opensearch.aggregations.Aggregation
import zio.opensearch.common.{ FieldAndFormat, SourceConfig }
import zio.opensearch.queries.Query
import zio.opensearch.sort.Sort.Sort
import zio.json._
import zio.json.ast._
/*
 * Translates SQL into OpenSearch queries
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/sql-translate-api.html
 *
 * @param aggregations

 * @param size

 * @param source

 * @param fields

 * @param query

 * @param sort

 */
final case class TranslateResponse(
  aggregations: Map[String, Aggregation] = Map.empty[String, Aggregation],
  size: Long,
  source: SourceConfig,
  fields: Chunk[FieldAndFormat] = Chunk.empty[FieldAndFormat],
  query: Query,
  sort: Sort
) {}
object TranslateResponse {
  implicit lazy val jsonCodec: JsonCodec[TranslateResponse] =
    DeriveJsonCodec.gen[TranslateResponse]
}
