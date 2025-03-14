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

package zio.opensearch.ml.preview_data_frame_analytics
import zio._
import zio.json._
import zio.json.ast._
/*
 * Previews that will be analyzed given a data frame analytics config.
 * For more info refers to http://www.elastic.co/guide/en/opensearch/reference/current/preview-dfanalytics.html
 *
 * @param featureValues An array of objects that contain feature name and value pairs. The features have been processed and indicate what will be sent to the model for training.

 */
final case class PreviewDataFrameAnalyticsResponse(
  featureValues: Chunk[Map[String, String]] = Chunk.empty[Map[String, String]]
) {}
object PreviewDataFrameAnalyticsResponse {
  implicit lazy val jsonCodec: JsonCodec[PreviewDataFrameAnalyticsResponse] =
    DeriveJsonCodec.gen[PreviewDataFrameAnalyticsResponse]
}
