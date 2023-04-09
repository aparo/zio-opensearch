/*
 * Copyright 2019-2023 Alberto Paro
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

package zio.elasticsearch.searchable_snapshots.requests
import zio._
import zio.json._
import zio.json.ast._

final case class MountRequestBody(
  index: String,
  @jsonField("renamed_index") renamedIndex: Option[String] = None,
  @jsonField("index_settings") indexSettings: Option[Map[String, Json]] = None,
  @jsonField("ignore_index_settings") ignoreIndexSettings: Option[
    Chunk[String]
  ] = None
)

object MountRequestBody {
  implicit val jsonCodec: JsonCodec[MountRequestBody] =
    DeriveJsonCodec.gen[MountRequestBody]
}
