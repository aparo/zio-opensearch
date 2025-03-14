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

package zio.schema

import zio.schema.generic.EnumSchema
import enumeratum.EnumEntry.Lowercase
import enumeratum.{ CirceEnum, Enum, EnumEntry }

sealed trait StorageType extends EnumLowerCase

object StorageType extends Enum[StorageType] with CirceEnum[StorageType] with EnumSchema[StorageType] {

  case object Columnar extends StorageType

  case object OpenSearch extends StorageType

  case object Ignite extends StorageType

  case object MongoDB extends StorageType

  case object SQL extends StorageType

  case object Parquet extends StorageType

  val values = findValues

}
