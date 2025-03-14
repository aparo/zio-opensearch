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

package zio.schema.opensearch

import zio.json._

/**
 * This class defines a Script Parameter used in typing the scripts
 * @param name
 *   the name of the entity
 * @param type
 *   the type of the entity
 * @param description
 *   the description of the ScriptParameter
 */
final case class ScriptParameter(name: String, `type`: ScriptType, description: Option[String] = None)
object ScriptParameter {
  implicit val jsonDecoder: JsonDecoder[ScriptParameter] = DeriveJsonDecoder.gen[ScriptParameter]
  implicit val jsonEncoder: JsonEncoder[ScriptParameter] = DeriveJsonEncoder.gen[ScriptParameter]
}

@jsonEnumLowerCase
sealed trait ScriptType extends EnumLowerCase

object ScriptType {

  case object String extends ScriptType

  case object Int extends ScriptType

  case object Float extends ScriptType

  case object Double extends ScriptType

  case object Long extends ScriptType

  case object LocalDateTime extends ScriptType

  case object LocalDate extends ScriptType

  case object OffsetDateTime extends ScriptType

  case object Byte extends ScriptType

  case object Any extends ScriptType

  implicit val decoder: JsonDecoder[ScriptType] = DeriveJsonDecoderEnum.gen[ScriptType]
  implicit val encoder: JsonEncoder[ScriptType] = DeriveJsonEncoderEnum.gen[ScriptType]

}
