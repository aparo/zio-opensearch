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

package zio.opensearch.ml.update_model_snapshot
import zio.opensearch.ml.ModelSnapshot
import zio.json._
import zio.json.ast._
/*
 * Updates certain properties of a snapshot.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/ml-update-snapshot.html
 *
 * @param acknowledged

 * @param model

 */
final case class UpdateModelSnapshotResponse(
  acknowledged: Boolean = true,
  model: ModelSnapshot
) {}
object UpdateModelSnapshotResponse {
  implicit lazy val jsonCodec: JsonCodec[UpdateModelSnapshotResponse] =
    DeriveJsonCodec.gen[UpdateModelSnapshotResponse]
}
