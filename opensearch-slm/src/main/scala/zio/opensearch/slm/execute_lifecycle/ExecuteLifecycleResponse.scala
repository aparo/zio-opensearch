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

package zio.opensearch.slm.execute_lifecycle
import zio.json._
import zio.json.ast._
/*
 * Immediately creates a snapshot according to the lifecycle policy, without waiting for the scheduled time.
 * For more info refers to https://www.elastic.co/guide/en/opensearch/reference/current/slm-api-execute-lifecycle.html
 *
 * @param snapshotName

 */
final case class ExecuteLifecycleResponse(snapshotName: String) {}
object ExecuteLifecycleResponse {
  implicit lazy val jsonCodec: JsonCodec[ExecuteLifecycleResponse] =
    DeriveJsonCodec.gen[ExecuteLifecycleResponse]
}
