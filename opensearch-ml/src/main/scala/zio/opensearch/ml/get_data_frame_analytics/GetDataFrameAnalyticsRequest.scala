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

package zio.opensearch.ml.get_data_frame_analytics
import scala.collection.mutable
import zio._
import zio.opensearch.common._
import zio.json.ast._
/*
 * Retrieves configuration information for data frame analytics jobs.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/get-dfanalytics.html
 *
 * @param id The ID of the data frame analytics to fetch
 * @param verbose Defines whether the stats response should be verbose.
 * @server_default false

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

 * @param allowNoMatch Whether to ignore if a wildcard expression matches no data frame analytics. (This includes `_all` string or when no data frame analytics have been specified)
 * @param excludeGenerated Omits fields that are illegal to set on data frame analytics PUT
 * @param from skips a number of analytics
 * @param size specifies a max number of analytics to get
 */

final case class GetDataFrameAnalyticsRequest(
  id: String,
  verbose: Boolean,
  errorTrace: Boolean = false,
  filterPath: Chunk[String] = Chunk.empty[String],
  human: Boolean = false,
  pretty: Boolean = false,
  allowNoMatch: Boolean = true,
  excludeGenerated: Boolean = false,
  from: Int = 0,
  size: Int = 100
) extends ActionRequest[Json]
    with RequestBase {
  def method: Method = Method.GET

  def urlPath: String = this.makeUrl("_ml", "data_frame", "analytics", id)

  def queryArgs: Map[String, String] = {
    // managing parameters
    val queryArgs = new mutable.HashMap[String, String]()
    if (!allowNoMatch)
      queryArgs += ("allow_no_match" -> allowNoMatch.toString)
    if (excludeGenerated != false)
      queryArgs += ("exclude_generated" -> excludeGenerated.toString)
    if (from != 0) queryArgs += ("from" -> from.toString)
    if (size != 100) queryArgs += ("size" -> size.toString)
    // Custom Code On
    // Custom Code Off
    queryArgs.toMap
  }

  def body: Json = Json.Null

  // Custom Code On
  // Custom Code Off

}
