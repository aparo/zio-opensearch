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

package zio.opensearch.mappings

import zio.opensearch.SpecHelper
import zio.json._
import zio.json.ast._
import zio.json._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.opensearch.indices.get.GetResponse

class MappingsSpec extends AnyFlatSpec with Matchers with SpecHelper {
  "Mappings" should "serialize and deserialize index with computed" in {
    val json = readResourceJSON("/zio/opensearch/mappings/logs.json")
    val oIndexResponse = json.as[GetResponse]
    oIndexResponse.isRight should be(true)
    val indexResponse = oIndexResponse.right.get
    indexResponse.size should be(1)
    indexResponse.contains("logs") should be(true)
    val indexTemplate = indexResponse("logs")
    indexTemplate.indexPatterns.size should be(0)
    indexTemplate.mappings.properties.size should be(2)
    indexTemplate.mappings.derived.isDefined should be(true)

  }

  it should "serialize and deserialize all the types" in {
    val json = readResourceJSON("/zio/opensearch/mappings/multi.json")
    val oIndexResponse = json.as[GetResponse]
    oIndexResponse.isRight should be(true)
    val indexResponse = oIndexResponse.right.get
    indexResponse.size should be(1)
    indexResponse.contains("multi") should be(true)
    val indexTemplate = indexResponse("multi")
    indexTemplate.indexPatterns.size should be(0)
    indexTemplate.mappings.derived.isDefined should be(true)
    indexTemplate.mappings.properties.size should be(27)

  }
}