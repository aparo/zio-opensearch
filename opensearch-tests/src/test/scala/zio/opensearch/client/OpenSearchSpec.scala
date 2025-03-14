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

package zio.opensearch.client

import zio.opensearch.test.ZIOTestOpenSearchSupport
import zio.auth.AuthContext
import zio.opensearch.indices.IndicesManager
import zio.opensearch.queries.TermQuery
import zio.opensearch.OpenSearchService
import zio.opensearch.common.ResultDocument
import zio.opensearch.common.update_by_query.UpdateByQueryRequest
import zio.opensearch.ingest.requests.SimulateRequestBody
import zio.opensearch.ingest.{ IngestManager, Pipeline, SetProcessor }
import zio.opensearch.orm.OrmManager
import zio.json._
import zio.json.ast._
import zio.schema.opensearch.annotations.CustomId
import zio.stream._
import zio.test.Assertion._
import zio.test._
import zio.{ Chunk, ZIO }

object OpenSearchSpec extends ZIOSpecDefault with ZIOTestOpenSearchSupport with ORMSpec /*with GeoSpec*/ {
  //#define-class
  case class Book(id: Int, title: String, pages: Int) extends CustomId {
    override def calcId(): String = id.toString
  }
  object Book {
    implicit final val decoder: JsonDecoder[Book] =
      DeriveJsonDecoder.gen[Book]
    implicit final val encoder: JsonEncoder[Book] =
      DeriveJsonEncoder.gen[Book]
    implicit final val codec: JsonCodec[Book] = JsonCodec(encoder, decoder)

  }

  implicit val authContext = AuthContext.System

  val SAMPLE_RECORDS = Seq(
    Book(1, "Akka in Action", 1),
    Book(2, "Programming in Scala", 2),
    Book(3, "Learning Scala", 3),
    Book(4, "Scala for Spark in Production", 4),
    Book(5, "Scala Puzzlers", 5),
    Book(6, "Effective Akka", 6),
    Book(7, "Akka Concurrency", 7)
  )

  def populate(index: String) =
    for {
      opensearchService <- ZIO.service[OpenSearchService]
      indicesManager <- ZIO.service[IndicesManager]
      _ <- ZIO.foreachDiscard(SAMPLE_RECORDS) { book =>
        opensearchService.index(
          index,
          body = Json.Obj(
            "title" -> Json.Str(book.title),
            "pages" -> Json.Num(book.pages),
            "active" -> Json.Bool(false)
          )
        )
      }
      _ <- indicesManager.refresh(index)

    } yield ()

  def countElement = test("count elements") {
    val index = "count_element"
    for {
      opensearchService <- ZIO.service[OpenSearchService]
      _ <- populate(index)
      countResult <- opensearchService.count(Chunk(index))
    } yield assert(countResult.count)(equalTo(SAMPLE_RECORDS.length.toLong))
  }

  def updateByQueryElements = test("update by query elements") {
    val index = new Object() {}.getClass.getEnclosingMethod.getName.toLowerCase
    for {
      _ <- populate(index)
      openSearchService <- ZIO.service[OpenSearchService]
      indicesManager <- ZIO.service[IndicesManager]
      ormManager <- ZIO.service[OrmManager]
      updatedResult <- openSearchService.updateByQuery(
        UpdateByQueryRequest.fromPartialDocument(index, Json.Obj("active" -> Json.Bool(true)))
      )
      _ <- indicesManager.refresh(index)
      qb <- ormManager.queryBuilder(indices = Chunk(index), filters = Chunk(TermQuery("active", true)))
      searchResult <- qb.results
    } yield assert(updatedResult.updated)(equalTo(SAMPLE_RECORDS.length.toLong)) &&
      assert(searchResult.total)(equalTo(SAMPLE_RECORDS.length.toLong))
  }

  def pipeline = test("put/get/simulate/delete pipeline") {
    for {
      ingestManager <- ZIO.service[IngestManager]
      pipelineName = "my-pipe"
      pipeline = Pipeline(
        description = Some("test pipeline"),
        processors = Chunk(SetProcessor(field = "test", value = Some(Json.Bool(true))))
      )
      _ <- ingestManager.putPipeline(pipelineName, pipeline)
      pipes <- ingestManager.getPipeline(id = Some(pipelineName))
      resSimulate <- ingestManager.simulate(
        SimulateRequestBody(
          docs = Chunk(ResultDocument(id = "1234", index = "test-pipe", source = Some(Json.Obj()))),
          pipeline = Some(pipeline)
        )
      )

      resultDelete <- ingestManager.deletePipeline(pipelineName)

    } yield assert(pipes(pipelineName))(equalTo(pipeline)) &&
      assert(resSimulate.docs.head.doc.get.source.contains("test"))(equalTo(true)) &&
      assert(resultDelete.acknowledged)(equalTo(true))
  }

  def sinker = test("sinker") {
    val index = new Object() {}.getClass.getEnclosingMethod.getName.toLowerCase
    for {
      ormManager <- ZIO.service[OrmManager]
      indicesManager <- ZIO.service[IndicesManager]
      _ <- indicesManager.delete(Chunk(index)).ignore
      numbers <- zio.Random.nextIntBetween(20, 100)
      osService <- ZIO.service[OpenSearchService]
      total <- ZIO.scoped {
        for {
          bulker <- osService.makeBulker(bulkSize = 10)
          sink = osService.sink[Book](index = index, bulker = bulker)
          total <- ZStream
            .fromIterable(1.to(numbers))
            .mapZIO { i =>
              for {
                s <- zio.Random.nextString(10)
              } yield Book(i, s, i)

            }
            .run(sink)
        } yield total
      }
      _ <- indicesManager.refresh(index)
      qb <- ormManager.queryBuilder(indices = Chunk(index))
      searchResult <- qb.results
    } yield assert(total)(equalTo(numbers.toLong)) &&
      assert(searchResult.total)(equalTo(numbers.toLong))
  }

  override def spec: Spec[TestEnvironment, Throwable] =
    suite("OpenSearchSpec")(
      pipeline,
      sinker,
      countElement,
      updateByQueryElements,
//      ormSchemaCheck,
      ormBulker,
      ormMultiTypeIndexBulker
//      ormMultiCallOnCreate,
      // geoIndexAndSorting
    ).provideSomeLayerShared[TestEnvironment](
      osLayer
    ) @@ TestAspect.sequential @@ TestAspect.withLiveClock

}
