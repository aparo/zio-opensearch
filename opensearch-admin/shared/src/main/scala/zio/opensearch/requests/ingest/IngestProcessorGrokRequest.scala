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

package zio.opensearch.requests.ingest

import zio.opensearch.requests.ActionRequest
import zio.json.ast.Json
import zio.json._
import zio.json.ast._

/*
 * Returns a list of the built-in patterns.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/grok-processor.html#grok-processor-rest-get
 *

 */
final case class IngestProcessorGrokRequest(_ok: Option[Boolean] = None) extends ActionRequest {
  def method: Method = Method.GET
  def urlPath = "/_ingest/processor/grok"
  def queryArgs: Map[String, String] = Map.empty[String, String]
  def body: Json = Json.Null
}
object IngestProcessorGrokRequest {
  implicit val jsonDecoder: JsonDecoder[IngestProcessorGrokRequest] = DeriveJsonDecoder.gen[IngestProcessorGrokRequest]
  implicit val jsonEncoder: JsonEncoder[IngestProcessorGrokRequest] = DeriveJsonEncoder.gen[IngestProcessorGrokRequest]
}
