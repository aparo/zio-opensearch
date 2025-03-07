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

package zio.opensearch.security.requests
import zio.opensearch.common.{ Password, Username }
import zio.opensearch.security.ApiKeyGrantType
import zio.opensearch.security.grant_api_key.GrantApiKey
import zio.json._
import zio.json.ast._

final case class GrantApiKeyRequestBody(
  @jsonField("api_key") apiKey: GrantApiKey,
  @jsonField("grant_type") grantType: ApiKeyGrantType,
  @jsonField("access_token") accessToken: Option[String] = None,
  username: Option[Username] = None,
  password: Option[Password] = None,
  @jsonField("run_as") runAs: Option[Username] = None
)

object GrantApiKeyRequestBody {
  implicit lazy val jsonCodec: JsonCodec[GrantApiKeyRequestBody] =
    DeriveJsonCodec.gen[GrantApiKeyRequestBody]
}
