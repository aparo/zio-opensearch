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

package zio.opensearch.sort

import zio.Chunk

import scala.collection.mutable
import zio.opensearch.geo.{ DistanceType, GeoPoint }
import zio.opensearch.queries.Query
import zio.opensearch.script.{ InlineScript, Script }
import zio.json._
import zio.json.ast._
import zio.json.internal.Write

object Sort {
  type Sort = List[Sorter]
  val EmptySort: List[Sorter] = Nil

  /* Sort to be used in serch_after with PIT*/
  def shardDoc: Sorter = FieldSort("_shard_doc", SortOrder.Desc)

}

sealed trait Sorter {
  /* If this sort is a _shard_doc sorting. Used in search_after*/
  def isShardDoc: Boolean = false
}

object Sorter {

  def apply(field: String, order: Boolean): FieldSort = {
    val orderName = order match {
      case true  => SortOrder.Asc
      case false => SortOrder.Desc
    }
    FieldSort(field, orderName)
  }

  def apply(orderName: String): FieldSort = {
    var field = orderName
    val ordering = orderName match {
      case "asc"  => SortOrder.Asc
      case "desc" => SortOrder.Desc
      case _ =>
        orderName.charAt(0) match {
          case '+' =>
            field = field.substring(1)
            SortOrder.Asc
          case '-' =>
            field = field.substring(1)
            SortOrder.Desc
          case _ =>
            SortOrder.Asc
        }
    }
    FieldSort(field, ordering)
  }

  def random(): ScriptSort = ScriptSort(script = InlineScript("Math.random()"))

  implicit final val decodeSorter: JsonDecoder[Sorter] =
    DeriveJsonDecoder
      .gen[Sorter]
      .orElse(JsonDecoder.string.map(v => new FieldSort(v)).widen[Sorter])
      //      .orElse(GeoDistanceSort.decodeScriptSort.widen[Sorter])
      .orElse(FieldSort.decodeFieldSort.widen[Sorter])
//    Json.decoder.mapOrFail { json =>
//      json match {
//        case Json.Str(value) => Right(new FieldSort(value))
//        case Json.Obj(fields) =>
//          fields.headOption match {
//            case Some((field, jValue)) =>
//              field match {
//                case "_geo_distance" => jValue.as[GeoDistanceSort]
//                case "_script"       => jValue.as[ScriptSort]
//                case _               => jValue.as[FieldSort]
//              }
//            case None => Left(s"Invalid values dict '$json' for Sorter")
//          }
//        case _ => Left(s"Invalid values '$json' for Sorter")
//      }
//    }
  //    JsonDecoder.instance { c =>
//      c.focus.get match {
//        case o: Json if o.isString =>
//          Right(new FieldSort(o.asString.get))
//        case o: Json if o.isObject =>
//          o.asObject.get.toList.head match {
//            case ("_geo_distance", jn) =>
//              //TODO fix safety
//              jn.as[GeoDistanceSort]
//            case ("_script", jn) =>
//              //TODO fix safety
//              jn.as[ScriptSort]
//            case (field, jn) =>
//              //TODO fix safety
//              o.as[FieldSort]
//
//          }
//      }
//    }

  implicit final val encodeSort: JsonEncoder[Sorter] = new JsonEncoder[Sorter] {
    override def unsafeEncode(a: Sorter, indent: Option[Int], out: Write): Unit = a match {
      case s: ScriptSort      => ScriptSort.encodeScriptSort.unsafeEncode(s, indent, out)
      case s: GeoDistanceSort => GeoDistanceSort.encodeScriptSort.unsafeEncode(s, indent, out)
      case s: FieldSort       => FieldSort.encodeFieldSort.unsafeEncode(s, indent, out)
    }
  }
//    JsonEncoder.instance {
//      case o: ScriptSort      => Json.Obj("_script" -> o.asJson)
//      case o: GeoDistanceSort => Json.Obj("_geo_distance" -> o.asJson)
//      case o: FieldSort       => o.asJson
//    }

}

