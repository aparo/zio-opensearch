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

package zio.opensearch.common.rank_eval
import zio.json._
import zio.json.ast._
final case class UnratedDocument(
  @jsonField("_id") id: String,
  @jsonField("_index") index: String
)

object UnratedDocument {
  implicit lazy val jsonCodec: JsonCodec[UnratedDocument] =
    DeriveJsonCodec.gen[UnratedDocument]
}
