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

package zio.opensearch.geo

import zio.opensearch.SpecHelper
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.json._
class GeoPointSpec extends AnyFlatSpec with Matchers with SpecHelper {
  "GeoPoint" should "deserialize lat/lon" in {

    val geopoint = """{"lat" : 40.73, "lon" : -74.1}""".fromJson[GeoPoint]
    geopoint.isRight should be(true)
    geopoint.value.isInstanceOf[GeoPointLatLon] should be(true)
    val realValue = geopoint.value.asInstanceOf[GeoPointLatLon]
    realValue.lat should be(40.73)
    realValue.lon should be(-74.1)
  }

  it should "deserialize array" in {

    val json = """[-74.1, 40.73]"""
    val geopoint = json.fromJson[GeoPoint]
    geopoint.isRight should be(true)
    geopoint.value.isInstanceOf[GeoPointLatLon] should be(true)
    val realValue = geopoint.value.asInstanceOf[GeoPointLatLon]
    realValue.lat should be(40.73)
    realValue.lon should be(-74.1)
  }

  it should "deserialize string" in {

    val json = """"40.73, -74.1""""
    val geopoint = json.fromJson[GeoPoint]
    geopoint.isRight should be(true)
    geopoint.value.isInstanceOf[GeoPointLatLon] should be(true)
    val realValue = geopoint.value.asInstanceOf[GeoPointLatLon]
    realValue.lat should be(40.73)
    realValue.lon should be(-74.1)
  }

  it should "deserialize hash" in {

    val json = """"dr5r9ydj2y73""""
    val geopoint = json.fromJson[GeoPoint]
    geopoint.isRight should be(true)
    geopoint.value.isInstanceOf[GeoPointLatLon] should be(true)
    val realValue = geopoint.value.asInstanceOf[GeoPointLatLon]
    realValue.lat.toInt should be(40)
    realValue.lon.toInt should be(-74)
  }

}