@jsonHint("_script")
final case class ScriptSort(
  script: Script,
  `type`: String = "number",
  order: SortOrder = SortOrder.Asc,
  missing: Option[Json] = None,
  mode: Option[SortMode] = None,
  nestedPath: Option[String] = None
) extends Sorter

object ScriptSort {
  implicit final val decodeScriptSort: JsonDecoder[ScriptSort] = DeriveJsonDecoder.gen[ScriptSort]
//    JsonDecoder.instance { c =>
//      jObj.get[Script]("script") match {
//        case Left(left) =>
//          Left(left)
//        case Right(script) =>
//          Right(
//            new ScriptSort(
//              script = script,
//              order = c.getOption[SortOrder]("order").getOrElse(SortOrder.Asc),
//              nestedPath = c.getOption[String]("nested_path"),
//              `type` = c.getOption[String]("type").getOrElse("number"),
//              mode = c.getOption[SortMode]("mode"),
//              missing = c.getOption[Json]("missing")
//            )
//          )
//      }
//
//    }

  implicit final val encodeScriptSort: JsonEncoder[ScriptSort] = DeriveJsonEncoder.gen[ScriptSort]
//    JsonEncoder.instance { obj =>
//      val fields = new mutable.ListBuffer[(String, Json)]()
//      fields += ("script" -> obj.script.asJson)
//      fields += ("order" -> obj.order.asJson)
//      fields += ("type" -> obj.`type`.asJson)
//      obj.nestedPath.map(v => fields += ("nested_path" -> v.asJson))
//      obj.mode.map(v => fields += ("mode" -> v.asJson))
//      obj.missing.map(v => fields += ("missing" -> v.asJson))
//
//      Json.Obj(Chunk.fromIterable(fields))
//    }
}

@jsonHint("_geo_distance")
final case class GeoDistanceSort(
  field: String,
  points: List[GeoPoint],
  order: SortOrder = SortOrder.Asc,
  ignore_unmapped: Boolean = true,
  missing: Option[Json] = None,
  unit: Option[String] = None,
  mode: Option[SortMode] = None,
  @jsonField("nested_path") nestedPath: Option[String] = None,
  @jsonField("distance_type") distanceType: Option[DistanceType] = None
) extends Sorter

object GeoDistanceSort {
  private lazy val otherFields =
    List(
      "order",
      "ignore_unmapped",
      "missing",
      "unit",
      "mode",
      "nested_path",
      "distance_type"
    )

  implicit final val decodeScriptSort: JsonDecoder[GeoDistanceSort] = Json.Obj.decoder.mapOrFail { c =>
    c.keys.toList.diff(otherFields).headOption match {
      case None =>
        Left("Unable to detect the geo field")
      case Some(field) =>
        val points: List[GeoPoint] = c.getOption[Json](field).get match {
          case jo: Json.Arr if jo.elements.isEmpty => Nil
          case jo: Json.Arr if jo.elements.head.isInstanceOf[Json.Arr] =>
            jo.as[List[GeoPoint]].toOption.getOrElse(Nil)
          case jo: Json.Arr =>
            jo.as[GeoPoint].toOption.toList
          case jo: Json => jo.as[GeoPoint].toOption.toList
        }
        if (points.isEmpty) Left("GeoDistanceSort: missing reference geopoint")
        else
          Right(
            new GeoDistanceSort(
              field = field,
              points = points,
              order = c.getOption[SortOrder]("order").getOrElse(SortOrder.Asc),
              ignore_unmapped = c.getOption[Boolean]("ignore_unmapped").getOrElse(true),
              missing = c.getOption[Json]("missing"),
              unit = c.getOption[String]("unit"),
              mode = c.getOption[SortMode]("mode"),
              nestedPath = c.getOption[String]("nested_path"),
              distanceType = c.getOption[DistanceType]("distance_type")
            )
          )

    }
  }

