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
final case class RoleDescriptorRead(
  cluster: Chunk[String],
  indices: Chunk[IndicesPrivileges],
  global: Option[Chunk[GlobalPrivilege]] = None,
  applications: Option[Chunk[ApplicationPrivileges]] = None,
  metadata: Option[Metadata] = None,
  @jsonField("run_as") runAs: Option[Chunk[String]] = None,
  @jsonField("transient_metadata") transientMetadata: Option[
    TransientMetadataConfig
  ] = None
)

object RoleDescriptorRead {
  implicit lazy val jsonCodec: JsonCodec[RoleDescriptorRead] =
    DeriveJsonCodec.gen[RoleDescriptorRead]
}
