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

package zio.resource

import scala.io.{ Codec, Source }

import zio._
import zio.exception.FrameworkException
import zio.json._
import zio.json.ast.Json

object ResourceService {

  trait ResourceService {
    def getFileAsString(name: String, codec: Codec = Codec.UTF8): ZIO[Any, Throwable, String]
    def getJson(name: String, codec: Codec = Codec.UTF8): ZIO[Any, Throwable, Json]
    def getJsonEntity[T: JsonDecoder](name: String, codec: Codec = Codec.UTF8): ZIO[Any, Throwable, T]
  }

  def getFileAsString(name: String, codec: Codec = Codec.UTF8): ZIO[ResourceService, Throwable, String] =
    ZIO.environmentWithZIO(_.get.getFileAsString(name, codec))

  def getJson(name: String, codec: Codec = Codec.UTF8): ZIO[ResourceService, Throwable, Json] =
    ZIO.environmentWithZIO(_.get.getJson(name, codec))

  def getJsonEntity[T: JsonDecoder](
    name: String,
    codec: Codec = Codec.UTF8
  ): ZIO[ResourceService, Throwable, T] =
    ZIO.environmentWithZIO(_.get.getJsonEntity[T](name, codec))

  object Live extends ResourceService {
    override def getFileAsString(name: String, codec: Codec = Codec.UTF8): Task[String] = {
      implicit val cdc: Codec = codec
      ZIO.attempt {
        val source = Source.fromInputStream(getClass.getResourceAsStream(name))
        val res = source.mkString
        source.close
        res
      }
    }

    override def getJson(name: String, codec: Codec = Codec.UTF8): Task[Json] =
      for {
        str <- getFileAsString(name, codec)
        json <- ZIO.fromEither(Json.decoder.decodeJson(str)).mapError(e => FrameworkException.jsonFailure(e))
      } yield json

    override def getJsonEntity[T](name: String, codec: Codec = Codec.UTF8)(implicit decoder: JsonDecoder[T]): Task[T] =
      for {
        str <- getFileAsString(name, codec)
        obj <- ZIO.fromEither(decoder.decodeJson(str)).mapError(e => FrameworkException.jsonFailure(e))
      } yield obj
  }

}
