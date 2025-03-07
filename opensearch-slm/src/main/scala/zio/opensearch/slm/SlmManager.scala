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

package zio.opensearch.slm

import zio.opensearch.common._
import zio.opensearch._
import zio.json._
import zio.json.ast._
import zio._
import zio.exception._
import zio.opensearch.slm.delete_lifecycle.DeleteLifecycleRequest
import zio.opensearch.slm.delete_lifecycle.DeleteLifecycleResponse
import zio.opensearch.slm.execute_lifecycle.ExecuteLifecycleRequest
import zio.opensearch.slm.execute_lifecycle.ExecuteLifecycleResponse
import zio.opensearch.slm.execute_retention.ExecuteRetentionRequest
import zio.opensearch.slm.execute_retention.ExecuteRetentionResponse
import zio.opensearch.slm.get_lifecycle.GetLifecycleRequest
import zio.opensearch.slm.get_lifecycle.GetLifecycleResponse
import zio.opensearch.slm.get_stats.GetStatsRequest
import zio.opensearch.slm.get_stats.GetStatsResponse
import zio.opensearch.slm.get_status.GetStatusRequest
import zio.opensearch.slm.get_status.GetStatusResponse
import zio.opensearch.slm.put_lifecycle.PutLifecycleRequest
import zio.opensearch.slm.put_lifecycle.PutLifecycleResponse
import zio.opensearch.slm.requests.PutLifecycleRequestBody
import zio.opensearch.slm.start.StartRequest
import zio.opensearch.slm.start.StartResponse
import zio.opensearch.slm.stop.StopRequest
import zio.opensearch.slm.stop.StopResponse

object SlmManager {
  lazy val live: ZLayer[OpenSearchHttpService, Nothing, SlmManager] =
    ZLayer {
      for {
        httpServiceBase <- ZIO.service[OpenSearchHttpService]
      } yield new SlmManager {
        override def httpService: OpenSearchHttpService = httpServiceBase
      }
    }

}

