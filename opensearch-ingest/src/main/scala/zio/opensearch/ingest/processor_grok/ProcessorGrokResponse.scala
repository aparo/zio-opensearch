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

package zio.opensearch.ingest.processor_grok
import zio.json._
import zio.json.ast._
/*
 * Returns a list of the built-in patterns.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/grok-processor.html#grok-processor-rest-get
 *
 * @param patterns

 */
final case class ProcessorGrokResponse(
  patterns: Map[String, String] = Map.empty[String, String]
) {}
object ProcessorGrokResponse {
  implicit lazy val jsonCodec: JsonCodec[ProcessorGrokResponse] =
    DeriveJsonCodec.gen[ProcessorGrokResponse]
}
