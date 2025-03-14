#!/usr/bin/env python3
import os
from glob import glob
import re

REGEX_CHANGES = [
    # (
    #     r"""c.downField\("(.*)"\).focus.flatMap\(_.asString\)""",
    #     """jObj.getOption[String]("$1")"""
    # ),

    (r'\bSingleKeyDictionary\b', 'Map'),
    (r'\bulong\b', 'Long'),
    (r'\buint\b', 'Int'),
    (r'\bQueryContainer\b', 'Query'),
    (r'\bAggregationContainer\b', 'Aggregation'),
    (r'\bTaskInfos\b', 'Chunk[zio.openasearch.tasks.TaskInfo]'),
    (r'\bpretty: Boolean\s+\)', 'pretty: Boolean=false)'),
    (r'class (.*)Manager\(httpService: ElasticSearchHttpService\) {', '''object $1Manager {
  lazy val live: ZLayer[ElasticSearchHttpService, Nothing, $1Manager] =
    ZLayer {
      for {
        httpServiceBase <- ZIO.service[ElasticSearchHttpService]
      } yield new $1Manager {
        override def httpService: ElasticSearchHttpService = httpServiceBase
      }
    }

}

trait $1Manager {
  def httpService: ElasticSearchHttpService
'''),


]

TEXT_CHANGES = [
    ('""1m""', '"1m"' ),
    ('""30s""', '"30s"' ),
    ('""20s""', '"20s"' ),
    ('""25s""', '"25s"' ),
    ('""10s""', '"10s"' ),
    ('""0""', '"0"' ),
    ('""1s""', '"1s"' ),
    ('""5d""', '"5d"' ),
    ('""5m""', '"5m"' ),
    ('"",""', '","' ),
    ('""started""', '"started"' ),
    # moving to chunk
    ('Seq[String] = Nil', 'Chunk[String] = Chunk.empty' ),
    ('Seq[String] = Seq.empty', 'Chunk[String] = Chunk.empty' ),
    # ('Seq[String]', 'Chunk[String]' ),
    ('List[String] = Nil', 'Chunk[String] = Chunk.empty' ),
    ('List[String] = List.empty', 'Chunk[String] = Chunk.empty' ),
    # ('List[String]', 'Chunk[String]' ),
    ('List[Query] = Nil', 'Chunk[Query] = Chunk.empty' ),
    ('List[Query] = List.empty', 'Chunk[Query] = Chunk.empty' ),
    # ('List[Query]', 'Chunk[Query]' ),

('implicit val jsonCodec', 'implicit lazy val jsonCodec'),
    ('ZioResponse[', 'ZIO[Any, FrameworkException, '),
    ('(client: ElasticSearch)', '(client: ElasticSearchClient)' ),
    ('Option[String] | Option[Int]', 'Option[Json]' ),
    ('Option[Boolean] | Option[String]', 'Option[Json]' ),
    ('Option[Int] | Option[String]', 'Option[Json]' ),
    ('Option[String] | Option[Long]', 'Option[Json]' ),
    ('errorTrace: Boolean,', 'errorTrace: Boolean=false,'),
    ('filterPath: Chunk[String],', 'filterPath: Chunk[String]=Chunk.empty[String],'),
    ('human: Boolean,', 'human: Boolean=false,'),
    ('pretty: Boolean,', 'pretty: Boolean=false,'),
    ('masterTimeout: String,', 'masterTimeout: Option[String]=None,'),
    ('timeout: String,', 'timeout: Option[String]=None,'),
    ('HitsMetadata[TDocument]', 'HitResults'),
    ('ResponseItem[TDocument]', 'ResultDocument'),
    (' defaultOperator.OR', ' DefaultOperator.OR'),
    ('InlineGet[TDocument]', 'TDocument'),
    ('DurationValue[UnitNanos]', 'Long'),
    ('ccsMinimizeRoundtrips != "true"', 'ccsMinimizeRoundtrips != true'),
    ('ccsMinimizeRoundtrips != "true"', 'ccsMinimizeRoundtrips != true'),
    ('DurationValue[UnitSeconds]', 'Long'),
    ('DurationValue[UnitFloatMillis]', 'Double'),
    ('from: Int = "0",', 'from: Int = 0,'),
    ('size: Int = "100",', 'size: Int = 100,'),
    ('Stringified[EpochTime[UnitSeconds]]', 'String'),
    ('client: ElasticSearchClient', 'client: ElasticSearchHttpService'),
    ('client: ElasticSearchHttpService', 'httpService: ElasticSearchHttpService'),
    ('client.execute', 'httpService.execute'),
    ('version: Option[Double] = None', 'version: Option[Long] = None'),
    ('def method: String = "PUT"', 'def method: Method = Method.PUT'),
    ('def method: String = "POST"', 'def method: Method = Method.POST'),
    ('def method: String = "GET"', 'def method: Method = Method.GET'),
    ('def method: String = "HEAD"', 'def method: Method = Method.HEAD'),
    ('def method: String = "DELETE"', 'def method: Method = Method.DELETE'),

    (
        "Double | null | None",
        "Option[Double] = None"
    ),
    (
        "match: Long,",
        "`match`: Long,"
    ),
    (
        "managed: Bool() = None",
        "managed: Option[Boolean] = None"
    )

]

PACKAGES = [
"openasearch-admin",
"openasearch-async-search",
"openasearch-autoscaling" ,
"openasearch-cat" ,
"openasearch-ccr" ,
"openasearch-client-http4s",
"openasearch-client-sttp" ,
"openasearch-cluster" ,
"openasearch-core" ,
"openasearch-dangling-indices",
"openasearch-enrich" ,
"openasearch-eql" ,
"openasearch-features",
"openasearch-fleet" ,
"openasearch-graph" ,
"openasearch-ilm" ,
"openasearch-indices",
"openasearch-ingest" ,
"openasearch-license" ,
"openasearch-logstash" ,
"openasearch-migration" ,
"openasearch-ml" ,
"openasearch-monitoring",
"openasearch-nodes" ,
"openasearch-orm" ,
"openasearch-rollup",
"openasearch-script" ,
"openasearch-searchable-snapshots",
"openasearch-security" ,
"openasearch-shutdown" ,
"openasearch-slm" ,
"openasearch-snapshot",
"openasearch-sql" ,
"openasearch-ssl" ,
"openasearch-tasks",
"openasearch-text-structure",
"openasearch-transform" ,
"openasearch-watcher" ,
"openasearch-xpack",

]

files = sorted(
    list([
        y
        for pkg in PACKAGES
        for path in [os.path.join(pkg, "src", "main", "scala"),
                     os.path.join(pkg, "js", "src", "main", "scala"),
                     os.path.join(pkg, "jvm", "src", "main", "scala"),
                     os.path.join(pkg, "shared", "src", "main", "scala")]
        for x in os.walk(path)
        for y in glob(os.path.join(x[0], "*.scala"))
    ]))
for file in files:
    # print(file)
    original = open(file, "r").read()
    content = original

    for rx, new_value in REGEX_CHANGES:
        matches = re.finditer(rx, content, re.MULTILINE)
        for match in matches:
            item = match.group()
            groups = match.groups()
            rep = new_value
            if len(groups) > 0:
                for pos, g in enumerate(groups):
                    rep = rep.replace("$"+str(pos+1), g)
            content = content.replace(item, rep)

    for old_value, new_value in TEXT_CHANGES:
        content = content.replace(old_value, new_value)

    if original != content:
        print("Updating " + file)
        # print(content)
        with open(file, "w") as fo:
            fo.write(content)
