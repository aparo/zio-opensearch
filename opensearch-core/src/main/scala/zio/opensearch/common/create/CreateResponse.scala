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

package zio.opensearch.common.create
import zio.opensearch.common._
import zio.json._
import zio.json.ast._
/*
 * Creates a new document in the index.

Returns a 409 response when a document with a same ID already exists in the index.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/docs-index_.html
 *
 * @param id

 * @param index

 * @param primaryTerm

 * @param result

 * @param seqNo

 * @param shards

 * @param version

 * @param forcedRefresh

 */
@jsonMemberNames(SnakeCase)
final case class CreateResponse(
  id: String,
  index: String,
  primaryTerm: Long = 0,
  result: Result = Result.created,
  seqNo: Int = 0,
  shards: ShardStatistics = ShardStatistics(),
  version: Int = 0,
  forcedRefresh: Boolean = true
) {}
object CreateResponse {
  implicit lazy val jsonCodec: JsonCodec[CreateResponse] =
    DeriveJsonCodec.gen[CreateResponse]
}
