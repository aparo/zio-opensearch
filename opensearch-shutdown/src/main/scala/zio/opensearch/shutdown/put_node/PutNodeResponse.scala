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

package zio.opensearch.shutdown.put_node
import zio.json._
import zio.json.ast._
/*
 * Adds a node to be shut down. Designed for indirect use by ECE/ESS and ECK. Direct use is not supported.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current
 *
 * @param acknowledged For a successful response, this value is always true. On failure, an exception is returned instead.

 */
final case class PutNodeResponse(acknowledged: Boolean = true) {}
object PutNodeResponse {
  implicit lazy val jsonCodec: JsonCodec[PutNodeResponse] =
    DeriveJsonCodec.gen[PutNodeResponse]
}
