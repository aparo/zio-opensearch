/*
 * Copyright 2019 Alberto Paro
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package elasticsearch.requests.ingest
import io.circe._
import io.circe.derivation.annotations.{ JsonCodec, JsonKey }
import scala.collection.mutable
import elasticsearch.requests.ActionRequest

/*
 * https://www.elastic.co/guide/en/elasticsearch/plugins/master/ingest.html
 *
 * @param id Pipeline ID
 * @param masterTimeout Explicit operation timeout for connection to master node
 * @param timeout Explicit operation timeout
 */
@JsonCodec
final case class DeletePipelineRequest(
  id: String,
  @JsonKey("master_timeout") masterTimeout: Option[String] = None,
  timeout: Option[String] = None
) extends ActionRequest {
  def method: String = "DELETE"

  def urlPath: String = this.makeUrl("_ingest", "pipeline", id)

  def queryArgs: Map[String, String] = {
    //managing parameters
    val queryArgs = new mutable.HashMap[String, String]()
    masterTimeout.map { v =>
      queryArgs += ("master_timeout" -> v.toString)
    }
    timeout.map { v =>
      queryArgs += ("timeout" -> v.toString)
    }
    // Custom Code On
    // Custom Code Off
    queryArgs.toMap
  }

  def body: Json = Json.Null

  // Custom Code On
  // Custom Code Off

}
