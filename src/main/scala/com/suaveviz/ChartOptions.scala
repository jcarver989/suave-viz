package com.suaveviz

case class ChartOptions(
  chartType: String,
  dots: Boolean,
  bins: Int,
  dotSize: Int,
  smooth: Boolean,
  ticks: Int,
  domain: Option[(Double, Double)],
  inputFile: Option[String])

object ChartOptions {
  val validOptions = Vector(
    "chart",
    "ticks",
    "dots",
    "dotSize",
    "smooth",
    "domain",
    "bins")

  def parse(args: Array[String]): ChartOptions = {
    val options = parse(args.toList)
    fromMap(options)
  }

  def fromMap(options: Map[String, String]): ChartOptions = {
    val domain = options.get("domain").map {
      _.split(",") match {
        case Array(x1, x2) => (x1.toDouble, x2.toDouble)
        case _ => sys.error("--domain must be specified like: start,end, ex --domain -3.3,3.3")
      }
    }

    ChartOptions(
      chartType = options.getOrElse("chart", "line"),
      bins = options.get("bins").map { _.toInt }.getOrElse(10),
      dots = options.get("dots").map { _.toBoolean }.getOrElse(false),
      dotSize = options.get("dotSize").map { _.toInt }.getOrElse(4),
      smooth = options.get("smooth").map { _.toBoolean }.getOrElse(false),
      ticks = options.get("ticks").map { _.toInt }.getOrElse(10),
      domain = domain,
      inputFile = options.get("input")
    )
  }

  private def parse(args: List[String], map: Map[String, String] = Map.empty): Map[String, String] = {
    args match {
      // N-args
      case opt1 :: opt2 :: tail if validOption(opt1) && validOption(opt2) => parse(opt2 +: tail, map + (opt1.drop(2) -> "true"))
      case inputFile :: opt :: tail if !validOption(inputFile) && validOption(opt) => parse(opt +: tail, map + ("input" -> inputFile))
      case opt :: value :: tail if validOption(opt) => parse(tail, map + (opt.drop(2) -> value))

      // 1 arg
      case opt :: Nil if validOption(opt) => map + (opt.drop(2) -> "true")
      case inputFile :: Nil => map + ("input" -> inputFile)

      // no args
      case Nil => map
      case _ => sys.error("Invalid option")
    }
  }

  private def validOption(name: String): Boolean = {
    name.startsWith("--") && validOptions.exists { o => o == name.drop(2) }
  }
}