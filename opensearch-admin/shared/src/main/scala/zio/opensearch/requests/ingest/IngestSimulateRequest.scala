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

import scala.collection.mutable

import zio.opensearch.requests.ActionRequest
import zio.json.ast.Json
import zio.json._
import zio.json.ast._

/*
 * Allows to simulate a pipeline with example documents.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/simulate-pipeline-api.html
 *
 * @param body body the body of the call
 * @param id Pipeline ID
 * @param verbose Verbose mode. Display data output for each processor in executed pipeline
 */
final case class IngestSimulateRequest(body: Json.Obj, id: Option[String] = None, verbose: Boolean = false)
    extends ActionRequest {
  def method: Method = Method.GET
  def urlPath: String = this.makeUrl("_ingest", "pipeline", id, "_simulate")
  def queryArgs: Map[String, String] = {
    val queryArgs = new mutable.HashMap[String, String]()
    if (verbose != false) queryArgs += "verbose" -> verbose.toString
    queryArgs.toMap
  }
}
object IngestSimulateRequest {
  implicit val jsonDecoder: JsonDecoder[IngestSimulateRequest] = DeriveJsonDecoder.gen[IngestSimulateRequest]
  implicit val jsonEncoder: JsonEncoder[IngestSimulateRequest] = DeriveJsonEncoder.gen[IngestSimulateRequest]
}
