package com.suaveviz

case class ChartOptions(
  chartType: String,
  dots: Boolean,
  bins: Int,
  dotSize: Int,
  smooth: Boolean,
  ticks: Int,
  domain: Option[(Double, Double)],
  inputFile: Option[String],
  header: Boolean,
  delimiter: String,
  layout: String)

object ChartOptions {
  val validOptions = Vector(
    "chart",
    "ticks",
    "dots",
    "dotSize",
    "smooth",
    "domain",
    "bins",
    "no-header",
    "delimiter",
    "horizontal")

  def parse(args: Array[String]): ChartOptions = {
    val options = new OptionParser(validOptions).parse(args)

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
      inputFile = options.get("input"),
      header = !(options.get("no-header").map { _.toBoolean }.getOrElse(false)),
      delimiter = options.get("delimiter").getOrElse("\t"),
      layout = if (options.get("horizontal").map {_.toBoolean }.getOrElse(false)) "horizontal" else "vertical"
    )
  }
}