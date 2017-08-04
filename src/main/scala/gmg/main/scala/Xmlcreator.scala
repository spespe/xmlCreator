package gmg.main.scala

import java.text.SimpleDateFormat
import java.util.Calendar
import scala.io.Source

/**
  * Created by Pietro.Speri on 28/11/2016.
  */

object Xmlcreator extends App {
  // Partial function approach to be continued
  // val test= new PartialFunction[Int,Int]{
  //   def apply(x:Int)
  //   def isDefinedAt(x:Int)
  // }
  val now = Calendar.getInstance.getTime
  val dateformat = new SimpleDateFormat("yyyyMMdd_HH:mm:ss")
  val date = dateformat.format(now)
  val name_test=args(2).toString

  val usage = "\nWrong number of parameters passed.\n" +
    "Correct usage: java -jar xmlcreator.jar [ input file ] \n" +
    "Example: java -jar xmlcreator.jar /home/hdp60-ss-pdl-uk/pietro/gmg_tests/tests/tests_result.log"

  if (args.length != 3) {
    println(usage)
    System.exit(1)
  }

  val file = Source.fromFile(args(0)).getLines.drop(1).toList.filter(x => x.contains("|")).map { x =>
    val el = x.split("\\|").map(_.trim)
    (el(0) ,el(1), el(2), el(3), el(6))
  }
  val data = file.map { y =>
    Test(y._1.mkString,y._2.mkString, y._4.mkString, y._5.mkString)
  }

  def resultToXml(tests: List[Test]): xml.Elem = {
    val size = tests.length.toString
    var err, skip, fail, tot = 0

    for (i <- 0 to tests.length - 1) {
      tests(i).result match {
        case "FAILED" => err += 1
        case "IGNORED" => skip += 1
        case "" => fail += 1
        case _ =>
      }
      val datetime=tests(i).time
      if (!tests(i).time.isEmpty) {
        val hours = tests(i).time.trim.split(" ")(0).toInt * 3600
        val minutes = tests(i).time.trim.split(" ")(2).toInt * 60
        val seconds = tests(i).time.trim.split(" ")(4).toInt
        val time = hours + minutes + seconds
        tot += time
      } else {
        tot += 0
      }
    }

    val h = tot / 3600
    val m = tot / 60 % 60
    val s = tot % 60
    val timing = h + " hours " + m + " minutes " + s + " seconds "

    val x = <testsuite name="" time={timing} tests={size} errors={err.toString} skipped={skip.toString} failures={fail.toString}>
      {for (test <- tests)
        yield test.toXml}
    </testsuite>
    x
  }

  case class Test(val datetime: String, val testname: String, val time: String, val result: String) {
    val classname = name_test

    def toXml: xml.Elem =
      <testcase date={datetime} name={testname} time={time} classname={classname}></testcase>
  }

  //Saving on an xml file
  scala.xml.XML.save(args(1), resultToXml(data), "UTF-8", true, null)
}

