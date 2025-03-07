import sbt._
import sbt.Keys._

object Dependencies {
  import PlatformDependencies._

  lazy val testSupport = Seq(
    libraryDependencies ++= DependencyHelpers.test(
      ScalaTest.test.value,
      "dev.zio" %% "zio-test" % Versions.zio,
      "dev.zio" %% "zio-test-sbt" % Versions.zio,
      "org.opensearch" % "opensearch-testcontainers" % Versions.testContainerOpenSearch,
      "com.dimafeng" %% "testcontainers-scala-opensearch" % Versions.testContainerScala,
      "ch.qos.logback" % "logback-core" % "1.4.6"
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )

  lazy val zioSchemaOpenSearch = Seq(
    libraryDependencies ++= DependencyHelpers.compile(
      "dev.zio" %% "zio-schema-json" % "1.6.4"
    )
  )

  lazy val opensearchORM = Seq(
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % Versions.scala % Provided
    )
  )

  lazy val opensearchAdmin = Def.settings {
    libraryDependencies ++= DependencyHelpers.compile(
      )
  }

  lazy val opensearchAdminJS = Def.settings {
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % Versions.scala % Provided
    )
  }

  lazy val zioJsonExtra = Def.settings {
    libraryDependencies ++= DependencyHelpers.compile(
      ZIO.json.value,
      ZIO.core.value,
      ZIO.streams.value,
      "org.gnieh" %% "diffson-core" % "4.6.0",
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.13.0",
      "io.github.cquiroz" %% "scala-java-time" % "2.6.0"
    ) ++
      DependencyHelpers.test(
        ScalaTest.test.value,
        Specs2.core.value,
        Specs2.scalaCheck.value
      )
  }

  lazy val zioCommon = Def.settings {
    libraryDependencies ++=
      DependencyHelpers.compile(
        ZIO.zioJsonException.value,
        ZIO.zioJsonExtra.value
      ) ++ DependencyHelpers.test(
        ScalaTest.test.value,
        Specs2.core.value
      )
  }

  lazy val zioSchema = Def.settings {
    libraryDependencies ++= DependencyHelpers.compile(
      Libraries.magnolia.value,
      Libraries.shapeless.value
    ) ++
      DependencyHelpers.test(
        ScalaTest.test.value
      )
  }

  lazy val clientSTTP = Def.settings {
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.client3" %% "zio" % "3.10.3",
      "com.softwaremill.sttp.client3" %% "prometheus-backend" % "3.10.3"
    )
  }

  lazy val clientZioHTTP = Def.settings {
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio-http" % "3.0.1"
    )
  }

  lazy val clientHttp4s = Def.settings {
    libraryDependencies ++= Seq(
      HTTP4S.dsl,
      HTTP4S.circe,
      HTTP4S.blazeClient
    ) ++ DependencyHelpers.test(
      ScalaTest.test.value,
      "org.opensearch" % "opensearch-testcontainers" % Versions.testContainerOpenSearch
    )
  }

}
