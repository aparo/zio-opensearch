/*
 * Copyright 2019-2023 Alberto Paro
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

package zio.elasticsearch.indices.forcemerge
import scala.collection.mutable
import zio._
import zio.elasticsearch.common._
import zio.json.ast._
/*
 * Performs the force merge operation on one or more indices.
 * For more info refers to https://www.elastic.co/guide/en/elasticsearch/reference/master/indices-forcemerge.html
 *
 * @param index

 * @param errorTrace When set to `true` Elasticsearch will include the full stack trace of errors
 * when they occur.
 * @server_default false

 * @param filterPath Comma-separated list of filters in dot notation which reduce the response
 * returned by Elasticsearch.

 * @param human When set to `true` will return statistics in a format suitable for humans.
 * For example `"exists_time": "1h"` for humans and
 * `"eixsts_time_in_millis": 3600000` for computers. When disabled the human
 * readable values will be omitted. This makes sense for responses being consumed
 * only by machines.
 * @server_default false

 * @param pretty If set to `true` the returned JSON will be "pretty-formatted". Only use
 * this option for debugging only.
 * @server_default false

 * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
 * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
 * @param flush Specify whether the index should be flushed after performing the operation (default: true)
 * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
 * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
 * @param maxNumSegments The number of segments the index should be merged into (default: dynamic)
 * @param onlyExpungeDeletes Specify whether the operation should only expunge deleted documents
 * @param waitForCompletion Should the request wait until the force merge is completed.
 */

final case class ForcemergeRequest(
  index: Chunk[String],
  errorTrace: Boolean = false,
  filterPath: Chunk[String] = Chunk.empty[String],
  human: Boolean = false,
  pretty: Boolean = false,
  allowNoIndices: Option[Boolean] = None,
  expandWildcards: Seq[ExpandWildcards] = Nil,
  flush: Option[Boolean] = None,
  ignoreUnavailable: Option[Boolean] = None,
  indices: Chunk[String] = Chunk.empty,
  maxNumSegments: Option[Double] = None,
  onlyExpungeDeletes: Option[Boolean] = None,
  waitForCompletion: Boolean = true
) extends ActionRequest[Json]
    with RequestBase {
  def method: Method = Method.POST

  def urlPath: String = this.makeUrl(indices, "_forcemerge")

  def queryArgs: Map[String, String] = {
    // managing parameters
    val queryArgs = new mutable.HashMap[String, String]()
    allowNoIndices.foreach { v =>
      queryArgs += ("allow_no_indices" -> v.toString)
    }
    if (expandWildcards.nonEmpty) {
      if (expandWildcards.toSet != Set(ExpandWildcards.open)) {
        queryArgs += ("expand_wildcards" -> expandWildcards.mkString(","))
      }

    }
    flush.foreach { v =>
      queryArgs += ("flush" -> v.toString)
    }
    ignoreUnavailable.foreach { v =>
      queryArgs += ("ignore_unavailable" -> v.toString)
    }
    maxNumSegments.foreach { v =>
      queryArgs += ("max_num_segments" -> v.toString)
    }
    onlyExpungeDeletes.foreach { v =>
      queryArgs += ("only_expunge_deletes" -> v.toString)
    }
    if (waitForCompletion != true)
      queryArgs += ("wait_for_completion" -> waitForCompletion.toString)
    // Custom Code On
    // Custom Code Off
    queryArgs.toMap
  }

  def body: Json = Json.Null

  // Custom Code On
  // Custom Code Off

}
