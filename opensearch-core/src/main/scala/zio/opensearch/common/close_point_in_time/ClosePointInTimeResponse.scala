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

package zio.opensearch.common.close_point_in_time
import zio.json._
import zio.json.ast._
/*
 * Close a point in time
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/point-in-time-api.html
 *
 * @param succeeded

 * @param numFreed

 */
final case class ClosePointInTimeResponse(
  succeeded: Boolean = true,
  numFreed: Int
) {}
object ClosePointInTimeResponse {
  implicit lazy val jsonCodec: JsonCodec[ClosePointInTimeResponse] =
    DeriveJsonCodec.gen[ClosePointInTimeResponse]
}
