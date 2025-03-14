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
 * Deletes an alias.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-aliases.html
 *
 * @param indices A comma-separated list of index names (supports wildcards); use `_all` for all indices
 * @param name A comma-separated list of aliases to delete (supports wildcards); use `_all` to delete all aliases for the specified indices.
 * @param masterTimeout Specify timeout for connection to master
 * @param timeout Explicit timestamp for the document
 */
final case class IndicesDeleteAliasResponse(acknowledged: Boolean)
object IndicesDeleteAliasResponse {
  implicit val jsonDecoder: JsonDecoder[IndicesDeleteAliasResponse] = DeriveJsonDecoder.gen[IndicesDeleteAliasResponse]
  implicit val jsonEncoder: JsonEncoder[IndicesDeleteAliasResponse] = DeriveJsonEncoder.gen[IndicesDeleteAliasResponse]
}
