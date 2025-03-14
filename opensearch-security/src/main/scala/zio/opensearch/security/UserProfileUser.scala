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

package zio.opensearch.security
import zio._
import zio.opensearch.common.Username
import zio.json._
import zio.json.ast._
final case class UserProfileUser(
  email: Option[String] = None,
  @jsonField("full_name") fullName: Option[String] = None,
  @jsonField("realm_name") realmName: String,
  @jsonField("realm_domain") realmDomain: Option[String] = None,
  roles: Chunk[String],
  username: Username
)

object UserProfileUser {
  implicit lazy val jsonCodec: JsonCodec[UserProfileUser] =
    DeriveJsonCodec.gen[UserProfileUser]
}
