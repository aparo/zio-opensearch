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
 * Updates an alias to point to a new index when the existing index
is considered to be too large or too old.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-rollover-index.html
 *
 * @param alias The name of the alias to rollover
 * @param body body the body of the call
 * @param dryRun If set to true the rollover action will only be validated but not actually performed even if a condition matches. The default is false
 * @param includeTypeName Whether a type should be included in the body of the mappings.
 * @param masterTimeout Specify timeout for connection to master
 * @param newIndex The name of the rollover index
 * @param timeout Explicit operation timeout
 * @param waitForActiveShards Set the number of active shards to wait for on the newly created rollover index before the operation returns.
 */
final case class IndicesRolloverResponse(_ok: Option[Boolean] = None)
object IndicesRolloverResponse {
  implicit val jsonDecoder: JsonDecoder[IndicesRolloverResponse] = DeriveJsonDecoder.gen[IndicesRolloverResponse]
  implicit val jsonEncoder: JsonEncoder[IndicesRolloverResponse] = DeriveJsonEncoder.gen[IndicesRolloverResponse]
}