trait SlmManager {
  def httpService: OpenSearchHttpService
  /*
   * Deletes an existing snapshot lifecycle policy.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/slm-api-delete-policy.html
   *
   * @param policyId The id of the snapshot lifecycle policy to remove
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
  def deleteLifecycle(
    policyId: String,
    errorTrace: Boolean = false,
    filterPath: Chunk[String] = Chunk.empty[String],
    human: Boolean = false,
    pretty: Boolean = false
  ): ZIO[Any, FrameworkException, DeleteLifecycleResponse] = {
    val request = DeleteLifecycleRequest(
      policyId = policyId,
      errorTrace = errorTrace,
      filterPath = filterPath,
      human = human,
      pretty = pretty
    )

    deleteLifecycle(request)

  }

  def deleteLifecycle(
    request: DeleteLifecycleRequest
  ): ZIO[Any, FrameworkException, DeleteLifecycleResponse] =
    httpService.execute[Json, DeleteLifecycleResponse](request)

  /*
   * Immediately creates a snapshot according to the lifecycle policy, without waiting for the scheduled time.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/slm-api-execute-lifecycle.html
   *
   * @param policyId The id of the snapshot lifecycle policy to be executed
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
  def executeLifecycle(
    policyId: String,
    errorTrace: Boolean = false,
    filterPath: Chunk[String] = Chunk.empty[String],
    human: Boolean = false,
    pretty: Boolean = false
  ): ZIO[Any, FrameworkException, ExecuteLifecycleResponse] = {
    val request = ExecuteLifecycleRequest(
      policyId = policyId,
      errorTrace = errorTrace,
      filterPath = filterPath,
      human = human,
      pretty = pretty
    )

    executeLifecycle(request)

  }

  def executeLifecycle(
    request: ExecuteLifecycleRequest
  ): ZIO[Any, FrameworkException, ExecuteLifecycleResponse] =
    httpService.execute[Json, ExecuteLifecycleResponse](request)

  /*
   * Deletes any snapshots that are expired according to the policy's retention rules.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/slm-api-execute-retention.html
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
  def executeRetention(
    errorTrace: Boolean = false,
    filterPath: Chunk[String] = Chunk.empty[String],
    human: Boolean = false,
    pretty: Boolean = false
  ): ZIO[Any, FrameworkException, ExecuteRetentionResponse] = {
    val request = ExecuteRetentionRequest(
      errorTrace = errorTrace,
      filterPath = filterPath,
      human = human,
      pretty = pretty
    )

    executeRetention(request)

  }

  def executeRetention(
    request: ExecuteRetentionRequest
  ): ZIO[Any, FrameworkException, ExecuteRetentionResponse] =
    httpService.execute[Json, ExecuteRetentionResponse](request)

  /*
   * Retrieves one or more snapshot lifecycle policy definitions and information about the latest snapshot attempts.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/slm-api-get-policy.html
   *
   * @param policyId Comma-separated list of snapshot lifecycle policies to retrieve
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
  def getLifecycle(
    policyId: String,
    errorTrace: Boolean = false,
    filterPath: Chunk[String] = Chunk.empty[String],
    human: Boolean = false,
    pretty: Boolean = false
  ): ZIO[Any, FrameworkException, GetLifecycleResponse] = {
    val request = GetLifecycleRequest(
      policyId = policyId,
      errorTrace = errorTrace,
      filterPath = filterPath,
      human = human,
      pretty = pretty
    )

    getLifecycle(request)

  }

  def getLifecycle(
    request: GetLifecycleRequest
  ): ZIO[Any, FrameworkException, GetLifecycleResponse] =
    httpService.execute[Json, GetLifecycleResponse](request)

  /*
   * Returns global and policy-level statistics about actions taken by snapshot lifecycle management.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/slm-api-get-stats.html
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
  def getStats(
    errorTrace: Boolean = false,
    filterPath: Chunk[String] = Chunk.empty[String],
    human: Boolean = false,
    pretty: Boolean = false
  ): ZIO[Any, FrameworkException, GetStatsResponse] = {
    val request = GetStatsRequest(
      errorTrace = errorTrace,
      filterPath = filterPath,
      human = human,
      pretty = pretty
    )

    getStats(request)

  }

  def getStats(
    request: GetStatsRequest
  ): ZIO[Any, FrameworkException, GetStatsResponse] =
    httpService.execute[Json, GetStatsResponse](request)

  /*
   * Retrieves the status of snapshot lifecycle management (SLM).
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/slm-api-get-status.html
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
  def getStatus(
    errorTrace: Boolean = false,
    filterPath: Chunk[String] = Chunk.empty[String],
    human: Boolean = false,
    pretty: Boolean = false
  ): ZIO[Any, FrameworkException, GetStatusResponse] = {
    val request = GetStatusRequest(
      errorTrace = errorTrace,
      filterPath = filterPath,
      human = human,
      pretty = pretty
    )

    getStatus(request)

  }

  def getStatus(
    request: GetStatusRequest
  ): ZIO[Any, FrameworkException, GetStatusResponse] =
    httpService.execute[Json, GetStatusResponse](request)

  /*
   * Creates or updates a snapshot lifecycle policy.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/slm-api-put-policy.html
   *
   * @param policyId The id of the snapshot lifecycle policy
   * @param masterTimeout Period to wait for a connection to the master node. If no response is received before the timeout expires, the request fails and returns an error.
   * @server_default 30s

   * @param timeout Period to wait for a response. If no response is received before the timeout expires, the request fails and returns an error.
   * @server_default 30s

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

   * @param body body the body of the call
   */
  def putLifecycle(
    policyId: String,
    body: PutLifecycleRequestBody = PutLifecycleRequestBody(),
    masterTimeout: Option[String] = None,
    timeout: Option[String] = None,
    errorTrace: Boolean = false,
    filterPath: Chunk[String] = Chunk.empty[String],
    human: Boolean = false,
    pretty: Boolean = false
  ): ZIO[Any, FrameworkException, PutLifecycleResponse] = {
    val request = PutLifecycleRequest(
      policyId = policyId,
      masterTimeout = masterTimeout,
      timeout = timeout,
      errorTrace = errorTrace,
      filterPath = filterPath,
      human = human,
      pretty = pretty,
      body = body
    )

    putLifecycle(request)

  }

  def putLifecycle(
    request: PutLifecycleRequest
  ): ZIO[Any, FrameworkException, PutLifecycleResponse] =
    httpService.execute[PutLifecycleRequestBody, PutLifecycleResponse](request)

  /*
   * Turns on snapshot lifecycle management (SLM).
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/slm-api-start.html
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
  def start(
    errorTrace: Boolean = false,
    filterPath: Chunk[String] = Chunk.empty[String],
    human: Boolean = false,
    pretty: Boolean = false
  ): ZIO[Any, FrameworkException, StartResponse] = {
    val request = StartRequest(
      errorTrace = errorTrace,
      filterPath = filterPath,
      human = human,
      pretty = pretty
    )

    start(request)

  }

  def start(
    request: StartRequest
  ): ZIO[Any, FrameworkException, StartResponse] =
    httpService.execute[Json, StartResponse](request)

  /*
   * Turns off snapshot lifecycle management (SLM).
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/slm-api-stop.html
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
  def stop(
    errorTrace: Boolean = false,
    filterPath: Chunk[String] = Chunk.empty[String],
    human: Boolean = false,
    pretty: Boolean = false
  ): ZIO[Any, FrameworkException, StopResponse] = {
    val request = StopRequest(
      errorTrace = errorTrace,
      filterPath = filterPath,
      human = human,
      pretty = pretty
    )

    stop(request)

  }

  def stop(request: StopRequest): ZIO[Any, FrameworkException, StopResponse] =
    httpService.execute[Json, StopResponse](request)

}
