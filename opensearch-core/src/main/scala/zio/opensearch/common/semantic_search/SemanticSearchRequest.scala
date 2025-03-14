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

package zio.opensearch.common.semantic_search
import zio.Chunk

import scala.collection.mutable
import zio.opensearch.common._
import zio.json.ast._
/*
 * Semantic search API using dense vector similarity
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/semantic-search.html
 *
 * @param indices A comma-separated list of index names to search; use `_all` to perform the operation on all indices
 * @param body body the body of the call
 * @param routing A comma-separated list of specific routing values
 */

final case class SemanticSearchRequest(
  indices: Chunk[String] = Chunk.empty,
  body: Json = Json.Null,
  routing: Chunk[String] = Chunk.empty
) extends ActionRequest[Json] {
  def method: Method = Method.GET

  def urlPath: String = this.makeUrl(indices, "_semantic_search")

  def queryArgs: Map[String, String] = {
    // managing parameters
    val queryArgs = new mutable.HashMap[String, String]()
    if (routing.nonEmpty) {
      queryArgs += ("routing" -> routing.toList.mkString(","))
    }
    // Custom Code On
    // Custom Code Off
    queryArgs.toMap
  }

  // Custom Code On
  // Custom Code Off

}
