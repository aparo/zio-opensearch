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

package zio.opensearch.common.search

import zio._
import zio.json._
final case class FetchProfile(
  @jsonField("type") `type`: String,
  description: String,
  @jsonField("time_in_nanos") timeInNanos: Long,
  breakdown: FetchProfileBreakdown,
  debug: Option[FetchProfileDebug] = None,
  children: Option[Chunk[FetchProfile]] = None
)

object FetchProfile {
  implicit lazy val jsonCodec: JsonCodec[FetchProfile] =
    DeriveJsonCodec.gen[FetchProfile]
}
