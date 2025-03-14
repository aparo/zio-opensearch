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

package zio.opensearch.indices.stats
import zio.json._
import zio.json.ast._
final case class ShardQueryCache(
  @jsonField("cache_count") cacheCount: Long,
  @jsonField("cache_size") cacheSize: Long,
  evictions: Long,
  @jsonField("hit_count") hitCount: Long,
  @jsonField("memory_size_in_bytes") memorySizeInBytes: Long,
  @jsonField("miss_count") missCount: Long,
  @jsonField("total_count") totalCount: Long
)

object ShardQueryCache {
  implicit lazy val jsonCodec: JsonCodec[ShardQueryCache] =
    DeriveJsonCodec.gen[ShardQueryCache]
}
