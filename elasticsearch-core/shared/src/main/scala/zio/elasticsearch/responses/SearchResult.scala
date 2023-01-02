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

package zio.elasticsearch.responses

import scala.collection.mutable

import zio.elasticsearch.responses.aggregations.Aggregation
import zio.elasticsearch.responses.suggest.SuggestResponse
import zio.json._
import zio.json.ast._

// format: off

final case class Total(value: Long = 0, relation: String = "eq")
object Total {
implicit val jsonDecoder: JsonDecoder[Total] = DeriveJsonDecoder.gen[Total]
implicit val jsonEncoder: JsonEncoder[Total] = DeriveJsonEncoder.gen[Total]
}

/**
 * A search result including found documents in `hits`.
 * The length of the `hits` list may be less than `hits_total` if the query has `from` and `size` properties.
 */
final case class SearchResult[T](
    took: Long = 0L,
    timedOut: Boolean = false,
    shards: Shards = Shards(),
    total: Total = Total(),
    maxScore: Option[Double] = None,
    hits: List[ResultDocument[T]] = Nil,
    scrollId: Option[String] = None,
    aggregations: Map[String, Aggregation] = Map.empty[String, Aggregation],
    suggest: Map[String, List[SuggestResponse]] = Map.empty[String, List[SuggestResponse]]) {

  def aggregation(name: String): Option[Aggregation] = aggregations.get(name)

  def agg(name: String): Option[Aggregation] = aggregations.get(name)

  def suggest(name: String): List[SuggestResponse] =
    suggest.getOrElse(name, List.empty[SuggestResponse])

  def cookAggregations(
      sourceAggregations: Map[String, zio.elasticsearch.aggregations.Aggregation]): Map[String, Aggregation] = {
    sourceAggregations.foreach {
      case (name, agg) =>
//        aggregations.get(name).foreach { a =>
//          val subaggs = agg.aggregations
//          a.sourceAggregation = Some(agg)
////          if (subaggs.nonEmpty) {
////            println(subaggs)
////          }
//        }
    }
    aggregations
  }

}

object SearchResult {
  implicit def decodeSearchResult[T](implicit encode: JsonEncoder[T], decoder: JsonDecoder[T]): JsonDecoder[SearchResult[T]] = //{
    DeriveJsonDecoder.gen[SearchResult[T]]
//    JsonDecoder.instance { c =>
//      for {
//took <- c.downField("took").as[Long]
//           timed_out <- c.downField("timed_out").as[Boolean]
//           _shards <- c.downField("_shards").as[Shards]
//           total <- c.downField("hits").downField("total").as[Total]
//           max_score <- c.downField("hits").downField("max_score").as[Option[Double]]
//           hits <- c.downField("hits").downField("hits").as[List[ResultDocument[T]]]
//           scrollId <- c.downField("_scroll_id").as[Option[String]]
//           aggregations <- c.downField("aggregations").as[Option[Map[String, Aggregation]]]
//           suggest <- c.downField("suggest").as[Option[Map[String, List[SuggestResponse]]]]
//           vertex <- c.downField("vertex").as[Option[Json]]}
//        yield
//          SearchResult(
//            took = took,
//            timedOut = timed_out,
//            shards = _shards,
//            total = total,
//            maxScore = ResultDocument.validateScore(max_score),
//            hits = hits,
//            scrollId = scrollId,
//            aggregations = aggregations.getOrElse(Map.empty[String, Aggregation]),
//            suggest = suggest.getOrElse(Map.empty[String, List[SuggestResponse]])
//          )
//    }
//  }

  implicit def encodeSearchResult[T](implicit encode: JsonEncoder[T], decoder: JsonDecoder[T]): JsonEncoder[SearchResult[T]] = //{
    DeriveJsonEncoder.gen[SearchResult[T]]

//    JsonEncoder.instance { obj =>
//      val fields = new mutable.ListBuffer[(String, Json)]()
//      fields += ("took" -> obj.took.asJson)
//      fields += ("timed_out" -> obj.timedOut.asJson)
//      fields += ("_shards" -> obj.shards.asJson)
//      val hits = new mutable.ListBuffer[(String, Json)]()
//      hits += ("total" -> obj.total.asJson)
//      hits += ("hits" -> obj.hits.asJson)
//      obj.maxScore.map(v => hits += ("max_score" -> v.asJson))
//      fields += ("hits" -> Json.fromFields(hits))
//
//      obj.scrollId.map(v => fields += ("_scroll_id" -> v.asJson))
//      obj.vertex.map(v => fields += ("vertex" -> v.asJson))
//
//      if (obj.aggregations.nonEmpty) {
//        fields += ("aggregations" -> obj.aggregations.asJson)
//      }
//      if (obj.suggest.nonEmpty) {
//        fields += ("suggest" -> obj.suggest.asJson)
//      }
//      Json.Obj(Chunk.fromIterable(fields))
//    }
//  }

    def fromResponse[T](response: SearchResponse)(implicit encode: JsonEncoder[T], decoder: JsonDecoder[T]): SearchResult[T] =
    response.copy(hits = response.hits.map(t => ResultDocument.fromHit[T](t)))
  
}
// format: on
