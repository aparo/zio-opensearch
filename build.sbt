import ReleaseTransformations._
import xerial.sbt.Sonatype._

inThisBuild(
  Seq(
    organization := "io.megl",
    parallelExecution := false,
    scalafmtOnCompile := false,
    Compile / packageDoc / publishArtifact := false,
    packageDoc / publishArtifact := false,
    Compile / doc / sources := Seq.empty,
    homepage := Some(url("https://github.com/aparo/zio-opensearch.git")),
    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer(
        "aparo",
        "Alberto Paro",
        "albeto.paro@gmail.com",
        url("https://github.com/aparo")
      )
    ),
    versionScheme := Some("early-semver"),
    sonatypeProfileName := "io.megl",
    licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
    sonatypeProjectHosting := Some(GitHubHosting("aparo", "zio-opensearch", "alberto.paro@gmail.com"))
  )
)

val disableDocs = Seq[Setting[_]](
  Compile / doc / sources := Seq.empty,
  Compile / packageDoc / publishArtifact := false
)

val disablePublishing = Seq[Setting[_]](
  publishArtifact := false,
  publish / skip := true
)

val paradiseVersion = "2.1.1"

val scalaTestVersion = "3.2.0-M2"
val scalaTestPlusVersion = "3.1.0.0-RC2"

lazy val root =
  project
    .in(file("."))
    .settings((publish / skip) := true)
    .aggregate(
      `zio-common`,
      `zio-schema-opensearch`,
      `opensearch-core`,
      `opensearch-cluster`,
      `opensearch-dangling-indices`,
      `opensearch-enrich`,
      `opensearch-indices`,
      `opensearch-ingest`,
      `opensearch-migration`,
      `opensearch-ml`,
      `opensearch-monitoring`,
      `opensearch-nodes`,
      `opensearch-rollup`,
      `opensearch-searchable-snapshots`,
      `opensearch-security`,
      `opensearch-shutdown`,
      `opensearch-slm`,
      `opensearch-snapshot`,
      `opensearch-sql`,
      `opensearch-ssl`,
      `opensearch-tasks`,
      `opensearch-text-structure`,
      `opensearch-transform`,
//      `opensearch-admin`,
//      `opensearch-cat`,
// custom managers
      `opensearch-orm`,
      // Clients
      `opensearch-client-sttp`
//      `opensearch-client-zio-http` //,
//        `opensearch-tests`
    )
    .settings(disableDocs)
    .settings(disablePublishing)

lazy val `zio-common` = ProjectUtils
  .setupProject("zio-common")
  .settings(
    moduleName := "zio-common"
  )
  .settings(Dependencies.zioCommon)

lazy val `zio-schema-opensearch` = ProjectUtils
  .setupProject("zio-schema-opensearch")
  .settings(
    moduleName := "zio-schema-opensearch"
  )
  .settings(Dependencies.zioSchemaOpenSearch)
  .settings(Dependencies.testSupport)
  .dependsOn(`zio-common`)

lazy val `opensearch-core` = ProjectUtils
  .setupProject("opensearch-core")
  .settings(
    moduleName := "zio-opensearch-core"
  )
  .dependsOn(`zio-schema-opensearch`)

lazy val `opensearch-cluster` = ProjectUtils
  .setupProject("opensearch-cluster")
  .settings(
    moduleName := "zio-opensearch-cluster"
  )
  .dependsOn(`opensearch-core` % "test->test;compile->compile")
  .dependsOn(`opensearch-ingest` % "test->test;compile->compile")
  .dependsOn(`opensearch-indices` % "test->test;compile->compile")

lazy val `opensearch-dangling-indices` = ProjectUtils
  .setupProject("opensearch-dangling-indices")
  .settings(
    moduleName := "zio-opensearch-dangling-indices"
  )
  .dependsOn(`opensearch-core` % "test->test;compile->compile")

lazy val `opensearch-enrich` = ProjectUtils
  .setupProject("opensearch-enrich")
  .settings(
    moduleName := "zio-opensearch-enrich"
  )
  .dependsOn(`opensearch-core` % "test->test;compile->compile")

lazy val `opensearch-indices` = ProjectUtils
  .setupProject("opensearch-indices")
  .settings(
    moduleName := "zio-opensearch-indices"
  )
  .dependsOn(`opensearch-core` % "test->test;compile->compile")

