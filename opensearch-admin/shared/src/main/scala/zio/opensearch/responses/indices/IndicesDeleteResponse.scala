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

package zio.opensearch.responses.indices

import zio.json._
/*
 * Deletes an index.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-delete-index.html
 *
 * @param indices A comma-separated list of indices to delete; use `_all` or `*` string to delete all indices
 * @param allowNoIndices Ignore if a wildcard expression resolves to no concrete indices (default: false)
 * @param expandWildcards Whether wildcard expressions should get expanded to open or closed indices (default: open)
 * @param ignoreUnavailable Ignore unavailable indexes (default: false)
 * @param masterTimeout Specify timeout for connection to master
 * @param timeout Explicit operation timeout
 */
final case class IndicesDeleteResponse(acknowledged: Boolean)
object IndicesDeleteResponse {
  implicit val jsonDecoder: JsonDecoder[IndicesDeleteResponse] = DeriveJsonDecoder.gen[IndicesDeleteResponse]
  implicit val jsonEncoder: JsonEncoder[IndicesDeleteResponse] = DeriveJsonEncoder.gen[IndicesDeleteResponse]
}
