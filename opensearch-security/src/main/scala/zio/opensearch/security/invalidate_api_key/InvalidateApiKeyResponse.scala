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

package zio.opensearch.security.invalidate_api_key
import zio._
import zio.opensearch.common.ErrorCause
import zio.json._
import zio.json.ast._
/*
 * Invalidates one or more API keys.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/security-api-invalidate-api-key.html
 *
 * @param errorCount

 * @param errorDetails

 * @param invalidatedApiKeys

 * @param previouslyInvalidatedApiKeys

 */
final case class InvalidateApiKeyResponse(
  errorCount: Int,
  errorDetails: Chunk[ErrorCause] = Chunk.empty[ErrorCause],
  invalidatedApiKeys: Chunk[String] = Chunk.empty[String],
  previouslyInvalidatedApiKeys: Chunk[String] = Chunk.empty[String]
) {}
object InvalidateApiKeyResponse {
  implicit lazy val jsonCodec: JsonCodec[InvalidateApiKeyResponse] =
    DeriveJsonCodec.gen[InvalidateApiKeyResponse]
}
