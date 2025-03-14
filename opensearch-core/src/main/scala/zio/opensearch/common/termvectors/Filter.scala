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

package zio.opensearch.common.termvectors
import zio.json._
import zio.json.ast._
final case class Filter(
  @jsonField("max_doc_freq") maxDocFreq: Option[Int] = None,
  @jsonField("max_num_terms") maxNumTerms: Option[Int] = None,
  @jsonField("max_term_freq") maxTermFreq: Option[Int] = None,
  @jsonField("max_word_length") maxWordLength: Option[Int] = None,
  @jsonField("min_doc_freq") minDocFreq: Option[Int] = None,
  @jsonField("min_term_freq") minTermFreq: Option[Int] = None,
  @jsonField("min_word_length") minWordLength: Option[Int] = None
)

object Filter {
  implicit lazy val jsonCodec: JsonCodec[Filter] = DeriveJsonCodec.gen[Filter]
}
