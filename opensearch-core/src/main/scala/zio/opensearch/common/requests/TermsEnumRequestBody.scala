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

package zio.opensearch.common.requests
import zio.opensearch.queries.Query
import zio.json._
import zio.json.ast._

final case class TermsEnumRequestBody(
  field: String,
  size: Option[Int] = None,
  timeout: Option[String] = None,
  @jsonField("case_insensitive") caseInsensitive: Option[Boolean] = None,
  @jsonField("index_filter") indexFilter: Option[Query] = None,
  string: Option[String] = None,
  @jsonField("search_after") searchAfter: Option[String] = None
)

object TermsEnumRequestBody {
  implicit lazy val jsonCodec: JsonCodec[TermsEnumRequestBody] =
    DeriveJsonCodec.gen[TermsEnumRequestBody]
}
