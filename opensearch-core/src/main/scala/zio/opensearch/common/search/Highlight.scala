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

package zio.opensearch.common.search

import zio._
import zio.opensearch.common._
import zio.opensearch.queries.Query
import zio.json._
import zio.json.ast._
final case class Highlight(
  fields: Map[String, HighlightField] = Map.empty,
  @jsonField("type") `type`: Option[HighlighterType] = None,
  @jsonField("boundary_chars") boundaryChars: Option[String] = None,
  @jsonField("boundary_max_scan") boundaryMaxScan: Option[Int] = None,
  @jsonField("boundary_scanner") boundaryScanner: Option[BoundaryScanner] = None,
  @jsonField("boundary_scanner_locale") boundaryScannerLocale: Option[
    String
  ] = None,
  encoder: Option[HighlighterEncoder] = None,
  @jsonField("force_source") forceSource: Option[Boolean] = None,
  fragmenter: Option[HighlighterFragmenter] = None,
  @jsonField("fragment_size") fragmentSize: Option[Int] = None,
  @jsonField("highlight_filter") highlightFilter: Option[Boolean] = None,
  @jsonField("highlight_query") highlightQuery: Option[Query] = None,
  @jsonField("max_fragment_length") maxFragmentLength: Option[Int] = None,
  @jsonField("max_analyzed_offset") maxAnalyzedOffset: Option[Int] = None,
  @jsonField("no_match_size") noMatchSize: Option[Int] = None,
  @jsonField("number_of_fragments") numberOfFragments: Option[Int] = None,
  options: Option[Map[String, Json]] = None,
  order: Option[HighlighterOrder] = None,
  @jsonField("phrase_limit") phraseLimit: Option[Int] = None,
  @jsonField("post_tags") postTags: Option[Chunk[String]] = None,
  @jsonField("pre_tags") preTags: Option[Chunk[String]] = None,
  @jsonField("require_field_match") requireFieldMatch: Option[Boolean] = None,
  @jsonField("tags_schema") tagsSchema: Option[HighlighterTagsSchema] = None
)

object Highlight {
  implicit lazy val jsonCodec: JsonCodec[Highlight] = DeriveJsonCodec.gen[Highlight]
}