lazy val `opensearch-ingest` = ProjectUtils
  .setupProject("opensearch-ingest")
  .settings(
    moduleName := "zio-opensearch-ingest"
  )
  .dependsOn(`opensearch-core` % "test->test;compile->compile")

lazy val `opensearch-migration` = ProjectUtils
  .setupProject("opensearch-migration")
  .settings(
    moduleName := "zio-opensearch-migration"
  )
  .dependsOn(`opensearch-core` % "test->test;compile->compile")

lazy val `opensearch-ml` = ProjectUtils
  .setupProject("opensearch-ml")
  .settings(
    moduleName := "zio-opensearch-ml"
  )
  .dependsOn(`opensearch-core` % "test->test;compile->compile")
  .dependsOn(`opensearch-indices` % "test->test;compile->compile")

lazy val `opensearch-monitoring` = ProjectUtils
  .setupProject("opensearch-monitoring")
  .settings(
    moduleName := "zio-opensearch-monitoring"
  )
  .dependsOn(`opensearch-core` % "test->test;compile->compile")

lazy val `opensearch-nodes` = ProjectUtils
  .setupProject("opensearch-nodes")
  .settings(
    moduleName := "zio-opensearch-nodes"
  )
  .dependsOn(`opensearch-core` % "test->test;compile->compile")
  .dependsOn(`opensearch-indices` % "test->test;compile->compile")

lazy val `opensearch-rollup` = ProjectUtils
  .setupProject("opensearch-rollup")
  .settings(
    moduleName := "zio-opensearch-rollup"
  )
  .dependsOn(`opensearch-core` % "test->test;compile->compile")

lazy val `opensearch-searchable-snapshots` = ProjectUtils
  .setupProject("opensearch-searchable-snapshots")
  .settings(
    moduleName := "zio-opensearch-searchable-snapshots"
  )
  .dependsOn(`opensearch-core` % "test->test;compile->compile")

lazy val `opensearch-security` = ProjectUtils
  .setupProject("opensearch-security")
  .settings(
    moduleName := "zio-opensearch-security"
  )
  .dependsOn(`opensearch-core` % "test->test;compile->compile")

lazy val `opensearch-shutdown` = ProjectUtils
  .setupProject("opensearch-shutdown")
  .settings(
    moduleName := "zio-opensearch-shutdown"
  )
  .dependsOn(`opensearch-core` % "test->test;compile->compile")

lazy val `opensearch-slm` = ProjectUtils
  .setupProject("opensearch-slm")
  .settings(
    moduleName := "zio-opensearch-slm"
  )
  .dependsOn(`opensearch-core` % "test->test;compile->compile")

lazy val `opensearch-snapshot` = ProjectUtils
  .setupProject("opensearch-snapshot")
  .settings(
    moduleName := "zio-opensearch-snapshot"
  )
  .dependsOn(`opensearch-core` % "test->test;compile->compile")
  .dependsOn(`opensearch-indices` % "test->test;compile->compile")

lazy val `opensearch-sql` = ProjectUtils
  .setupProject("opensearch-sql")
  .settings(
    moduleName := "zio-opensearch-sql"
  )
  .dependsOn(`opensearch-core` % "test->test;compile->compile")

lazy val `opensearch-ssl` = ProjectUtils
  .setupProject("opensearch-ssl")
  .settings(
    moduleName := "zio-opensearch-ssl"
  )
  .dependsOn(`opensearch-core` % "test->test;compile->compile")

lazy val `opensearch-tasks` = ProjectUtils
  .setupProject("opensearch-tasks")
  .settings(
    moduleName := "zio-opensearch-tasks"
  )
  .dependsOn(`opensearch-core` % "test->test;compile->compile")

lazy val `opensearch-text-structure` = ProjectUtils
  .setupProject("opensearch-text-structure")
  .settings(
    moduleName := "zio-opensearch-text-structure"
  )
  .dependsOn(`opensearch-core` % "test->test;compile->compile")

lazy val `opensearch-transform` = ProjectUtils
  .setupProject("opensearch-transform")
  .settings(
    moduleName := "zio-opensearch-transform"
  )
  .dependsOn(`opensearch-core` % "test->test;compile->compile")
  .dependsOn(`opensearch-ml` % "test->test;compile->compile")

