name := "dplyrSparkSQLTestServer"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.3.1"

libraryDependencies += "org.apache.spark" %% "spark-hive-thriftserver" % "1.3.1"

fork := true

javaOptions in run += "-Xmx1g -Xss1m -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=256m"
