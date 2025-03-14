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

package zio.opensearch.responses.cat

import zio.json._
/*
 * Returns help for the Cat APIs.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/cat.html
 *
 * @param help Return help information
 * @param s Comma-separated list of column names or column aliases to sort by
 */
final case class CatHelpResponse(_ok: Option[Boolean] = None)
object CatHelpResponse {
  implicit val jsonDecoder: JsonDecoder[CatHelpResponse] = DeriveJsonDecoder.gen[CatHelpResponse]
  implicit val jsonEncoder: JsonEncoder[CatHelpResponse] = DeriveJsonEncoder.gen[CatHelpResponse]
}
