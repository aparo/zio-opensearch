/*
 * Copyright 2019 Alberto Paro
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

package zio.schema

import zio.json.ast.Json
import zio.json._
import zio.json._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ParsingSpec extends AnyFlatSpec with Matchers {

  implicit val printer = Printer.noSpaces.copy(dropNullValues = true)

  behavior.of("ParsingSpec")

  "Schema" should "deserialize" in {
    val json =
      parser.parse("""
                              |{
                              |  "type": "object",
                              |  "name": "analytic",
                              |  "module": "opensearch",
                              |  "class_name": "zio.opensearch.models.Analytic",
                              |  "is_root": true,
                              |  "properties": [
                              |    {
                              |      "type": "string",
                              |      "index": {
                              |        "analyzers": [
                              |          "keyword"
                              |        ]
                              |      },
                              |      "name": "name"
                              |    },
                              |    {
                              |      "type": "object",
                              |      "name": "queryContext",
                              |      "module": "opensearch",
                              |      "class_name": "zio.opensearch.models.QueryViewQuery",
                              |      "is_root": true,
                              |      "properties": [
                              |        {
                              |          "format": "list",
                              |          "items": {
                              |            "type": "object",
                              |            "format": "json",
                              |            "name": "queries"
                              |          },
                              |          "multiple": true,
                              |          "type": "array",
                              |          "required": false,
                              |          "default": [
                              |          ],
                              |          "name": "queries"
                              |        },
                              |        {
                              |          "format": "list",
                              |          "items": {
                              |            "type": "object",
                              |            "format": "json",
                              |            "name": "postFilters"
                              |          },
                              |          "multiple": true,
                              |          "type": "array",
                              |          "required": false,
                              |          "default": [
                              |          ],
                              |          "name": "postFilters"
                              |        },
                              |        {
                              |          "format": "list",
                              |          "items": {
                              |            "type": "object",
                              |            "format": "json",
                              |            "name": "filters"
                              |          },
                              |          "multiple": true,
                              |          "type": "array",
                              |          "required": false,
                              |          "default": [
                              |          ],
                              |          "name": "filters"
                              |        },
                              |        {
                              |          "format": "list",
                              |          "items": {
                              |            "type": "string",
                              |            "name": "defaultSearchFields"
                              |          },
                              |          "multiple": true,
                              |          "type": "array",
                              |          "required": false,
                              |          "default": [
                              |            "_all"
                              |          ],
                              |          "name": "defaultSearchFields"
                              |        },
                              |        {
                              |          "format": "list",
                              |          "items": {
                              |            "type": "object",
                              |            "format": "json",
                              |            "name": "fixedFilters"
                              |          },
                              |          "multiple": true,
                              |          "type": "array",
                              |          "required": false,
                              |          "default": [
                              |          ],
                              |          "name": "fixedFilters"
                              |        },
                              |        {
                              |          "type": "integer",
                              |          "format": "int32",
                              |          "default": 10,
                              |          "name": "itemsPerPage"
                              |        },
                              |        {
                              |          "format": "list",
                              |          "items": {
                              |            "type": "object",
                              |            "format": "json",
                              |            "name": "sort"
                              |          },
                              |          "multiple": true,
                              |          "type": "array",
                              |          "required": false,
                              |          "default": [
                              |          ],
                              |          "name": "sort"
                              |        }
                              |      ],
                              |      "id": "zio.opensearch.models.QueryViewQuery",
                              |      "index": {
                              |        "index": false
                              |      }
                              |    },
                              |    {
                              |      "type": "integer",
                              |      "format": "int32",
                              |      "required": false,
                              |      "multiple": false,
                              |      "name": "refreshInterval"
                              |    },
                              |    {
                              |      "type": "object",
                              |      "name": "aggregation",
                              |      "module": "opensearch",
                              |      "class_name": "zio.opensearch.models.QueryViewAggregation",
                              |      "is_root": true,
                              |      "properties": [
                              |        {
                              |          "type": "string",
                              |          "index": {
                              |            "analyzers": [
                              |              "keyword"
                              |            ]
                              |          },
                              |          "name": "name"
                              |        },
                              |        {
                              |          "type": "boolean",
                              |          "default": true,
                              |          "name": "show"
                              |        },
                              |        {
                              |          "type": "boolean",
                              |          "default": true,
                              |          "name": "filter"
                              |        },
                              |        {
                              |          "type": "boolean",
                              |          "default": false,
                              |          "name": "global"
                              |        },
                              |        {
                              |          "type": "object",
                              |          "name": "chartOptions",
                              |          "module": "opensearch",
                              |          "class_name": "zio.opensearch.models.ChartOptions",
                              |          "is_root": true,
                              |          "properties": [
                              |            {
                              |              "format": "seq",
                              |              "items": {
                              |                "type": "string",
                              |                "enum": [
                              |                  "pie",
                              |                  "pie3d",
                              |                  "bars",
                              |                  "bars3d",
                              |                  "line",
                              |                  "folsonomy"
                              |                ],
                              |                "name": "charts"
                              |              },
                              |              "multiple": true,
                              |              "type": "array",
                              |              "required": false,
                              |              "name": "charts"
                              |            },
                              |            {
                              |              "type": "string",
                              |              "required": false,
                              |              "multiple": false,
                              |              "name": "aggregationPath"
                              |            },
                              |            {
                              |              "type": "string",
                              |              "enum": [
                              |                "pie",
                              |                "pie3d",
                              |                "bars",
                              |                "bars3d",
                              |                "line",
                              |                "folsonomy"
                              |              ],
                              |              "name": "selectedChart"
                              |            },
                              |            {
                              |              "type": "integer",
                              |              "format": "int32",
                              |              "default": 6,
                              |              "name": "spanSize"
                              |            },
                              |            {
                              |              "type": "string",
                              |              "required": false,
                              |              "multiple": false,
                              |              "name": "chartTitle"
                              |            },
                              |            {
                              |              "type": "string",
                              |              "required": false,
                              |              "multiple": false,
                              |              "name": "title"
                              |            }
                              |          ],
                              |          "id": "zio.opensearch.models.ChartOptions"
                              |        },
                              |        {
                              |          "type": "object",
                              |          "format": "json",
                              |          "name": "aggregation"
                              |        },
                              |        {
                              |          "type": "boolean",
                              |          "default": true,
                              |          "name": "active"
                              |        }
                              |      ],
                              |      "required": false,
                              |      "multiple": false,
                              |      "id": "zio.opensearch.models.QueryViewAggregation",
                              |      "index": {
                              |        "index": false
                              |      }
                              |    },
                              |    {
                              |      "format": "list",
                              |      "items": {
                              |        "type": "object",
                              |        "name": "filters",
                              |        "module": "opensearch",
                              |        "class_name": "zio.opensearch.models.QueryViewFilter",
                              |        "is_root": true,
                              |        "properties": [
                              |          {
                              |            "type": "string",
                              |            "name": "field"
                              |          },
                              |          {
                              |            "type": "string",
                              |            "required": false,
                              |            "multiple": false,
                              |            "name": "kind"
                              |          },
                              |          {
                              |            "type": "string",
                              |            "name": "type"
                              |          },
                              |          {
                              |            "type": "string",
                              |            "required": false,
                              |            "multiple": false,
                              |            "name": "aggregationName"
                              |          },
                              |          {
                              |            "type": "boolean",
                              |            "default": true,
                              |            "name": "active"
                              |          }
                              |        ],
                              |        "id": "zio.opensearch.models.QueryViewFilter"
                              |      },
                              |      "multiple": true,
                              |      "type": "array",
                              |      "required": false,
                              |      "default": [
                              |      ],
                              |      "index": {
                              |        "index": false
                              |      },
                              |      "name": "filters"
                              |    },
                              |    {
                              |      "type": "string",
                              |      "required": false,
                              |      "multiple": false,
                              |      "name": "title"
                              |    },
                              |    {
                              |      "type": "string",
                              |      "default": "",
                              |      "index": {
                              |        "analyzers": [
                              |          "keyword"
                              |        ]
                              |      },
                              |      "name": "type"
                              |    },
                              |    {
                              |      "type": "object",
                              |      "oneOf": [
                              |        {
                              |          "type": "object",
                              |          "name": "no_render",
                              |          "module": "opensearch",
                              |          "class_name": "zio.opensearch.models.NoRender",
                              |          "is_root": true,
                              |          "properties": [
                              |            {
                              |              "type": "string",
                              |              "default": "No render",
                              |              "name": "msg"
                              |            }
                              |          ],
                              |          "id": "zio.opensearch.models.NoRender"
                              |        },
                              |        {
                              |          "type": "object",
                              |          "name": "table_render_method",
                              |          "module": "opensearch",
                              |          "class_name": "zio.opensearch.models.TableRenderMethod",
                              |          "is_root": true,
                              |          "properties": [
                              |            {
                              |              "format": "list",
                              |              "items": {
                              |                "type": "object",
                              |                "name": "columns",
                              |                "module": "opensearch",
                              |                "class_name": "zio.opensearch.models.ColumnRender",
                              |                "is_root": true,
                              |                "properties": [
                              |                  {
                              |                    "type": "string",
                              |                    "name": "title"
                              |                  },
                              |                  {
                              |                    "type": "string",
                              |                    "name": "path"
                              |                  }
                              |                ],
                              |                "id": "zio.opensearch.models.ColumnRender"
                              |              },
                              |              "multiple": true,
                              |              "type": "array",
                              |              "required": false,
                              |              "name": "columns"
                              |            }
                              |          ],
                              |          "id": "zio.opensearch.models.TableRenderMethod"
                              |        },
                              |        {
                              |          "type": "object",
                              |          "name": "graph_render_method",
                              |          "module": "opensearch",
                              |          "class_name": "zio.opensearch.models.GraphRenderMethod",
                              |          "is_root": true,
                              |          "properties": [
                              |            {
                              |              "type": "integer",
                              |              "format": "int32",
                              |              "name": "span"
                              |            }
                              |          ],
                              |          "id": "zio.opensearch.models.GraphRenderMethod"
                              |        }
                              |      ],
                              |      "index": {
                              |        "analyzers": [
                              |          "keyword"
                              |        ]
                              |      },
                              |      "name": "render"
                              |    },
                              |    {
                              |      "type": "string",
                              |      "default": "default",
                              |      "index": {
                              |        "analyzers": [
                              |          "keyword"
                              |        ]
                              |      },
                              |      "name": "index"
                              |    }
                              |  ],
                              |  "key": {
                              |    "parts": [
                              |      {
                              |        "field": "name",
                              |        "postProcessing": [
                              |        ],
                              |        "format": null
                              |      }
                              |    ]
                              |  },
                              |  "columnar": {
                              |    "table": "analytics"
                              |  },
                              |  "storages": [
                              |    "opensearch"
                              |  ],
                              |  "id": "zio.opensearch.models.Analytic"
                              |}
                              |""".stripMargin).right.get

    val field = json.as[Schema]
//    println(field)
    field.isRight should be(true)
  }
}
