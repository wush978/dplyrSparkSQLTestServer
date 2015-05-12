name := "dplyrSparkSQLTestServer"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.3.1"

libraryDependencies += "org.apache.spark" %% "spark-hive-thriftserver" % "1.3.1"

connectInput in run := true

outputStrategy in run := Some (StdoutOutput)

console := {
  (runMain in Compile).toTask(
    """
      |Main
    """.stripMargin).value
}
