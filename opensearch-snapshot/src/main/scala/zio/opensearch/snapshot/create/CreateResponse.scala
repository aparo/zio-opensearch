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

package zio.opensearch.snapshot.create
import zio.opensearch.snapshot.SnapshotInfo
import zio.json._
import zio.json.ast._
/*
 * Creates a snapshot in a repository.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/modules-snapshots.html
 *
 * @param accepted Equals `true` if the snapshot was accepted. Present when the request had `wait_for_completion` set to `false`
 * @since 7.15.0

 * @param snapshot Snapshot information. Present when the request had `wait_for_completion` set to `true`

 */
final case class CreateResponse(
  accepted: Boolean = true,
  snapshot: SnapshotInfo
) {}
object CreateResponse {
  implicit lazy val jsonCodec: JsonCodec[CreateResponse] =
    DeriveJsonCodec.gen[CreateResponse]
}
