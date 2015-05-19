name := "dplyrSparkSQLTestServer"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.3.1"

libraryDependencies += "org.apache.spark" %% "spark-hive-thriftserver" % "1.3.1"

fork := true

javaOptions in run += "-Xms 1024M -Xmx3072M"