  implicit final val encodeScriptSort: JsonEncoder[GeoDistanceSort] = Json.Obj.encoder.contramap { obj =>
    val fields = new mutable.ListBuffer[(String, Json)]()
    if (obj.points.length == 1) {
      fields += (obj.field -> obj.points.head.toJsonAST.toOption.get)
    } else {
      fields += (obj.field -> obj.points.toJsonAST.toOption.get)

    }
    fields += ("order" -> obj.order.toJsonAST.toOption.get)
    if (obj.ignore_unmapped)
      fields += ("ignore_unmapped" -> obj.ignore_unmapped.asJson)
    obj.missing.map(v => fields += ("missing" -> v))
    obj.unit.map(v => fields += ("unit" -> v.asJson))
    obj.mode.map(v => fields += ("mode" -> v.toJsonAST.toOption.get))
    obj.nestedPath.map(v => fields += ("nested_path" -> v.asJson))
    obj.distanceType.map(v => fields += ("distance_type" -> v.toJsonAST.toOption.get))

    Json.Obj("_geo_distance" -> Json.Obj(Chunk.fromIterable(fields)))
  }
}

final case class FieldSort(
  field: String = "",
  order: SortOrder = SortOrder.Asc,
  @jsonField("unmapped_type") unmappedType: Option[String] = None,
  @jsonField("nested_path") nestedPath: Option[String] = None,
  @jsonField("nested_filter") nestedFilter: Option[Query] = None,
  mode: Option[SortMode] = None,
  missing: Option[Json] = None
) extends Sorter {
  override def isShardDoc: Boolean = field == "_shard_doc"
}

object FieldSort {

  def apply(field: String, asc: Boolean): FieldSort =
    FieldSort(field, if (asc) SortOrder.Asc else SortOrder.Desc)

  implicit final val decodeFieldSort: JsonDecoder[FieldSort] = Json.Obj.decoder.mapOrFail { jObj =>
    jObj.fields.headOption match {
      case Some((field, jValue)) =>
        jValue match {
          case valueJson: Json.Obj =>
            Right(
              new FieldSort(
                field = field,
                order = valueJson.getOption[SortOrder]("order").getOrElse(SortOrder.Asc),
                unmappedType = valueJson.getOption[String]("unmapped_type"),
                nestedPath = valueJson.getOption[String]("nested_path"),
                nestedFilter = valueJson.getOption[Query]("nested_filter"),
                mode = valueJson.getOption[SortMode]("mode"),
                missing = valueJson.getOption[Json]("missing")
              )
            )
          case j: Json.Str =>
            Right(
              new FieldSort(
                field = field,
                j.as[SortOrder].toOption.getOrElse(SortOrder.Asc)
              )
            )

          case _ => Left(s"Invalid value $jValue for sorter")
        }
      case None => Left("Empty sort object")
    }
  }

  implicit final val encodeFieldSort: JsonEncoder[FieldSort] = Json.Obj.encoder.contramap { obj =>
    var jo = Json.Obj()
    jo = jo.add("order", obj.order.toJsonAST)
    obj.unmappedType.foreach(v => jo = jo.add("unmapped_type", v.asJson))
    obj.nestedPath.foreach(v => jo = jo.add("nested_path", v.asJson))
    obj.nestedFilter.foreach(v => jo = jo.add("nested_filter", v.toJsonAST))
    obj.mode.foreach(v => jo = jo.add("mode", v.toJsonAST))
    obj.missing.foreach(v => jo = jo.add("missing", v.toJsonAST))
    Json.Obj(obj.field -> jo)

  }
//    JsonEncoder.instance { obj =>
//      val fields = new mutable.ListBuffer[(String, Json)]()
//      fields += ("order" -> obj.order.asJson)
//      obj.unmappedType.map(v => fields += ("unmapped_type" -> v.asJson))
//      obj.nestedPath.map(v => fields += ("nested_path" -> v.asJson))
//      obj.nestedFilter.map(v => fields += ("nested_filter" -> v.asJson))
//      obj.mode.map(v => fields += ("mode" -> v.asJson))
//      obj.missing.map(v => fields += ("missing" -> v.asJson))
//
//      Json.Obj(obj.field -> Json.Obj(Chunk.fromIterable(fields)))
//    }

}
