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

package zio.exception

// we move the exception in this package to simplify every access using app.exception._
import zio.ZIO
import zio.opensearch.client.ESResponse
import zio.opensearch.common.bulk.BulkResponse
import zio.opensearch.responses.ErrorResponse
import zio.json._
import zio.json.ast.Json

/**
 * ************************************** OpenSearch Exceptions
 */
sealed trait OpenSearchException extends FrameworkException {

  override def toJsonWithFamily: Either[String, Json] = for {
    json <- implicitly[JsonEncoder[OpenSearchException]].toJsonAST(this)
    jsonFamily <- addFamily(json, "OpenSearchSearchException")
  } yield jsonFamily

}

object OpenSearchException extends ExceptionFamily {
  register("OpenSearchSearchException", this)
  implicit final val jsonDecoder: JsonDecoder[OpenSearchException] =
    DeriveJsonDecoder.gen[OpenSearchException]
  implicit final val jsonEncoder: JsonEncoder[OpenSearchException] =
    DeriveJsonEncoder.gen[OpenSearchException]
  implicit final val jsonCodec: JsonCodec[OpenSearchException] = JsonCodec(jsonEncoder, jsonDecoder)

  override def decode(c: Json): Either[String, FrameworkException] =
    implicitly[JsonDecoder[OpenSearchException]].fromJsonAST(c)

//  implicit def convertDecodeError(
//    error: DecodingFailure
//  ): OpenSearchParsingException =
//    new OpenSearchParsingException(error.message)

  def apply(msg: String, status: Int, json: Json) =
    new SearchPhaseExecutionException(msg, status, json = json)

  def buildException(osResponse: ESResponse): FrameworkException =
    osResponse.body.toJsonAST match {
      case Left(value)  => InvalidJsonException(value)
      case Right(value) => buildException(value, osResponse.status)
    }

  /*
   * Build an error
   */
  def buildException(data: Json, status: Int = ErrorCode.InternalServerError): FrameworkException =
    data match {
      case Json.Null =>
        if (status == 404) new NotFoundException(s"Error $status", json = data)
        else
          new NotFoundException(s"Error $status", json = data) //TODO improve
      case d: Json.Obj =>
        d.as[ErrorResponse] match {
          case Left(ex) =>
            OpenSearchQueryException(
              d.toJson,
              status = status,
              json = data
            )

          case Right(errorResponse) =>
            val errorType = errorResponse.error.`type`
            errorType match {
              case _ =>
                OpenSearchQueryException(
                  errorResponse.error.reason,
                  status = status,
                  json = data
                )
            }
        }
      case default =>
        InvalidValueException(s"Not valid value $default")

    }
  //  private def removeErrorType(errorType: String, str: String): String =
  //    str.substring(errorType.size + 1, str.size - 2)
}

final case class MultiDocumentException(
  message: String,
  json: Json = Json.Null,
  status: Int = ErrorCode.InternalServerError,
  errorType: ErrorType = ErrorType.ValidationError,
  errorCode: String = "opensearch.multiple",
  stacktrace: Option[String] = None
) extends OpenSearchException
object MultiDocumentException {
  implicit val jsonDecoder: JsonDecoder[MultiDocumentException] = DeriveJsonDecoder.gen[MultiDocumentException]
  implicit val jsonEncoder: JsonEncoder[MultiDocumentException] = DeriveJsonEncoder.gen[MultiDocumentException]
}

