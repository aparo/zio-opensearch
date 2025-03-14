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

// /*
//  * Copyright 2023-2025 Alberto Paro
//  *
//  * Licensed under the Apache License, Version 2.0 (the "License");
//  * you may not use this file except in compliance with the License.
//  * You may obtain a copy of the License at
//  *
//  *     http://www.apache.org/licenses/LICENSE-2.0
//  *
//  * Unless required by applicable law or agreed to in writing, software
//  * distributed under the License is distributed on an "AS IS" BASIS,
//  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  * See the License for the specific language governing permissions and
//  * limitations under the License.
//  */

// package zio.opensearch.client

// import fixures.models.GeoModel
// import zio.Random._
// import zio.{ Chunk, ZIO }
// import zio.auth.AuthContext
// import zio.opensearch.geo.{ GeoPoint, GeoPointLatLon }
// import zio.opensearch.indices.IndicesManager
// import zio.opensearch._
// import zio.opensearch.orm.{ OrmManager, TypedQueryBuilder }
// import zio.stream._
// import zio.test.Assertion._
// import zio.test._
// trait GeoSpec {

//   implicit def authContext: AuthContext

//   def geoIndexAndSorting = zio.test.test("geoIndex and Sorting") {
//     for {
//       ormManager <- ZIO.service[OrmManager]
//       openSearchManager <- ZIO.service[OpenSearchService]
//       indicesManager <- ZIO.service[IndicesManager]
//       indexName = GeoModel.osSchema.indexName
//       _ <- ormManager.deleteMapping[GeoModel].ignore
//       mapping <- ormManager.getMapping[GeoModel]
//       indexCreationResponse <- ormManager.createIndex[GeoModel]
//       records <- nextLongBetween(10, 20)
//       toIndex <- ZIO.foreach(1.to(records.toInt).toList) { i =>
//         for {
//           lat <- nextDoubleBetween(-90.0, 90.0)
//           lon <- nextDoubleBetween(-180.0, 180.0)
//         } yield GeoModel(s"test$i", GeoPointLatLon(lat, lon))
//       }
//       bulked <- ormManager.bulkStream[GeoModel](
//         ZStream.fromIterable(toIndex)
//       )
//       _ <- indicesManager.refresh(Chunk(indexName))
//       count <- openSearchManager.count(Chunk(indexName)).map(_.count)
//       qb: TypedQueryBuilder[GeoModel] <- ormManager.query[GeoModel]
//       qbQuery = qb.sortByDistance("geoPoint", GeoPoint(0, 0), unit = "km")
//       result <- qbQuery.results
//     } yield assert(records)(equalTo(count)) &&
//       assert(result.hits.hits.length)(equalTo(10)) &&
//       assert(result.hits.hits.head.sort.length)(equalTo(1))
//   }

// }
