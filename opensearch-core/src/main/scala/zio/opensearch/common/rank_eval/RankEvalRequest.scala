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

package zio.opensearch.common.rank_eval
import scala.collection.mutable
import zio._
import zio.opensearch.common.search.SearchType
import zio.opensearch.common._
import zio.opensearch.common.requests.RankEvalRequestBody
/*
 * Allows to evaluate the quality of ranked search results over a set of typical search queries
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/search-rank-eval.html
 *
 * @param index Comma-separated list of data streams, indices, and index aliases used to limit the request. Wildcard (`*`) expressions are supported.
 * To target all data streams and indices in a cluster, omit this parameter or use `_all` or `*`.

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

 * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
 * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
 * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
 * @param indices A comma-separated list of index names to search; use `_all` or empty string to perform the operation on all indices
 * @param searchType Search operation type
 */

final case class RankEvalRequest(
  index: Chunk[String],
  body: RankEvalRequestBody,
  errorTrace: Boolean = false,
  filterPath: Chunk[String] = Chunk.empty[String],
  human: Boolean = false,
  pretty: Boolean = false,
  allowNoIndices: Option[Boolean] = None,
  expandWildcards: Seq[ExpandWildcards] = Nil,
  ignoreUnavailable: Option[Boolean] = None,
  indices: Chunk[String] = Chunk.empty,
  searchType: Option[SearchType] = None
) extends ActionRequest[RankEvalRequestBody]
    with RequestBase {
  def method: Method = Method.GET

  def urlPath: String = this.makeUrl(indices, "_rank_eval")

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
    ignoreUnavailable.foreach { v =>
      queryArgs += ("ignore_unavailable" -> v.toString)
    }
    searchType.foreach { v =>
      queryArgs += ("search_type" -> v.toString)
    }
    // Custom Code On
    // Custom Code Off
    queryArgs.toMap
  }

  // Custom Code On
  // Custom Code Off

}
