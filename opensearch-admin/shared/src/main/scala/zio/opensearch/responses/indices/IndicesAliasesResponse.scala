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
 * http://www.elastic.co/guide/en/opensearch/reference/master/indices-aliases.html
 *
 * @param indices A comma-separated list of index names the alias should point to (supports wildcards); use `_all` to perform the operation on all indices.
 * @param name The name of the alias to be created or updated
 * @param body body the body of the call
 * @param timeout Explicit timestamp for the document
 * @param masterTimeout Specify timeout for connection to master
 */
final case class IndicesAliasesResponse(acknowledged: Boolean)
object IndicesAliasesResponse {
  implicit val jsonDecoder: JsonDecoder[IndicesAliasesResponse] = DeriveJsonDecoder.gen[IndicesAliasesResponse]
  implicit val jsonEncoder: JsonEncoder[IndicesAliasesResponse] = DeriveJsonEncoder.gen[IndicesAliasesResponse]
}