/*----------*/

lazy val `opensearch-orm` = ProjectUtils
  .setupProject("opensearch-orm")
  .settings(
    moduleName := "zio-opensearch-orm"
  )
  .dependsOn(`opensearch-cluster` % "test->test;compile->compile")
  .dependsOn(`opensearch-core` % "test->test;compile->compile")
  .settings(Dependencies.opensearchORM)

//lazy val `opensearch-admin` = ProjectUtils
//  .setupProject("opensearch-admin")
//  .settings(
//    moduleName := "zio-opensearch-admin"
//  )
//  .dependsOn(`opensearch-core` % "test->test;compile->compile")
//
//lazy val `opensearch-admin` = `opensearch-admin`.jvm.settings(Dependencies.opensearchAdmin)
//lazy val `opensearch-admin-js` = `opensearch-admin`.js.settings(Dependencies.opensearchAdminJS)
//
//lazy val `opensearch-cat` = ProjectUtils
//  .setupProject("opensearch-cat")
//  .settings(
//    moduleName := "zio-opensearch-cat"
//  )
//  .dependsOn(`opensearch-core` % "test->test;compile->compile")
//
//lazy val `opensearch-cat` = `opensearch-cat`.jvm
//lazy val `opensearch-cat-js` = `opensearch-cat`.js

lazy val `opensearch-client-sttp` = ProjectUtils
  .setupProject("opensearch-client-sttp")
  .settings(
    moduleName := "zio-opensearch-client-sttp"
  )
  .settings(Dependencies.clientSTTP)
  .settings(Dependencies.testSupport)
  .dependsOn(
    `opensearch-core` % "test->test;compile->compile"
  )

lazy val `opensearch-client-zio-http` = ProjectUtils
  .setupProject("opensearch-zio-http")
  .settings(
    moduleName := "zio-opensearch-zio-http"
  )
  .settings(Dependencies.clientZioHTTP)
  .settings(Dependencies.testSupport)
  .dependsOn(
    `opensearch-core` % "test->test;compile->compile"
  )

lazy val `opensearch-tests` = ProjectUtils
  .setupProject("opensearch-tests")
  .settings(
    moduleName := "zio-opensearch-tests"
  )
  .settings(Dependencies.testSupport)
  .dependsOn(
    `opensearch-core` % "test->test;compile->compile"
  )
  .dependsOn(
    `opensearch-cluster`,
    `opensearch-dangling-indices`,
    `opensearch-enrich`,
    `opensearch-indices`,
    `opensearch-ingest`,
    `opensearch-migration`,
    `opensearch-ml`,
    `opensearch-monitoring`,
    `opensearch-nodes`,
    `opensearch-rollup`,
    `opensearch-searchable-snapshots`,
    `opensearch-security`,
    `opensearch-shutdown`,
    `opensearch-slm`,
    `opensearch-snapshot`,
    `opensearch-sql`,
    `opensearch-ssl`,
    `opensearch-tasks`,
    `opensearch-text-structure`,
    `opensearch-transform`,
    `opensearch-orm`,
    `opensearch-client-sttp`,
    `opensearch-client-zio-http` //,
  )
  .settings(disableDocs)
  .settings(disablePublishing)

//lazy val `opensearch-orm` = ProjectUtils
//  .setupProject("opensearch-orm", CrossType.Full)
//  .settings(
//    moduleName := "zio-opensearch-orm"
//  )
//  .dependsOn(`zio-schema-opensearch`, `opensearch-admin` % "test->test;compile->compile")
//
//lazy val `opensearch-orm` = `opensearch-orm`.jvm
//lazy val `opensearch-orm-js` = `opensearch-orm`.js

/*
lazy val `opensearch-client-http4s` = ProjectUtils
  .setupProject("opensearch-client-http4s")
  .settings(
    moduleName := "zio-opensearch-client-http4s"
  )
  .settings(Dependencies.clientHttp4s)
  .dependsOn(
    `opensearch-orm` % "test->test;compile->compile",
    `opensearch-core` % "test->test;compile->compile",
    `opensearch-admin` % "test->test;compile->compile",
    `opensearch-cat` % "test->test;compile->compile"
  )
 */
// Releasing
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
//  runClean,
//  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("publish"),
  setNextVersion,
  commitNextVersion,
  pushChanges
)
