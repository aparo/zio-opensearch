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

package zio.opensearch

import zio.exception.FrameworkException
import zio.opensearch.OpenSearchService
import zio.opensearch.client.IngestActionResolver
import zio.opensearch.requests.ingest._
import zio.opensearch.responses.ingest._
import zio.json.ast.Json
import zio.json._
import zio._

trait IngestService extends IngestActionResolver {

  /*
   * Deletes a pipeline.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/delete-pipeline-api.html
   *
   * @param id Pipeline ID
   * @param masterTimeout Explicit operation timeout for connection to master node
   * @param timeout Explicit operation timeout
   */
  def deletePipeline(
    id: String,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None
  ): ZIO[Any, FrameworkException, IngestDeletePipelineResponse] = {
    val request = IngestDeletePipelineRequest(
      id = id,
      masterTimeout = masterTimeout,
      timeout = timeout
    )

    deletePipeline(request)

  }

  def deletePipeline(
    request: IngestDeletePipelineRequest
  ): ZIO[Any, FrameworkException, IngestDeletePipelineResponse] = execute(request)

  /*
   * Returns a pipeline.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/get-pipeline-api.html
   *
   * @param id Comma separated list of pipeline ids. Wildcards supported
   * @param masterTimeout Explicit operation timeout for connection to master node
   */
  def getPipeline(
    id: Option[String] = None,
    masterTimeout: Option[String] = None
  ): ZIO[Any, FrameworkException, IngestGetPipelineResponse] = {
    val request =
      IngestGetPipelineRequest(id = id, masterTimeout = masterTimeout)

    getPipeline(request)

  }

  def getPipeline(
    request: IngestGetPipelineRequest
  ): ZIO[Any, FrameworkException, IngestGetPipelineResponse] = execute(request)

  /*
   * Returns a list of the built-in patterns.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/grok-processor.html#grok-processor-rest-get
   *

   */
  def processorGrok(
    ): ZIO[Any, FrameworkException, IngestProcessorGrokResponse] = {
    val request = IngestProcessorGrokRequest()

    processorGrok(request)

  }

  def processorGrok(
    request: IngestProcessorGrokRequest
  ): ZIO[Any, FrameworkException, IngestProcessorGrokResponse] = execute(request)

  /*
   * Creates or updates a pipeline.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/put-pipeline-api.html
   *
   * @param id Pipeline ID
   * @param body body the body of the call
   * @param masterTimeout Explicit operation timeout for connection to master node
   * @param timeout Explicit operation timeout
   */
  def putPipeline(
    id: String,
    body: Json.Obj,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None
  ): ZIO[Any, FrameworkException, IngestPutPipelineResponse] = {
    val request = IngestPutPipelineRequest(
      id = id,
      body = body,
      masterTimeout = masterTimeout,
      timeout = timeout
    )

    putPipeline(request)

  }

  def putPipeline(
    request: IngestPutPipelineRequest
  ): ZIO[Any, FrameworkException, IngestPutPipelineResponse] = execute(request)

  /*
   * Allows to simulate a pipeline with example documents.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/simulate-pipeline-api.html
   *
   * @param body body the body of the call
   * @param id Pipeline ID
   * @param verbose Verbose mode. Display data output for each processor in executed pipeline
   */
  def simulate(
    body: Json.Obj,
    id: Option[String] = None,
    verbose: Boolean = false
  ): ZIO[Any, FrameworkException, IngestSimulateResponse] = {
    val request =
      IngestSimulateRequest(body = body, id = id, verbose = verbose)

    simulate(request)

  }

  def simulate(
    request: IngestSimulateRequest
  ): ZIO[Any, FrameworkException, IngestSimulateResponse] = execute(request)

}

object IngestService {

  // services

  private case class Live(
    baseOpenSearchService: OpenSearchService,
    httpService: OpenSearchHttpService
  ) extends IngestService

