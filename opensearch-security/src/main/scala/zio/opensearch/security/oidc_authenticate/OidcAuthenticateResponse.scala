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

package zio.opensearch.security.oidc_authenticate
import zio.json._
import zio.json.ast._
/*
 * Exchanges an OpenID Connection authentication response message for an OpenSearch access token and refresh token pair
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/security-api-oidc-authenticate.html
 */
final case class OidcAuthenticateResponse(
  ) {}
object OidcAuthenticateResponse {
  implicit lazy val jsonCodec: JsonCodec[OidcAuthenticateResponse] =
    DeriveJsonCodec.gen[OidcAuthenticateResponse]
}
