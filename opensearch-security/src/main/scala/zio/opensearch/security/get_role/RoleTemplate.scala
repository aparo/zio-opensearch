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

package zio.opensearch.security.get_role
import zio.opensearch.script.Script
import zio.json._
import zio.json.ast._
final case class RoleTemplate(
  format: Option[Json] = None,
  template: Script
)

object RoleTemplate {
  implicit lazy val jsonCodec: JsonCodec[RoleTemplate] =
    DeriveJsonCodec.gen[RoleTemplate]
}
