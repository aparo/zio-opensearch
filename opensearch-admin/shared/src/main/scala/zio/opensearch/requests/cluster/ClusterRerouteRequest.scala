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

package zio.opensearch.requests.cluster

import scala.collection.mutable

import zio.opensearch.requests.ActionRequest
import zio.json.ast.Json
import zio.json._
import zio.json.ast._

/*
 * Allows to manually change the allocation of individual shards in the cluster.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-reroute.html
 *
 * @param body body the body of the call
 * @param dryRun Simulate the operation only and return the resulting state
 * @param explain Return an explanation of why the commands can or cannot be executed
 * @param masterTimeout Explicit operation timeout for connection to master node
 * @param metric Limit the information returned to the specified metrics. Defaults to all but metadata
 * @param retryFailed Retries allocation of shards that are blocked due to too many subsequent allocation failures
 * @param timeout Explicit operation timeout
 */
final case class ClusterRerouteRequest(
  body: Option[Json.Obj] = None,
  @jsonField("dry_run") dryRun: Option[Boolean] = None,
  explain: Option[Boolean] = None,
  @jsonField("master_timeout") masterTimeout: Option[String] = None,
  metric: Chunk[String] = Chunk.empty,
  @jsonField("retry_failed") retryFailed: Option[Boolean] = None,
  timeout: Option[String] = None
) extends ActionRequest {
  def method: Method = Method.POST
  def urlPath = "/_cluster/reroute"
  def queryArgs: Map[String, String] = {
    val queryArgs = new mutable.HashMap[String, String]()
    body.foreach { v =>
      queryArgs += "body" -> v.toString
    }
    dryRun.foreach { v =>
      queryArgs += "dry_run" -> v.toString
    }
    explain.foreach { v =>
      queryArgs += "explain" -> v.toString
    }
    masterTimeout.foreach { v =>
      queryArgs += "master_timeout" -> v.toString
    }
    if (metric.nonEmpty) {
      queryArgs += "metric" -> metric.toList.mkString(",")
    }
    retryFailed.foreach { v =>
      queryArgs += "retry_failed" -> v.toString
    }
    timeout.foreach { v =>
      queryArgs += "timeout" -> v.toString
    }
    queryArgs.toMap
  }
}
object ClusterRerouteRequest {
  implicit val jsonDecoder: JsonDecoder[ClusterRerouteRequest] = DeriveJsonDecoder.gen[ClusterRerouteRequest]
  implicit val jsonEncoder: JsonEncoder[ClusterRerouteRequest] = DeriveJsonEncoder.gen[ClusterRerouteRequest]
}
