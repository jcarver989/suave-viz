package com.suaveviz

case class ChartOptions(
  chartType: ChartType,
  dots: Boolean,
  dotSize: Int,
  smooth: Boolean,
  ticks: Int,
  domain: Option[(Double, Double)])

object ChartOptions {
  val validOptions = Vector(
    "chart",
    "ticks",
    "dots",
    "smooth",
    "domain")

  def parse(args: Array[String]): ChartOptions = {
    val options = parse(args.toList)
    fromMap(options)
  }

  def fromMap(options: Map[String, String]): ChartOptions = {
    val chartType = options.get("chart") match {
      case Some("line") => Line
      case Some("bar") => Bar
      case Some("histogram") => Histogram
      case None => Line
      case _ => sys.error("Unsupported chart type")
    }

    val domain = options.get("domain").map {
      _.split(",") match {
        case Array(x1, x2) => (x1.toDouble, x2.toDouble)
        case _ => sys.error("--domain must be specified like: start,end, ex --domain -3.3,3.3")
      }
    }

    ChartOptions(
      chartType = chartType,
      dots = options.get("dots").map { _.toBoolean }.getOrElse(false),
      dotSize = options.get("dotSize").map { _.toInt }.getOrElse(4),
      smooth = options.get("smooth").map { _.toBoolean }.getOrElse(false),
      ticks = options.get("ticks").map { _.toInt }.getOrElse(10),
      domain = domain
    )
  }

  private def parse(args: List[String], map: Map[String, String] = Map.empty): Map[String, String] = {
    args match {
      case Nil => map
      case key :: value :: tail if key.startsWith("--") && validOptions.exists { key.endsWith } => parse(tail, map + (key.drop(2) -> value))
      case _ => sys.error("Invalid option")
    }
  }
}