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
 * Clones an index
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-clone-index.html
 *
 * @param index The name of the source index to clone
 * @param target The name of the target index to clone into
 * @param body body the body of the call
 * @param masterTimeout Specify timeout for connection to master
 * @param timeout Explicit operation timeout
 * @param waitForActiveShards Set the number of active shards to wait for on the cloned index before the operation returns.
 */
final case class IndicesCloneResponse(_ok: Option[Boolean] = None)
object IndicesCloneResponse {
  implicit val jsonDecoder: JsonDecoder[IndicesCloneResponse] = DeriveJsonDecoder.gen[IndicesCloneResponse]
  implicit val jsonEncoder: JsonEncoder[IndicesCloneResponse] = DeriveJsonEncoder.gen[IndicesCloneResponse]
}
