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

import zio.json._
/*
 * The _upgrade API is no longer useful and will be removed.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/master/indices-upgrade.html
 *
 * @param allowNoIndices Whether to ignore if a wildcard indices expression resolves into no concrete indices. (This includes `_all` string or when no indices have been specified)
 * @param expandWildcards Whether to expand wildcard expression to concrete indices that are open, closed or both.
 * @param ignoreUnavailable Whether specified concrete indices should be ignored when unavailable (missing or closed)
 * @param indices A comma-separated list of index names; use `_all` or empty string to perform the operation on all indices
 * @param onlyAncientSegments If true, only ancient (an older Lucene major release) segments will be upgraded
 * @param waitForCompletion Specify whether the request should block until the all segments are upgraded (default: false)
 */
final case class IndicesUpgradeResponse(_ok: Option[Boolean] = None)
object IndicesUpgradeResponse {
  implicit val jsonDecoder: JsonDecoder[IndicesUpgradeResponse] = DeriveJsonDecoder.gen[IndicesUpgradeResponse]
  implicit val jsonEncoder: JsonEncoder[IndicesUpgradeResponse] = DeriveJsonEncoder.gen[IndicesUpgradeResponse]
}
