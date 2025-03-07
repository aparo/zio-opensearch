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

package zio.opensearch.ml.get_job_stats
import zio._
import zio.opensearch.ml.JobStats
import zio.json._
import zio.json.ast._
/*
 * Retrieves usage information for anomaly detection jobs.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/ml-get-job-stats.html
 *
 * @param count

 * @param jobs

 */
final case class GetJobStatsResponse(
  count: Long,
  jobs: Chunk[JobStats] = Chunk.empty[JobStats]
) {}
object GetJobStatsResponse {
  implicit lazy val jsonCodec: JsonCodec[GetJobStatsResponse] =
    DeriveJsonCodec.gen[GetJobStatsResponse]
}
