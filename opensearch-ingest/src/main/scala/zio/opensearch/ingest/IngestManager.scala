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

package zio.opensearch.ingest

import zio.opensearch._
import zio.json.ast._
import zio._
import zio.exception._
import zio.opensearch.ingest.delete_pipeline.DeletePipelineRequest
import zio.opensearch.ingest.delete_pipeline.DeletePipelineResponse
import zio.opensearch.ingest.geo_ip_stats.GeoIpStatsRequest
import zio.opensearch.ingest.geo_ip_stats.GeoIpStatsResponse
import zio.opensearch.ingest.get_pipeline.GetPipelineRequest
import zio.opensearch.ingest.get_pipeline.GetPipelineResponse
import zio.opensearch.ingest.processor_grok.ProcessorGrokRequest
import zio.opensearch.ingest.processor_grok.ProcessorGrokResponse
import zio.opensearch.ingest.put_pipeline.PutPipelineRequest
import zio.opensearch.ingest.put_pipeline.PutPipelineResponse
import zio.opensearch.ingest.requests.SimulateRequestBody
import zio.opensearch.ingest.simulate.SimulateRequest
import zio.opensearch.ingest.simulate.SimulateResponse

object IngestManager {
  lazy val live: ZLayer[OpenSearchHttpService, Nothing, IngestManager] =
    ZLayer {
      for {
        httpServiceBase <- ZIO.service[OpenSearchHttpService]
      } yield new IngestManager {
        override def httpService: OpenSearchHttpService = httpServiceBase
      }
    }

}

trait IngestManager {
  def httpService: OpenSearchHttpService
  /*
   * Deletes a pipeline.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/delete-pipeline-api.html
   *
   * @param id Pipeline ID
   * @param errorTrace When set to `true` OpenSearch will include the full stack trace of errors
   * when they occur.
   * @server_default false

   * @param filterPath Comma-separated list of filters in dot notation which reduce the response
   * returned by OpenSearch.

   * @param human When set to `true` will return statistics in a format suitable for humans.
   * For example `"exists_time": "1h"` for humans and
   * `"eixsts_time_in_millis": 3600000` for computers. When disabled the human
   * readable values will be omitted. This makes sense for responses being consumed
   * only by machines.
   * @server_default false

   * @param pretty If set to `true` the returned JSON will be "pretty-formatted". Only use
   * this option for debugging only.
   * @server_default false

   * @param masterTimeout Explicit operation timeout for connection to master node
   * @param timeout Explicit operation timeout
   */
  def deletePipeline(
    id: String,
    errorTrace: Boolean = false,
    filterPath: Chunk[String] = Chunk.empty[String],
    human: Boolean = false,
    pretty: Boolean = false,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None
  ): ZIO[Any, FrameworkException, DeletePipelineResponse] = {
    val request = DeletePipelineRequest(
      id = id,
      errorTrace = errorTrace,
      filterPath = filterPath,
      human = human,
      pretty = pretty,
      masterTimeout = masterTimeout,
      timeout = timeout
    )

    deletePipeline(request)

  }

  def deletePipeline(
    request: DeletePipelineRequest
  ): ZIO[Any, FrameworkException, DeletePipelineResponse] =
    httpService.execute[Json, DeletePipelineResponse](request)

  /*
   * Returns statistical information about geoip databases
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/geoip-stats-api.html
   *
   * @param errorTrace When set to `true` OpenSearch will include the full stack trace of errors
   * when they occur.
   * @server_default false

   * @param filterPath Comma-separated list of filters in dot notation which reduce the response
   * returned by OpenSearch.

   * @param human When set to `true` will return statistics in a format suitable for humans.
   * For example `"exists_time": "1h"` for humans and
   * `"eixsts_time_in_millis": 3600000` for computers. When disabled the human
   * readable values will be omitted. This makes sense for responses being consumed
   * only by machines.
   * @server_default false

   * @param pretty If set to `true` the returned JSON will be "pretty-formatted". Only use
   * this option for debugging only.
   * @server_default false

   */
  def geoIpStats(
    errorTrace: Boolean = false,
    filterPath: Chunk[String] = Chunk.empty[String],
    human: Boolean = false,
    pretty: Boolean = false
  ): ZIO[Any, FrameworkException, GeoIpStatsResponse] = {
    val request = GeoIpStatsRequest(
      errorTrace = errorTrace,
      filterPath = filterPath,
      human = human,
      pretty = pretty
    )

    geoIpStats(request)

  }

  def geoIpStats(
    request: GeoIpStatsRequest
  ): ZIO[Any, FrameworkException, GeoIpStatsResponse] =
    httpService.execute[Json, GeoIpStatsResponse](request)

  /*
   * Returns a pipeline.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/get-pipeline-api.html
   *
   * @param errorTrace When set to `true` OpenSearch will include the full stack trace of errors
   * when they occur.
   * @server_default false

   * @param filterPath Comma-separated list of filters in dot notation which reduce the response
   * returned by OpenSearch.

   * @param human When set to `true` will return statistics in a format suitable for humans.
   * For example `"exists_time": "1h"` for humans and
   * `"eixsts_time_in_millis": 3600000` for computers. When disabled the human
   * readable values will be omitted. This makes sense for responses being consumed
   * only by machines.
   * @server_default false

   * @param pretty If set to `true` the returned JSON will be "pretty-formatted". Only use
   * this option for debugging only.
   * @server_default false

   * @param id Comma separated list of pipeline ids. Wildcards supported
   * @param masterTimeout Explicit operation timeout for connection to master node
   * @param summary Return pipelines without their definitions (default: false)
   */
  def getPipeline(
    errorTrace: Boolean = false,
    filterPath: Chunk[String] = Chunk.empty[String],
    human: Boolean = false,
    pretty: Boolean = false,
    id: Option[String] = None,
    masterTimeout: Option[String] = None,
    summary: Option[Boolean] = None
  ): ZIO[Any, FrameworkException, GetPipelineResponse] = {
    val request = GetPipelineRequest(
      errorTrace = errorTrace,
      filterPath = filterPath,
      human = human,
      pretty = pretty,
      id = id,
      masterTimeout = masterTimeout,
      summary = summary
    )

    getPipeline(request)

  }

