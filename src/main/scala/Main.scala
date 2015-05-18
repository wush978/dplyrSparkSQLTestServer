import java.sql.{SQLException, DriverManager}

import scala.annotation.tailrec
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global
import org.apache.spark.deploy.SparkSubmit
import java.io.File
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

  def checkMem() {
    val mb = 1024 * 1024
    val runtime = Runtime.getRuntime()
    println("Heap utilization statistics [MB]")
    println("Used memory: " + (runtime.totalMemory - runtime.freeMemory) / mb)
    println("Free memory: " + runtime.freeMemory / mb)
    println("Total memory: " + runtime.totalMemory / mb)
    println("Max memory: " + runtime.maxMemory / mb)
  }

  def main(args : Array[String]) : Unit = {
    if (args.length < 1) throw new IllegalArgumentException("Usage: sbt \"run <source path>\"")
    val packagePath = args.head
    val defaultArgs : Array[String] = """
                                         |--class org.apache.spark.sql.hive.thriftserver.HiveThriftServer2
                                         |spark-internal
                                       """.stripMargin.replace('\n', ' ').split(" ").filter(_.size > 0)
    val sparkDaemon = future {
      try {
        SparkSubmit.main(defaultArgs ++ args)
      } catch {
        case e : java.lang.OutOfMemoryError => {
	  checkMem()
	}
      }
    }
    checkConnection()
    checkMem()
    val cmd = f"cd $packagePath && ./travis-tool.sh run_tests"
    val code = Process("./travis-tool.sh run_tests", new File(packagePath)).!
    if (code ==0) {
      Process("touch .success", new File(packagePath)).!
    }
    System.exit(0)
  }
}
