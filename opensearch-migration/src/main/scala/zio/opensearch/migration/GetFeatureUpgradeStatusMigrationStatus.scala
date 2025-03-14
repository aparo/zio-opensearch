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

package zio.opensearch.migration

import zio.json._

sealed trait GetFeatureUpgradeStatusMigrationStatus

object GetFeatureUpgradeStatusMigrationStatus {

  case object NO_MIGRATION_NEEDED extends GetFeatureUpgradeStatusMigrationStatus

  case object MIGRATION_NEEDED extends GetFeatureUpgradeStatusMigrationStatus

  case object IN_PROGRESS extends GetFeatureUpgradeStatusMigrationStatus

  case object ERROR extends GetFeatureUpgradeStatusMigrationStatus

  implicit final val decoder: JsonDecoder[GetFeatureUpgradeStatusMigrationStatus] =
    DeriveJsonDecoderEnum.gen[GetFeatureUpgradeStatusMigrationStatus]
  implicit final val encoder: JsonEncoder[GetFeatureUpgradeStatusMigrationStatus] =
    DeriveJsonEncoderEnum.gen[GetFeatureUpgradeStatusMigrationStatus]
  implicit final val codec: JsonCodec[GetFeatureUpgradeStatusMigrationStatus] =
    JsonCodec(encoder, decoder)

}