  def getPipeline(
    request: GetPipelineRequest
  ): ZIO[Any, FrameworkException, GetPipelineResponse] =
    httpService.execute[Json, GetPipelineResponse](request)

  /*
   * Returns a list of the built-in patterns.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/grok-processor.html#grok-processor-rest-get
   *
   * @param errorTrace When set to `true` OpenSearch will include the full stack trace of errors
   * when they occur.
   * @server_default false

   * @param filterPath Comma-separated list of filters in dot notation which reduce the response
   * returned by OpenSearch.

   * @param human When set to `true` will return statistics in a format suitable for humans.
   * For example `"exists_time": "1h"` for humans and
   * `"eixsts_time_in_millis": 3600000` for computers. When disabled the human
   * readable values will be omitted. This makes sense for responses being consumed
   * only by machines.
   * @server_default false

   * @param pretty If set to `true` the returned JSON will be "pretty-formatted". Only use
   * this option for debugging only.
   * @server_default false

   */
  def processorGrok(
    errorTrace: Boolean = false,
    filterPath: Chunk[String] = Chunk.empty[String],
    human: Boolean = false,
    pretty: Boolean = false
  ): ZIO[Any, FrameworkException, ProcessorGrokResponse] = {
    val request = ProcessorGrokRequest(
      errorTrace = errorTrace,
      filterPath = filterPath,
      human = human,
      pretty = pretty
    )

    processorGrok(request)

  }

  def processorGrok(
    request: ProcessorGrokRequest
  ): ZIO[Any, FrameworkException, ProcessorGrokResponse] =
    httpService.execute[Json, ProcessorGrokResponse](request)

  /*
   * Creates or updates a pipeline.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/put-pipeline-api.html
   *
   * @param id Pipeline ID
   * @param body a Pipeline
   * @param errorTrace When set to `true` OpenSearch will include the full stack trace of errors
   * when they occur.
   * @server_default false

   * @param filterPath Comma-separated list of filters in dot notation which reduce the response
   * returned by OpenSearch.

   * @param human When set to `true` will return statistics in a format suitable for humans.
   * For example `"exists_time": "1h"` for humans and
   * `"eixsts_time_in_millis": 3600000` for computers. When disabled the human
   * readable values will be omitted. This makes sense for responses being consumed
   * only by machines.
   * @server_default false

   * @param pretty If set to `true` the returned JSON will be "pretty-formatted". Only use
   * this option for debugging only.
   * @server_default false

   * @param ifVersion Required version for optimistic concurrency control for pipeline updates
   * @param masterTimeout Explicit operation timeout for connection to master node
   * @param timeout Explicit operation timeout
   */
  def putPipeline(
    id: String,
    body: Pipeline,
    errorTrace: Boolean = false,
    filterPath: Chunk[String] = Chunk.empty[String],
    human: Boolean = false,
    pretty: Boolean = false,
    ifVersion: Option[Int] = None,
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None
  ): ZIO[Any, FrameworkException, PutPipelineResponse] = {
    val request = PutPipelineRequest(
      id = id,
      body = body,
      errorTrace = errorTrace,
      filterPath = filterPath,
      human = human,
      pretty = pretty,
      ifVersion = ifVersion,
      masterTimeout = masterTimeout,
      timeout = timeout
    )

    putPipeline(request)

  }

  def putPipeline(
    request: PutPipelineRequest
  ): ZIO[Any, FrameworkException, PutPipelineResponse] =
    httpService.execute[Pipeline, PutPipelineResponse](request)

  /*
   * Allows to simulate a pipeline with example documents.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/simulate-pipeline-api.html
   *
   * @param body body the body of the call
   * @param errorTrace When set to `true` OpenSearch will include the full stack trace of errors
   * when they occur.
   * @server_default false

   * @param filterPath Comma-separated list of filters in dot notation which reduce the response
   * returned by OpenSearch.

   * @param human When set to `true` will return statistics in a format suitable for humans.
   * For example `"exists_time": "1h"` for humans and
   * `"eixsts_time_in_millis": 3600000` for computers. When disabled the human
   * readable values will be omitted. This makes sense for responses being consumed
   * only by machines.
   * @server_default false

   * @param pretty If set to `true` the returned JSON will be "pretty-formatted". Only use
   * this option for debugging only.
   * @server_default false

   * @param id Pipeline ID
   * @param verbose Verbose mode. Display data output for each processor in executed pipeline
   */
  def simulate(
    body: SimulateRequestBody,
    errorTrace: Boolean = false,
    filterPath: Chunk[String] = Chunk.empty[String],
    human: Boolean = false,
    pretty: Boolean = false,
    id: Option[String] = None,
    verbose: Boolean = false
  ): ZIO[Any, FrameworkException, SimulateResponse] = {
    val request = SimulateRequest(
      body = body,
      errorTrace = errorTrace,
      filterPath = filterPath,
      human = human,
      pretty = pretty,
      id = id,
      verbose = verbose
    )

    simulate(request)

  }

  def simulate(
    request: SimulateRequest
  ): ZIO[Any, FrameworkException, SimulateResponse] =
    httpService.execute[SimulateRequestBody, SimulateResponse](request)

}
