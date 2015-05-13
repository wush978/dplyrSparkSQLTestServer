import java.sql.{SQLException, DriverManager}

import scala.annotation.tailrec
import scala.concurrent._
import ExecutionContext.Implicits.global
import org.apache.spark.deploy.SparkSubmit
import sys.process._

/**
 * Created by wush on 2015/5/12.
 */
object Main {

  val driverName = "org.apache.hive.jdbc.HiveDriver";

  Class.forName(driverName)

  @tailrec
  def checkConnection() : Unit = {
    try {
      println("Test Connection...")
      val con = DriverManager.getConnection("jdbc:hive2://localhost:10000/default", "hive", "")
    }
    catch {
      case e : SQLException => {
        Thread.sleep(5000)
        checkConnection()
      }
    }
  }

  def main(args : Array[String]) : Unit = {
    if (args.length < 1) throw new IllegalArgumentException("Usage: sbt \"run <source path>\"")
    val package_path = args.head
    val default_args : Array[String] = """
                                         |--class org.apache.spark.sql.hive.thriftserver.HiveThriftServer2
                                         |spark-internal
                                       """.stripMargin.replace('\n', ' ').split(" ").filter(_.size > 0)
    future {
      SparkSubmit.main(default_args ++ args)
    }
    checkConnection()
    f"R CMD build $package_path".!
    f"R CMD check --as-cran $package_path*.tar.gz"
    System.exit(0)
  }
}
