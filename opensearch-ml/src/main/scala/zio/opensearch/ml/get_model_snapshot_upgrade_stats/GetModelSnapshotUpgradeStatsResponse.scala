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

package zio.opensearch.ml.get_model_snapshot_upgrade_stats
import zio._
import zio.opensearch.ml.ModelSnapshotUpgrade
import zio.json._
import zio.json.ast._
/*
 * Gets stats for anomaly detection job model snapshot upgrades that are in progress.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/ml-get-job-model-snapshot-upgrade-stats.html
 *
 * @param count

 * @param modelSnapshotUpgrades

 */
final case class GetModelSnapshotUpgradeStatsResponse(
  count: Long,
  modelSnapshotUpgrades: Chunk[ModelSnapshotUpgrade] = Chunk.empty[ModelSnapshotUpgrade]
) {}
object GetModelSnapshotUpgradeStatsResponse {
  implicit lazy val jsonCodec: JsonCodec[GetModelSnapshotUpgradeStatsResponse] =
    DeriveJsonCodec.gen[GetModelSnapshotUpgradeStatsResponse]
}
