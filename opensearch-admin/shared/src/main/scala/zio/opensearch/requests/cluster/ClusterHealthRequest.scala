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

import zio.opensearch.common.{ ExpandWildcards, Level }
import scala.collection.mutable

import zio.opensearch.requests.ActionRequest
import zio.opensearch.{ WaitForEvents, WaitForStatus }
import zio.json.ast.Json
import zio.json._
import zio.json.ast._

/*
 * Returns basic information about the health of the cluster.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cluster-health.html
 *
 * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
 * @param index Limit the information returned to a specific index
 * @param level Specify the level of detail for returned information
 * @param local Return local information, do not retrieve the state from master node (default: false)
 * @param masterTimeout Explicit operation timeout for connection to master node
 * @param timeout Explicit operation timeout
 * @param waitForActiveShards Wait until the specified number of shards is active
 * @param waitForEvents Wait until all currently queued events with the given priority are processed
 * @param waitForNoInitializingShards Whether to wait until there are no initializing shards in the cluster
 * @param waitForNoRelocatingShards Whether to wait until there are no relocating shards in the cluster
 * @param waitForNodes Wait until the specified number of nodes is available
 * @param waitForStatus Wait until cluster is in a specific state
 */
final case class ClusterHealthRequest(
  body: Json.Obj,
  @jsonField("expand_wildcards") expandWildcards: Seq[ExpandWildcards] = Nil,
  index: Option[String] = None,
  level: Level = Level.cluster,
  local: Option[Boolean] = None,
  @jsonField("master_timeout") masterTimeout: Option[String] = None,
  timeout: Option[String] = None,
  @jsonField("wait_for_active_shards") waitForActiveShards: Option[String] = None,
  @jsonField("wait_for_events") waitForEvents: Seq[WaitForEvents] = Nil,
  @jsonField("wait_for_no_initializing_shards") waitForNoInitializingShards: Option[Boolean] = None,
  @jsonField("wait_for_no_relocating_shards") waitForNoRelocatingShards: Option[Boolean] = None,
  @jsonField("wait_for_nodes") waitForNodes: Option[String] = None,
  @jsonField("wait_for_status") waitForStatus: Option[WaitForStatus] = None
) extends ActionRequest {
  def method: Method = Method.GET
  def urlPath: String = this.makeUrl("_cluster", "health", index)
  def queryArgs: Map[String, String] = {
    val queryArgs = new mutable.HashMap[String, String]()
    if (expandWildcards.nonEmpty) {
      if (expandWildcards.toSet != Set(ExpandWildcards.all)) {
        queryArgs += "expand_wildcards" -> expandWildcards.mkString(",")
      }
    }
    if (level != Level.cluster) queryArgs += "level" -> level.toString
    local.foreach { v =>
      queryArgs += "local" -> v.toString
    }
    masterTimeout.foreach { v =>
      queryArgs += "master_timeout" -> v.toString
    }
    timeout.foreach { v =>
      queryArgs += "timeout" -> v.toString
    }
    waitForActiveShards.foreach { v =>
      queryArgs += "wait_for_active_shards" -> v
    }
    if (waitForEvents.nonEmpty) {
      queryArgs += "wait_for_events" -> waitForEvents.mkString(",")
    }
    waitForNoInitializingShards.foreach { v =>
      queryArgs += "wait_for_no_initializing_shards" -> v.toString
    }
    waitForNoRelocatingShards.foreach { v =>
      queryArgs += "wait_for_no_relocating_shards" -> v.toString
    }
    waitForNodes.foreach { v =>
      queryArgs += "wait_for_nodes" -> v
    }
    waitForStatus.foreach { v =>
      queryArgs += "wait_for_status" -> v.toString
    }
    queryArgs.toMap
  }
}
object ClusterHealthRequest {
  implicit val jsonDecoder: JsonDecoder[ClusterHealthRequest] = DeriveJsonDecoder.gen[ClusterHealthRequest]
  implicit val jsonEncoder: JsonEncoder[ClusterHealthRequest] = DeriveJsonEncoder.gen[ClusterHealthRequest]
}
