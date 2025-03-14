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

package zio.opensearch.responses.indices

import zio.Chunk

import scala.collection.mutable
import zio.opensearch.responses.AliasDefinition
import zio.json.ast.Json
import zio.json._
import zio.json.ast._
/*
 * Returns an alias.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-aliases.html
 *
 * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
 * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
 * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
 * @param indices A comma-separated list of index names to filter aliases
 * @param local Return local information, do not retrieve the state from master node (default: false)
 * @param name A comma-separated list of alias names to return
 */
final case class IndicesGetAliasResponse(aliases: Map[String, Map[String, AliasDefinition]] = Map.empty)

object IndicesGetAliasResponse {
  implicit val decodeIndicesGetAliasesResponse: JsonDecoder[IndicesGetAliasResponse] = Json.Obj.decoder.mapOrFail { c =>
    c.keys.toList match {
      case Nil => Right(IndicesGetAliasResponse())
      case indices =>
        Right(
          IndicesGetAliasResponse(
            aliases = indices.flatMap { f =>
              c.getOption[Json.Obj](f)
                .getOrElse(Json.Obj())
                .getOption[Json]("aliases")
                .flatMap(_.as[Map[String, AliasDefinition]].toOption)
                .map { agg =>
                  f -> agg
                }
            }.toMap
          )
        )
    }
  }
  implicit val encodeIndicesGetAliasesResponse: JsonEncoder[IndicesGetAliasResponse] = Json.Obj.encoder.contramap {
    obj =>
      val fields = new mutable.ListBuffer[(String, Json)]()
      obj.aliases.foreach {
        case (key, aliasDef) =>
          fields += (key -> Json.Obj("aliases" -> aliasDef.asJson))
      }
      Json.Obj(Chunk.fromIterable(fields))

  }
//  implicit val jsonDecoder: JsonDecoder[IndicesGetAliasResponse] = DeriveJsonDecoder.gen[IndicesGetAliasResponse]
//  implicit val jsonEncoder: JsonEncoder[IndicesGetAliasResponse] = DeriveJsonEncoder.gen[IndicesGetAliasResponse]
}

/* Example
{
 "logs_20162801" : {
   "aliases" : {
     "2016" : {
       "filter" : {
         "term" : {
           "year" : 2016
         }
       }
     }
   }
 }
}

 */
