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

package zio.opensearch.security
import zio._
import zio.opensearch.common._
import zio.json._
import zio.json.ast._
final case class ApiKey(
  creation: Option[Long] = None,
  expiration: Option[Long] = None,
  id: String,
  invalidated: Option[Boolean] = None,
  name: String,
  realm: Option[String] = None,
  username: Option[Username] = None,
  metadata: Option[Metadata] = None,
  @jsonField("role_descriptors") roleDescriptors: Option[
    Map[String, RoleDescriptor]
  ] = None,
  @jsonField("limited_by") limitedBy: Option[
    Chunk[Map[String, RoleDescriptor]]
  ] = None,
  @jsonField("_sort") sort: Option[SortResults] = None
)

object ApiKey {
  implicit lazy val jsonCodec: JsonCodec[ApiKey] = DeriveJsonCodec.gen[ApiKey]
}
