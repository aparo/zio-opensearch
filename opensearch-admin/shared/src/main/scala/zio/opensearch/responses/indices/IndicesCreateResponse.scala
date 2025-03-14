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
 * Creates an index with optional settings and mappings.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-create-index.html
 *
 * @param index The name of the index
 * @param body body the body of the call
 * @param includeTypeName Whether a type should be expected in the body of the mappings.
 * @param masterTimeout Specify timeout for connection to master
 * @param timeout Explicit operation timeout
 * @param waitForActiveShards Set the number of active shards to wait for before the operation returns.
 */
final case class IndicesCreateResponse(acknowledged: Boolean)
object IndicesCreateResponse {
  implicit val jsonDecoder: JsonDecoder[IndicesCreateResponse] = DeriveJsonDecoder.gen[IndicesCreateResponse]
  implicit val jsonEncoder: JsonEncoder[IndicesCreateResponse] = DeriveJsonEncoder.gen[IndicesCreateResponse]
}
