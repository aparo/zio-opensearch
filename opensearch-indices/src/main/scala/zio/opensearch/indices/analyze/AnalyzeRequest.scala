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

package zio.opensearch.indices.analyze
import scala.collection.mutable
import zio._
import zio.opensearch.common._
import zio.opensearch.indices.requests.AnalyzeRequestBody
/*
 * Performs the analysis process on a text and return the tokens breakdown of the text.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-analyze.html
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

 * @param body body the body of the call
 * @param index The name of the index to scope the operation
 */

final case class AnalyzeRequest(
  body: AnalyzeRequestBody,
  errorTrace: Boolean = false,
  filterPath: Chunk[String] = Chunk.empty[String],
  human: Boolean = false,
  pretty: Boolean = false,
  index: Option[String] = None
) extends ActionRequest[AnalyzeRequestBody]
    with RequestBase {
  def method: Method = Method.GET

  def urlPath: String = this.makeUrl(index, "_analyze")

  def queryArgs: Map[String, String] = {
    // managing parameters
    val queryArgs = new mutable.HashMap[String, String]()
    index.foreach { v =>
      queryArgs += ("index" -> v)
    }
    // Custom Code On
    // Custom Code Off
    queryArgs.toMap
  }

  // Custom Code On
  // Custom Code Off

}
