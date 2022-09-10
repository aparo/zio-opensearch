/*
 * Copyright 2019 Alberto Paro
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

package zio.schema.elasticsearch

import enumeratum.EnumEntry.Lowercase
import enumeratum.{ CirceEnum, Enum, EnumEntry }

sealed trait IndexSharding extends EnumEntry with Lowercase

object IndexSharding extends Enum[IndexSharding] with CirceEnum[IndexSharding] {

  case object NONE extends IndexSharding

  case object Year extends IndexSharding

  case object Month extends IndexSharding

  case object Week extends IndexSharding

  case object Day extends IndexSharding

  case object Hour extends IndexSharding

  case object Quarter extends IndexSharding

  val values = findValues

}
