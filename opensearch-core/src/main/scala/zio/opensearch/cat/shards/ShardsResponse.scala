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

package zio.opensearch.cat.shards
import zio._
import zio.json._
import zio.json.ast._
/*
 * Provides a detailed view of shard allocation on nodes.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cat-shards.html
 *
 * @param Array

 */
final case class ShardsResponse(
  Array: Chunk[ShardsRecord] = Chunk.empty[ShardsRecord]
) {}
object ShardsResponse {
  implicit lazy val jsonCodec: JsonCodec[ShardsResponse] =
    DeriveJsonCodec.gen[ShardsResponse]
}
