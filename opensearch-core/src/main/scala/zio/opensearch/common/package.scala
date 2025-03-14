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

package zio.opensearch

import zio.Chunk
import zio.json.ast.Json

package object common {

  type CronExpression = String

  type DataStreamName = String

  type ScrollId = String

  type Username = String
  type Password = String

  type AggregateName = String
  type SuggestionName = String
  type TrackHits = Boolean

  type Aggregate = zio.opensearch.responses.aggregations.Aggregation
  type DataStreamNames = Chunk[String]

  type PipelineName = String
  type Routing = String

  type TransportAddress = String
  type NodeName = String
  type NodeId = String

  type NodeIds = Chunk[NodeId] // | NodeId[]

  type NodeRoles = Chunk[NodeRole]
  type ScrollIds = Chunk[String]

  type Time = String

  type DateFormat = String

  type DateMath = String

  type DateTime = String //string | EpochTime<UnitMillis>

  type Distance = String

  type Indices = Chunk[String]
  type Names = Chunk[String]
  type Metadata = Map[String, String]

  type WaitForActiveShards = Json //integer | WaitForActiveShardOptions
  type Percentage = Json //string | float
  type Duration = Json //string | -1 | 0

  type DurationLarge = String

  type RuntimeFields = Json
  type TimeZone = Json
  type FieldAndFormat = Json

  type TPartialDocument = Json
  type TDocument = Json

  type QueryVector = Chunk[Double]

  type MinimumShouldMatch = Int

  type GeoHashPrecision = Json

  type SortResults = Chunk[Json]

  type MinimumInterval = String // 'second' | 'minute' | 'hour' | 'day' | 'month' | 'year'

  type Fuzziness = Json

  type Missing = Json //string | integer | double | boolean

  type GridAggregationType = String //'geotile' | 'geohex'

  type IntervalUnit = Json

}