final case class OpenSearchIllegalStateException(
  message: String,
  errorType: ErrorType = ErrorType.UnknownError,
  errorCode: String = "opensearch.error",
  status: Int = ErrorCode.InternalServerError,
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object OpenSearchIllegalStateException {
  implicit val jsonDecoder: JsonDecoder[OpenSearchIllegalStateException] =
    DeriveJsonDecoder.gen[OpenSearchIllegalStateException]
  implicit val jsonEncoder: JsonEncoder[OpenSearchIllegalStateException] =
    DeriveJsonEncoder.gen[OpenSearchIllegalStateException]
}

final case class OpenSearchParsingException(
  message: String,
  errorType: ErrorType = ErrorType.ValidationError,
  errorCode: String = "opensearch.parsing",
  status: Int = ErrorCode.InternalServerError,
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object OpenSearchParsingException {
  implicit val jsonDecoder: JsonDecoder[OpenSearchParsingException] =
    DeriveJsonDecoder.gen[OpenSearchParsingException]
  implicit val jsonEncoder: JsonEncoder[OpenSearchParsingException] =
    DeriveJsonEncoder.gen[OpenSearchParsingException]
}

final case class OpenSearchDeleteException(
  message: String,
  status: Int = ErrorCode.InternalServerError,
  errorType: ErrorType = ErrorType.UnknownError,
  errorCode: String = "opensearch.delete",
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object OpenSearchDeleteException {
  implicit val jsonDecoder: JsonDecoder[OpenSearchDeleteException] =
    DeriveJsonDecoder.gen[OpenSearchDeleteException]
  implicit val jsonEncoder: JsonEncoder[OpenSearchDeleteException] =
    DeriveJsonEncoder.gen[OpenSearchDeleteException]
}

final case class OpenSearchScriptException(
  message: String,
  errorType: ErrorType = ErrorType.ValidationError,
  errorCode: String = "opensearch.script",
  status: Int = ErrorCode.InternalServerError,
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object OpenSearchScriptException {
  implicit val jsonDecoder: JsonDecoder[OpenSearchScriptException] =
    DeriveJsonDecoder.gen[OpenSearchScriptException]
  implicit val jsonEncoder: JsonEncoder[OpenSearchScriptException] =
    DeriveJsonEncoder.gen[OpenSearchScriptException]
}

final case class OpenSearchQueryException(
  message: String,
  errorType: ErrorType = ErrorType.ValidationError,
  errorCode: String = "opensearch.query",
  status: Int = ErrorCode.InternalServerError,
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object OpenSearchQueryException {
  implicit val jsonDecoder: JsonDecoder[OpenSearchQueryException] =
    DeriveJsonDecoder.gen[OpenSearchQueryException]
  implicit val jsonEncoder: JsonEncoder[OpenSearchQueryException] =
    DeriveJsonEncoder.gen[OpenSearchQueryException]
}
final case class InvalidQueryException(
  message: String,
  errorType: ErrorType = ErrorType.ValidationError,
  errorCode: String = "opensearch.invalidquery",
  status: Int = ErrorCode.InternalServerError,
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object InvalidQueryException {
  implicit val jsonDecoder: JsonDecoder[InvalidQueryException] = DeriveJsonDecoder.gen[InvalidQueryException]
  implicit val jsonEncoder: JsonEncoder[InvalidQueryException] = DeriveJsonEncoder.gen[InvalidQueryException]
}

final case class InvalidParameterQueryException(
  message: String,
  errorType: ErrorType = ErrorType.ValidationError,
  errorCode: String = "opensearch.invalidquery",
  status: Int = ErrorCode.InternalServerError,
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object InvalidParameterQueryException {
  implicit val jsonDecoder: JsonDecoder[InvalidParameterQueryException] =
    DeriveJsonDecoder.gen[InvalidParameterQueryException]
  implicit val jsonEncoder: JsonEncoder[InvalidParameterQueryException] =
    DeriveJsonEncoder.gen[InvalidParameterQueryException]
}

final case class QueryException(
  message: String,
  status: Int,
  errorType: ErrorType = ErrorType.ValidationError,
  errorCode: String = "opensearch.invalidquery",
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object QueryException {
  implicit val jsonDecoder: JsonDecoder[QueryException] = DeriveJsonDecoder.gen[QueryException]
  implicit val jsonEncoder: JsonEncoder[QueryException] = DeriveJsonEncoder.gen[QueryException]
}

final case class QueryParameterException(
  message: String,
  status: Int,
  errorType: ErrorType = ErrorType.ValidationError,
  errorCode: String = "opensearch.invalidquery",
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object QueryParameterException {
  implicit val jsonDecoder: JsonDecoder[QueryParameterException] = DeriveJsonDecoder.gen[QueryParameterException]
  implicit val jsonEncoder: JsonEncoder[QueryParameterException] = DeriveJsonEncoder.gen[QueryParameterException]
}

final case class ScriptFieldsException(
  message: String,
  status: Int,
  errorType: ErrorType = ErrorType.ValidationError,
  errorCode: String = "opensearch.invalidquery",
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object ScriptFieldsException {
  implicit val jsonDecoder: JsonDecoder[ScriptFieldsException] = DeriveJsonDecoder.gen[ScriptFieldsException]
  implicit val jsonEncoder: JsonEncoder[ScriptFieldsException] = DeriveJsonEncoder.gen[ScriptFieldsException]
}

/**
 * This class defines a InvalidParameter entity
 * @param message
 *   the error message
 * @param errorType
 *   the errorType
 * @param errorCode
 *   a string grouping common application errors
 * @param status
 *   HTTP Error Status
 * @param json
 *   a Json entity
 */
final case class InvalidParameterException(
  message: String,
  errorType: ErrorType = ErrorType.ValidationError,
  errorCode: String = "opensearch.invalidparameter",
  status: Int = ErrorCode.BadRequest,
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object InvalidParameterException {
  implicit val jsonDecoder: JsonDecoder[InvalidParameterException] = DeriveJsonDecoder.gen[InvalidParameterException]
  implicit val jsonEncoder: JsonEncoder[InvalidParameterException] = DeriveJsonEncoder.gen[InvalidParameterException]
}

final case class MergeMappingException(
  message: String,
  solution: String,
  errorType: ErrorType = ErrorType.ValidationError,
  errorCode: String = "opensearch.mapping",
  status: Int = ErrorCode.InternalServerError,
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object MergeMappingException {
  implicit val jsonDecoder: JsonDecoder[MergeMappingException] = DeriveJsonDecoder.gen[MergeMappingException]
  implicit val jsonEncoder: JsonEncoder[MergeMappingException] = DeriveJsonEncoder.gen[MergeMappingException]
}
final case class OpenSearchSearchIllegalArgumentException(
  message: String,
  status: Int,
  errorType: ErrorType = ErrorType.ValidationError,
  errorCode: String = "opensearch.invaliddata",
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object OpenSearchSearchIllegalArgumentException {
  implicit val jsonDecoder: JsonDecoder[OpenSearchSearchIllegalArgumentException] =
    DeriveJsonDecoder.gen[OpenSearchSearchIllegalArgumentException]
  implicit val jsonEncoder: JsonEncoder[OpenSearchSearchIllegalArgumentException] =
    DeriveJsonEncoder.gen[OpenSearchSearchIllegalArgumentException]
}

final case class IndexNotFoundException(
  message: String,
  status: Int = 404,
  errorType: ErrorType = ErrorType.ValidationError,
  errorCode: String = "opensearch.missing",
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object IndexNotFoundException {
  implicit val jsonDecoder: JsonDecoder[IndexNotFoundException] = DeriveJsonDecoder.gen[IndexNotFoundException]
  implicit val jsonEncoder: JsonEncoder[IndexNotFoundException] = DeriveJsonEncoder.gen[IndexNotFoundException]
}

/**
 * This class defines a VersionConflictEngineException entity
 * @param message
 *   the error message
 * @param errorType
 *   the errorType
 * @param errorCode
 *   a string grouping common application errors
 * @param status
 *   HTTP Error Status
 * @param json
 *   a Json entity
 */
final case class IndexAlreadyExistsException(
  message: String,
  status: Int = 409,
  errorType: ErrorType = ErrorType.ValidationError,
  errorCode: String = "opensearch.exists",
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object IndexAlreadyExistsException {
  implicit val jsonDecoder: JsonDecoder[IndexAlreadyExistsException] =
    DeriveJsonDecoder.gen[IndexAlreadyExistsException]
  implicit val jsonEncoder: JsonEncoder[IndexAlreadyExistsException] =
    DeriveJsonEncoder.gen[IndexAlreadyExistsException]
}

final case class SearchPhaseExecutionException(
  message: String,
  status: Int,
  errorType: ErrorType = ErrorType.ValidationError,
  errorCode: String = "opensearch.search",
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object SearchPhaseExecutionException {
  implicit val jsonDecoder: JsonDecoder[SearchPhaseExecutionException] =
    DeriveJsonDecoder.gen[SearchPhaseExecutionException]
  implicit val jsonEncoder: JsonEncoder[SearchPhaseExecutionException] =
    DeriveJsonEncoder.gen[SearchPhaseExecutionException]
}

final case class ReplicationShardOperationFailedException(
  message: String,
  status: Int,
  errorType: ErrorType = ErrorType.ValidationError,
  errorCode: String = "opensearch.search",
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object ReplicationShardOperationFailedException {
  implicit val jsonDecoder: JsonDecoder[ReplicationShardOperationFailedException] =
    DeriveJsonDecoder.gen[ReplicationShardOperationFailedException]
  implicit val jsonEncoder: JsonEncoder[ReplicationShardOperationFailedException] =
    DeriveJsonEncoder.gen[ReplicationShardOperationFailedException]
}

final case class ClusterBlockException(
  message: String,
  status: Int,
  errorType: ErrorType = ErrorType.ValidationError,
  errorCode: String = "opensearch.cluster",
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object ClusterBlockException {
  implicit val jsonDecoder: JsonDecoder[ClusterBlockException] = DeriveJsonDecoder.gen[ClusterBlockException]
  implicit val jsonEncoder: JsonEncoder[ClusterBlockException] = DeriveJsonEncoder.gen[ClusterBlockException]
}

final case class MapperParsingException(
  message: String,
  status: Int,
  errorType: ErrorType = ErrorType.ValidationError,
  errorCode: String = "opensearch.mapping",
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object MapperParsingException {
  implicit val jsonDecoder: JsonDecoder[MapperParsingException] = DeriveJsonDecoder.gen[MapperParsingException]
  implicit val jsonEncoder: JsonEncoder[MapperParsingException] = DeriveJsonEncoder.gen[MapperParsingException]
}

final case class ReduceSearchPhaseException(
  message: String,
  status: Int,
  errorType: ErrorType = ErrorType.ValidationError,
  errorCode: String = "opensearch.search",
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object ReduceSearchPhaseException {
  implicit val jsonDecoder: JsonDecoder[ReduceSearchPhaseException] = DeriveJsonDecoder.gen[ReduceSearchPhaseException]
  implicit val jsonEncoder: JsonEncoder[ReduceSearchPhaseException] = DeriveJsonEncoder.gen[ReduceSearchPhaseException]
}

final case class TypeMissingException(
  message: String,
  status: Int = 404,
  errorType: ErrorType = ErrorType.ValidationError,
  errorCode: String = "opensearch.type",
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object TypeMissingException {
  implicit val jsonDecoder: JsonDecoder[TypeMissingException] = DeriveJsonDecoder.gen[TypeMissingException]
  implicit val jsonEncoder: JsonEncoder[TypeMissingException] = DeriveJsonEncoder.gen[TypeMissingException]
}

//mappings

final case class MappedFieldNotFoundException(
  message: String,
  status: Int,
  errorType: ErrorType = ErrorType.ValidationError,
  errorCode: String = "opensearch.mapping",
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object MappedFieldNotFoundException {
  implicit val jsonDecoder: JsonDecoder[MappedFieldNotFoundException] =
    DeriveJsonDecoder.gen[MappedFieldNotFoundException]
  implicit val jsonEncoder: JsonEncoder[MappedFieldNotFoundException] =
    DeriveJsonEncoder.gen[MappedFieldNotFoundException]
}
final case class BulkException(
  bulkResult: BulkResponse,
  message: String = "Bulk exception in processing some records",
  status: Int = 500,
  errorType: ErrorType = ErrorType.ValidationError,
  errorCode: String = "opensearch.bulk",
  json: Json = Json.Null,
  stacktrace: Option[String] = None
) extends OpenSearchException
object BulkException {
  implicit val jsonDecoder: JsonDecoder[BulkException] = DeriveJsonDecoder.gen[BulkException]
  implicit val jsonEncoder: JsonEncoder[BulkException] = DeriveJsonEncoder.gen[BulkException]
}
