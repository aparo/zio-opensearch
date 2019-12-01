/*
 * Copyright 2019 Alberto Paro
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package elasticsearch.requests
import scala.collection.mutable
import elasticsearch.Refresh
import io.circe.derivation.annotations.{ JsonCodec, JsonKey }

/*
 * Allows to perform multiple index/update/delete operations in a single request.
 * For more info refers to https://www.elastic.co/guide/en/elasticsearch/reference/master/docs-bulk.html
 *
 * @param body body the body of the call
 * @param docType Default document type for items which don't provide one
 * @param index Default index for items which don't provide one
 * @param pipeline The pipeline id to preprocess incoming documents with
 * @param refresh If `true` then refresh the effected shards to make this operation visible to search, if `wait_for` then wait for a refresh to make this operation visible to search, if `false` (the default) then do nothing with refreshes.
 * @param routing Specific routing value
 * @param source True or false to return the _source field or not, or default list of fields to return, can be overridden on each sub-request
 * @param sourceExcludes Default list of fields to exclude from the returned _source field, can be overridden on each sub-request
 * @param sourceIncludes Default list of fields to extract and return from the _source field, can be overridden on each sub-request
 * @param timeout Explicit operation timeout
 * @param `type` Default document type for items which don't provide one
 * @param waitForActiveShards Sets the number of shard copies that must be active before proceeding with the bulk operation. Defaults to 1, meaning the primary shard only. Set to `all` for all shard copies, otherwise set to any non-negative value less than or equal to the total number of copies for the shard (number of replicas + 1)
 */
@JsonCodec
final case class BulkRequest(
  body: String,
  docType: Option[String] = None,
  index: Option[String] = None,
  pipeline: Option[String] = None,
  refresh: Option[Refresh] = None,
  routing: Option[String] = None,
  @JsonKey("_source") source: Seq[String] = Nil,
  @JsonKey("_source_excludes") sourceExcludes: Seq[String] = Nil,
  @JsonKey("_source_includes") sourceIncludes: Seq[String] = Nil,
  timeout: Option[String] = None,
  @JsonKey("type") `type`: Option[String] = None,
  @JsonKey("wait_for_active_shards") waitForActiveShards: Option[String] = None
) extends ActionRequest {
  def method: String = "POST"

  def urlPath: String = this.makeUrl(index, docType, "_bulk")

  def queryArgs: Map[String, String] = {
    //managing parameters
    val queryArgs = new mutable.HashMap[String, String]()
    pipeline.foreach { v =>
      queryArgs += ("pipeline" -> v)
    }
    refresh.foreach { v =>
      queryArgs += ("refresh" -> v.toString)
    }
    routing.foreach { v =>
      queryArgs += ("routing" -> v)
    }
    if (source.nonEmpty) {
      queryArgs += ("_source" -> source.toList.mkString(","))
    }
    if (sourceExcludes.nonEmpty) {
      queryArgs += ("_source_excludes" -> sourceExcludes.toList.mkString(","))
    }
    if (sourceIncludes.nonEmpty) {
      queryArgs += ("_source_includes" -> sourceIncludes.toList.mkString(","))
    }
    timeout.foreach { v =>
      queryArgs += ("timeout" -> v.toString)
    }
    `type`.foreach { v =>
      queryArgs += ("type" -> v)
    }
    waitForActiveShards.foreach { v =>
      queryArgs += ("wait_for_active_shards" -> v)
    }
    // Custom Code On
    // Custom Code Off
    queryArgs.toMap
  }

  // Custom Code On
  // Custom Code Off

}
