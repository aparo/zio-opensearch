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

package zio.opensearch.responses.snapshot

import zio.json._
/*
 * Creates a snapshot in a repository.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/modules-snapshots.html
 *
 * @param repository A repository name
 * @param snapshot A snapshot name
 * @param body body the body of the call
 * @param masterTimeout Explicit operation timeout for connection to master node
 * @param waitForCompletion Should this request wait until the operation has completed before returning
 */
final case class SnapshotCreateResponse(_ok: Option[Boolean] = None)
object SnapshotCreateResponse {
  implicit val jsonDecoder: JsonDecoder[SnapshotCreateResponse] = DeriveJsonDecoder.gen[SnapshotCreateResponse]
  implicit val jsonEncoder: JsonEncoder[SnapshotCreateResponse] = DeriveJsonEncoder.gen[SnapshotCreateResponse]
}
