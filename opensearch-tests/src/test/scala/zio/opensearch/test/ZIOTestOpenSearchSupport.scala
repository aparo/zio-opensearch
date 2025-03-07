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

package zio.opensearch.test

import zio.ZLayer
import zio.opensearch.client.ZioSttpClient
import zio.opensearch.client.ZioSttpClient.OpenSearchEnvironment
import zio.opensearch.cluster.ClusterManager
import zio.opensearch.indices.IndicesManager
import zio.opensearch.ingest.IngestManager
import zio.opensearch.orm.{ MappingManager, OrmManager }
import zio.opensearch.{ OpenSearch }
import zio.opensearch.OpenSearchConfig

trait ZIOTestOpenSearchSupport {

  lazy val osLayer: ZLayer[
    Any,
    Throwable,
    OpenSearchEnvironment with IndicesManager with ClusterManager with OrmManager with IngestManager
  ] = {
    val osInternalLayer = if (sys.env.getOrElse("USE_EMBEDDED", "true").toBoolean) {
      val osEmbedded: ZLayer[Any, Throwable, OpenSearch] = DockerOpenSearch.opensearch()
      ZioSttpClient.buildFromOpenSearch(osEmbedded)
    } else {
      ZioSttpClient.fullFromConfig(OpenSearchConfig())
    }
    osInternalLayer >+> IndicesManager.live >+> ClusterManager.live >+> IngestManager.live >+> MappingManager.live >+> OrmManager.live
  }
}
