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
 * Returns all snapshots in a specific repository.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cat-snapshots.html
 *
 * @param format a short version of the Accept header, e.g. json, yaml
 * @param h Comma-separated list of column names to display
 * @param help Return help information
 * @param ignoreUnavailable Set to true to ignore unavailable snapshots
 * @param masterTimeout Explicit operation timeout for connection to master node
 * @param repository Name of repository from which to fetch the snapshot information
 * @param s Comma-separated list of column names or column aliases to sort by
 * @param time The unit in which to display time values
 * @param v Verbose mode. Display column headers
 */
final case class CatSnapshotsResponse(_ok: Option[Boolean] = None)
object CatSnapshotsResponse {
  implicit val jsonDecoder: JsonDecoder[CatSnapshotsResponse] = DeriveJsonDecoder.gen[CatSnapshotsResponse]
  implicit val jsonEncoder: JsonEncoder[CatSnapshotsResponse] = DeriveJsonEncoder.gen[CatSnapshotsResponse]
}
