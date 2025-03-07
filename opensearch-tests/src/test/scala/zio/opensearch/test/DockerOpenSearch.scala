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
import com.dimafeng.testcontainers.OpensearchContainer
import org.testcontainers.utility.DockerImageName
import zio._
import zio.opensearch.{ OpenSearch, OpenSearchConfig }

object DockerOpenSearch {

  case class DockerOpenSearch(container: OpensearchContainer, password: String) extends OpenSearch {
    def osConfig: UIO[OpenSearchConfig] = ZIO.succeed(
      OpenSearchConfig(
        container.httpHost.replace("http://", "").replace("https://", ""),
        useSSL = false,
        validateSSLCertificates = false,
        user = Some(container.username),
        password = Some(container.password)
      )
    )
    def migrate(): ZIO[Any, zio.exception.FrameworkException, Unit] = ZIO.unit
    def start(): UIO[Unit] = ZIO.attempt(container.start()).orDie
    def stop(): UIO[Unit] = ZIO.attempt(container.stop()).ignore.unit
  }

  lazy val defaultOpensearchDockerVersion =
    s"${sys.env.getOrElse("DOCKER_PROXY", "")}opensearchproject/opensearch:2.19.1"

  def opensearch(
    imageName: String = s"${sys.env.getOrElse("DOCKER_PROXY", "")}opensearchproject/opensearch:2.19.1",
    password: String = "mypassword"
  ): ZLayer[Any, Throwable, OpenSearch] = ZLayer.scoped {

    val containerInit: ZIO[Any, Throwable, DockerOpenSearch] =
      for {
        _ <- ZIO.logDebug(s"Init OpenSearch $imageName")
        container = new OpensearchContainer(
          dockerImageName = DockerImageName.parse(imageName),
          securityEnabled = false
        )
        _ <- ZIO.attempt(container.container.getEnvMap().put("node.attr.context", "hsoc")) // added segregation
        _ <- ZIO.attemptBlocking(container.start())
        result = DockerOpenSearch(container, password)
        _ <- result.migrate()
      } yield result

    ZIO.acquireRelease(containerInit)(container => container.stop())

  }
}
