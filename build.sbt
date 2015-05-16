name := "dplyrSparkSQLTestServer"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.3.1"

libraryDependencies += "org.apache.spark" %% "spark-hive-thriftserver" % "1.3.1"

lazy val myRun = taskKey[Unit]("A custom run task.")

myRun := {
  val r   = (runner in Compile).value
  val cp  = (fullClasspath in Compile).value
  val res = r.run("Main", cp.map(_.data), Nil, streams.value.log)
  val ExitCode = "Nonzero exit code: (-?\\d+)".r
  val code = res match {
    case Some(ExitCode(c)) => c.toInt
    case _ => 0
  }
  if (code != 0) sys.exit(code)
}
