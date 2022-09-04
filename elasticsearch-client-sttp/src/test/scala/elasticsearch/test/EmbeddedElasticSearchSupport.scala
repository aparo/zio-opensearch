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

package elasticsearch.test

import elasticsearch.client.ZioSttpClient
import elasticsearch.client.ZioSttpClient.ElasticSearchEnvironment
import elasticsearch.{ ElasticSearch, ElasticSearchConfig }
import zio.ZLayer

trait ZIOTestElasticSearchSupport {

  lazy val esLayer: ZLayer[Any, Throwable, ElasticSearchEnvironment] = {
    if (sys.env.getOrElse("USE_EMBEDDED", "true").toBoolean) {
      val esEmbedded: ZLayer[Any, Throwable, ElasticSearch] = DockerElasticSearch.elasticsearch()
      ZioSttpClient.buildFromElasticsearch(esEmbedded)
    } else {
      ZioSttpClient.fullFromConfig(ElasticSearchConfig())
    }
  }
}
