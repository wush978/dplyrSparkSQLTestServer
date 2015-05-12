import org.apache.spark.deploy.SparkSubmit

/**
 * Created by wush on 2015/5/12.
 */
object Main {

  def main(args : Array[String]) : Unit = {
    val default_args : Array[String] = """
                                         |--class org.apache.spark.sql.hive.thriftserver.HiveThriftServer2
                                         |spark-internal
                                       """.stripMargin.replace('\n', ' ').split(" ").filter(_.size > 0)

    SparkSubmit.main(default_args ++ args)

  }

}
