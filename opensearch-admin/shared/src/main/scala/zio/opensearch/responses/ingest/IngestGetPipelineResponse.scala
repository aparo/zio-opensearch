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

package zio.opensearch.responses.ingest

import zio.json._
/*
 * Returns a pipeline.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/get-pipeline-api.html
 *
 * @param id Comma separated list of pipeline ids. Wildcards supported
 * @param masterTimeout Explicit operation timeout for connection to master node
 */
final case class IngestGetPipelineResponse(_ok: Option[Boolean] = None)
object IngestGetPipelineResponse {
  implicit val jsonDecoder: JsonDecoder[IngestGetPipelineResponse] = DeriveJsonDecoder.gen[IngestGetPipelineResponse]
  implicit val jsonEncoder: JsonEncoder[IngestGetPipelineResponse] = DeriveJsonEncoder.gen[IngestGetPipelineResponse]
}
