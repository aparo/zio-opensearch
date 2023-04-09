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

package zio.elasticsearch.cat.allocation
import zio._
import zio.json._
import zio.json.ast._
/*
 * Provides a snapshot of how many shards are allocated to each data node and how much disk space they are using.
 * For more info refers to https://www.elastic.co/guide/en/elasticsearch/reference/master/cat-allocation.html
 *
 * @param Array

 */
final case class AllocationResponse(
  Array: Chunk[AllocationRecord] = Chunk.empty[AllocationRecord]
) {}
object AllocationResponse {
  implicit val jsonCodec: JsonCodec[AllocationResponse] =
    DeriveJsonCodec.gen[AllocationResponse]
}
