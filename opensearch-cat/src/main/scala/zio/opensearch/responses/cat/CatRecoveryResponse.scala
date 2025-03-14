/*
 * Copyright 2019 Alberto Paro
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

package zio.opensearch.responses.cat

import zio.json._
/*
 * Returns information about index shard recoveries, both on-going completed.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cat-recovery.html
 *
 * @param activeOnly If `true`, the response only includes ongoing shard recoveries
 * @param bytes The unit in which to display byte values
 * @param detailed If `true`, the response includes detailed information about shard recoveries
 * @param format a short version of the Accept header, e.g. json, yaml
 * @param h Comma-separated list of column names to display
 * @param help Return help information
 * @param index Comma-separated list or wildcard expression of index names to limit the returned information
 * @param masterTimeout Explicit operation timeout for connection to master node
 * @param s Comma-separated list of column names or column aliases to sort by
 * @param time The unit in which to display time values
 * @param v Verbose mode. Display column headers
 */
final case class CatRecoveryResponse(_ok: Option[Boolean] = None)
object CatRecoveryResponse {
  implicit val jsonDecoder: JsonDecoder[CatRecoveryResponse] = DeriveJsonDecoder.gen[CatRecoveryResponse]
  implicit val jsonEncoder: JsonEncoder[CatRecoveryResponse] = DeriveJsonEncoder.gen[CatRecoveryResponse]
}