  val live: ZLayer[OpenSearchService, Nothing, IngestService] =
    ZLayer {
      for { baseOpenSearchService <- ZIO.service[OpenSearchService] } yield Live(
        baseOpenSearchService,
        baseOpenSearchService.httpService
      )
    }

  // access methods

  /*
   * Deletes a pipeline.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/delete-pipeline-api.html
   *
   * @param id Pipeline ID
   * @param masterTimeout Explicit operation timeout for connection to master node
   * @param timeout Explicit operation timeout
   */
  def deletePipeline(
    id: String,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None
  ): ZIO[IngestService, FrameworkException, IngestDeletePipelineResponse] =
    ZIO.environmentWithZIO[IngestService](
      _.get.deletePipeline(id = id, masterTimeout = masterTimeout, timeout = timeout)
    )

  def deletePipeline(
    request: IngestDeletePipelineRequest
  ): ZIO[IngestService, FrameworkException, IngestDeletePipelineResponse] =
    ZIO.environmentWithZIO[IngestService](_.get.execute(request))

  /*
   * Returns a pipeline.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/get-pipeline-api.html
   *
   * @param id Comma separated list of pipeline ids. Wildcards supported
   * @param masterTimeout Explicit operation timeout for connection to master node
   */
  def getPipeline(
    id: Option[String] = None,
    masterTimeout: Option[String] = None
  ): ZIO[IngestService, FrameworkException, IngestGetPipelineResponse] =
    ZIO.environmentWithZIO[IngestService](_.get.getPipeline(id = id, masterTimeout = masterTimeout))

  def getPipeline(
    request: IngestGetPipelineRequest
  ): ZIO[IngestService, FrameworkException, IngestGetPipelineResponse] =
    ZIO.environmentWithZIO[IngestService](_.get.execute(request))

  /*
   * Returns a list of the built-in patterns.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/grok-processor.html#grok-processor-rest-get
   *

   */
  def processorGrok(): ZIO[IngestService, FrameworkException, IngestProcessorGrokResponse] =
    ZIO.environmentWithZIO[IngestService](_.get.processorGrok())

  def processorGrok(
    request: IngestProcessorGrokRequest
  ): ZIO[IngestService, FrameworkException, IngestProcessorGrokResponse] =
    ZIO.environmentWithZIO[IngestService](_.get.execute(request))

  /*
   * Creates or updates a pipeline.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/put-pipeline-api.html
   *
   * @param id Pipeline ID
   * @param body body the body of the call
   * @param masterTimeout Explicit operation timeout for connection to master node
   * @param timeout Explicit operation timeout
   */
  def putPipeline(
    id: String,
    body: Json.Obj,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None
  ): ZIO[IngestService, FrameworkException, IngestPutPipelineResponse] =
    ZIO.environmentWithZIO[IngestService](
      _.get.putPipeline(id = id, body = body, masterTimeout = masterTimeout, timeout = timeout)
    )

  def putPipeline(
    request: IngestPutPipelineRequest
  ): ZIO[IngestService, FrameworkException, IngestPutPipelineResponse] =
    ZIO.environmentWithZIO[IngestService](_.get.execute(request))

  /*
   * Allows to simulate a pipeline with example documents.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/simulate-pipeline-api.html
   *
   * @param body body the body of the call
   * @param id Pipeline ID
   * @param verbose Verbose mode. Display data output for each processor in executed pipeline
   */
  def simulate(
    body: Json.Obj,
    id: Option[String] = None,
    verbose: Boolean = false
  ): ZIO[IngestService, FrameworkException, IngestSimulateResponse] =
    ZIO.environmentWithZIO[IngestService](_.get.simulate(body = body, id = id, verbose = verbose))

  def simulate(request: IngestSimulateRequest): ZIO[IngestService, FrameworkException, IngestSimulateResponse] =
    ZIO.environmentWithZIO[IngestService](_.get.execute(request))

}
