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

package zio.opensearch.security.authenticate
import zio._
import zio.opensearch.common.{ Metadata, Username }
import zio.opensearch.security._
import zio.json._
import zio.json.ast._
/*
 * Enables authentication as a user and retrieve information about the authenticated user.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/security-api-authenticate.html
 *
 * @param apiKey

 * @param authenticationRealm

 * @param email

 * @param fullName

 * @param lookupRealm

 * @param metadata

 * @param roles

 * @param username

 * @param enabled

 * @param authenticationType

 * @param token
@since 7.14.0

 */
final case class AuthenticateResponse(
  apiKey: ApiKey,
  authenticationRealm: RealmInfo,
  email: String,
  fullName: String,
  lookupRealm: RealmInfo,
  metadata: Metadata,
  roles: Chunk[String] = Chunk.empty[String],
  username: Username,
  enabled: Boolean = true,
  authenticationType: String,
  token: Token
) {}
object AuthenticateResponse {
  implicit lazy val jsonCodec: JsonCodec[AuthenticateResponse] =
    DeriveJsonCodec.gen[AuthenticateResponse]
}
