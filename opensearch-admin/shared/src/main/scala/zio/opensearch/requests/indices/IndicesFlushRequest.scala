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

package zio.opensearch.requests.indices

import zio.opensearch.common.ExpandWildcards
import scala.collection.mutable
import zio.opensearch.requests.ActionRequest
import zio.json.ast.Json
import zio.json._
import zio.json.ast._

/*
 * Performs the flush operation on one or more indices.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-flush.html
 *
 * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
 * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
 * @param force Whether a flush should be forced even if it is not necessarily needed ie. if no changes will be committed to the index. This is useful if transaction log IDs should be incremented even if no uncommitted changes are present. (This setting can be considered as internal)
 * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
 * @param indices A comma-separated list of index names; use `_all` or empty string for all indices
 * @param waitIfOngoing If set to true the flush operation will block until the flush can be executed if another flush operation is already executing. The default is true. If set to false the flush will be skipped iff if another flush operation is already running.
 */
final case class IndicesFlushRequest(
  @jsonField("allow_no_indices") allowNoIndices: Option[Boolean] = None,
  @jsonField("expand_wildcards") expandWildcards: Seq[ExpandWildcards] = Nil,
  force: Option[Boolean] = None,
  @jsonField("ignore_unavailable") ignoreUnavailable: Option[Boolean] = None,
  indices: Chunk[String] = Chunk.empty,
  @jsonField("wait_if_ongoing") waitIfOngoing: Option[Boolean] = None
) extends ActionRequest {
  def method: Method = Method.POST
  def urlPath: String = this.makeUrl(indices, "_flush")
  def queryArgs: Map[String, String] = {
    val queryArgs = new mutable.HashMap[String, String]()
    allowNoIndices.foreach { v =>
      queryArgs += "allow_no_indices" -> v.toString
    }
    if (expandWildcards.nonEmpty) {
      if (expandWildcards.toSet != Set(ExpandWildcards.open)) {
        queryArgs += "expand_wildcards" -> expandWildcards.mkString(",")
      }
    }
    force.foreach { v =>
      queryArgs += "force" -> v.toString
    }
    ignoreUnavailable.foreach { v =>
      queryArgs += "ignore_unavailable" -> v.toString
    }
    waitIfOngoing.foreach { v =>
      queryArgs += "wait_if_ongoing" -> v.toString
    }
    queryArgs.toMap
  }
  def body: Json = Json.Null
}
object IndicesFlushRequest {
  implicit val jsonDecoder: JsonDecoder[IndicesFlushRequest] = DeriveJsonDecoder.gen[IndicesFlushRequest]
  implicit val jsonEncoder: JsonEncoder[IndicesFlushRequest] = DeriveJsonEncoder.gen[IndicesFlushRequest]
}
