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

package zio.opensearch.security.has_privileges_user_profile
import zio._
import zio.opensearch.security.UserProfileId
import zio.json._
import zio.json.ast._
/*
 * Determines whether the users associated with the specified profile IDs have all the requested privileges.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/security-api-has-privileges-user-profile.html
 *
 * @param hasPrivilegeUids The subset of the requested profile IDs of the users that
 * have all the requested privileges.

 * @param errors The subset of the requested profile IDs for which an error
 * was encountered. It does not include the missing profile IDs
 * or the profile IDs of the users that do not have all the
 * requested privileges. This field is absent if empty.

 */
final case class HasPrivilegesUserProfileResponse(
  hasPrivilegeUids: Chunk[UserProfileId] = Chunk.empty[UserProfileId],
  errors: HasPrivilegesUserProfileErrors
) {}
object HasPrivilegesUserProfileResponse {
  implicit lazy val jsonCodec: JsonCodec[HasPrivilegesUserProfileResponse] =
    DeriveJsonCodec.gen[HasPrivilegesUserProfileResponse]
}
