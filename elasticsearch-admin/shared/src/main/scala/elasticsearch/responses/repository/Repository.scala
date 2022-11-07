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

package zio.elasticsearch.responses.repository

import zio.json._

@jsonDerive
final case class RepositoryList(
  repos: List[Repository] = List.empty[Repository]
)

@jsonDerive
final case class RepositorySettings(
  location: Option[String] = None,
  @jsonField("chunk_size") chunkSize: Option[String] = None,
  @jsonField("max_restore_bytes_per_sec") maxRestoreBytesPerSec: Option[
    String
  ] = None,
  @jsonField("max_snapshot_bytes_per_sec") maxSnapshotBytesPerSec: Option[
    String
  ] = None,
  compress: Option[String] = None,
  url: Option[String] = None,
  region: Option[String] = None,
  bucket: Option[String] = None,
  @jsonField("base_path") basePath: Option[String] = None,
  @jsonField("access_key") accessKey: Option[String] = None,
  @jsonField("secret_key") secretKey: Option[String] = None,
  //chunk_size:Option[String] = None,
  @jsonField("max_retries") maxRetries: Option[String] = None,
  //compress:Option[Boolean] = None,
  @jsonField("server_side_encryption") serverSideEncryption: Option[Boolean] = None,
  uri: Option[String] = None,
  path: Option[String] = None,
  @jsonField("load_defaults") loadDefaults: Option[Boolean] = None,
  @jsonField("conf_location") confLocation: Option[String] = None,
  @jsonField("concurrent_streams") concurrentStreams: Option[String] = None,
  //compress:Option[Boolean] = None,
  //chunk_size:Option[String] = None,
  container: Option[String] = None
  //base_path:Option[String] = None
  //concurrent_streams:Option[String] = None
  //chunk_size:Option[String] = None
  //compress:Option[Boolean] = None
)

@jsonDerive
final case class Repository(
  name: Option[String] = None,
  `type`: String = "",
  settings: RepositorySettings = RepositorySettings()
)
