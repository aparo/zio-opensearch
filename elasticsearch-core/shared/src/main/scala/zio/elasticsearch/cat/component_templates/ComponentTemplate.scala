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

package zio.elasticsearch.cat.component_templates
import zio.json._
import zio.json.ast._
final case class ComponentTemplate(
  name: String,
  version: String,
  @jsonField("alias_count") aliasCount: String,
  @jsonField("mapping_count") mappingCount: String,
  @jsonField("settings_count") settingsCount: String,
  @jsonField("metadata_count") metadataCount: String,
  @jsonField("included_in") includedIn: String
)

object ComponentTemplate {
  implicit val jsonCodec: JsonCodec[ComponentTemplate] =
    DeriveJsonCodec.gen[ComponentTemplate]
}
