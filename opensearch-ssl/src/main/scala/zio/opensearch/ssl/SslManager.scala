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

package zio.opensearch.ssl

import zio.opensearch.common._
import zio.opensearch._
import zio.json._
import zio.json.ast._
import zio._
import zio.exception._

import zio.opensearch.ssl.certificates.CertificatesRequest
import zio.opensearch.ssl.certificates.CertificatesResponse

object SslManager {
  lazy val live: ZLayer[OpenSearchHttpService, Nothing, SslManager] =
    ZLayer {
      for {
        httpServiceBase <- ZIO.service[OpenSearchHttpService]
      } yield new SslManager {
        override def httpService: OpenSearchHttpService = httpServiceBase
      }
    }

}

trait SslManager {
  def httpService: OpenSearchHttpService
  /*
   * Retrieves information about the X.509 certificates used to encrypt communications in the cluster.
   * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/security-api-ssl.html
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
  def certificates(
    errorTrace: Boolean = false,
    filterPath: Chunk[String] = Chunk.empty[String],
    human: Boolean = false,
    pretty: Boolean = false
  ): ZIO[Any, FrameworkException, CertificatesResponse] = {
    val request = CertificatesRequest(
      errorTrace = errorTrace,
      filterPath = filterPath,
      human = human,
      pretty = pretty
    )

    certificates(request)

  }

  def certificates(
    request: CertificatesRequest
  ): ZIO[Any, FrameworkException, CertificatesResponse] =
    httpService.execute[Json, CertificatesResponse](request)

}
