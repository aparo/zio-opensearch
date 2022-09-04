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

package elasticsearch

import enumeratum.EnumEntry.Lowercase
import enumeratum.{ CirceEnum, Enum, EnumEntry }
import io.circe.derivation.annotations.JsonCodec

sealed trait RegexOption extends EnumEntry with Lowercase

object RegexOption extends Enum[RegexOption] with CirceEnum[RegexOption] {

  case object CANON_EQ extends RegexOption

  case object CASE_INSENSITIVE extends RegexOption

  case object COMMENTS extends RegexOption

  case object DOTALL extends RegexOption

  case object LITERAL extends RegexOption
  case object MULTILINE extends RegexOption
  case object UNICODE_CASE extends RegexOption
  case object UNICODE_CHARACTER_CLASS extends RegexOption
  case object UNIX_LINES extends RegexOption

  val values = findValues

}

@JsonCodec
final case class Regex(pattern: String, flags: Seq[RegexOption] = Nil)
