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

package zio.opensearch.nodes
import zio.json._
import zio.json.ast._
final case class JvmClasses(
  @jsonField("current_loaded_count") currentLoadedCount: Option[Long] = None,
  @jsonField("total_loaded_count") totalLoadedCount: Option[Long] = None,
  @jsonField("total_unloaded_count") totalUnloadedCount: Option[Long] = None
)

object JvmClasses {
  implicit lazy val jsonCodec: JsonCodec[JvmClasses] =
    DeriveJsonCodec.gen[JvmClasses]
}
