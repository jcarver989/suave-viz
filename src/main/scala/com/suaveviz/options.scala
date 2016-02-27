package com.suaveviz

trait ChartOptions {
  val ticks: Int
}

case class LineAndScatterOptions(
  dots: Boolean,
  dotSize: Int,
  smooth: Boolean,
  xScale: String,
  yScale: String,
  ticks: Int,
  isScatter: Boolean) extends ChartOptions

object LineAndScatterOptions {
  def apply(options: Map[String, String]): LineAndScatterOptions = {
    LineAndScatterOptions(
      dots = options("dots").toBoolean,
      dotSize = options("dotSize").toInt,
      smooth = options("smooth").toBoolean,
      ticks = options("ticks").toInt,
      xScale = options("x"),
      yScale = options("y"),
      isScatter = options("chart") == "scatter"
    )
  }
}

case class HistogramOptions(bins: Int, domain: Option[(Double, Double)], ticks: Int) extends ChartOptions

object HistogramOptions {
  def apply(options: Map[String, String]): HistogramOptions = {
    val domain = options("domain").split(",") match {
      case Array("") => None
      case Array(x1, x2) => Some((x1.toDouble, x2.toDouble))
      case _ => sys.error("--domain must be specified like: start,end, ex --domain -3.3,3.3")
    }

    HistogramOptions(
      ticks = options("ticks").toInt,
      bins = options("bins").toInt,
      domain = domain
    )
  }
}

case class BarOptions(layout: String, ticks: Int) extends ChartOptions

object BarOptions {
  def apply(options: Map[String, String]): BarOptions = {
    BarOptions(
      layout = if (options("horizontal").toBoolean) "horizontal" else "vertical",
      ticks = options("ticks").toInt
    )
  }
}

case class GeneralOptions(
  inputFile: Option[String],
  header: Boolean,
  delimiter: String,
  isLocal: Boolean)

object Options {
  val defaults = Map(
    "chart" -> "line",
    "bins" -> "10",
    "delimiter" -> "\t",
    "domain" -> "",
    "dots" -> "false",
    "dotSize" -> "4",
    "smooth" -> "false",
    "no-header" -> "false",
    "horizontal" -> "false",
    "x" -> "auto",
    "y" -> "auto",
    "ticks" -> "10",
    "local" -> "false")

  def parse(args: Array[String]): (GeneralOptions, ChartOptions) = {
    val options = defaults ++ new OptionParser(defaults.keySet).parse(args)

    val generalOptions = GeneralOptions(
      inputFile = options.get("input"),
      header = !options("no-header").toBoolean,
      delimiter = options("delimiter"),
      isLocal = options("local").toBoolean
    )

    val chartOptions: ChartOptions = options.getOrElse("chart", "line") match {
      case "bar" => BarOptions(options)
      case "histogram" => HistogramOptions(options)
      case "line" => LineAndScatterOptions(options)
      case "scatter" => LineAndScatterOptions(options)
    }

    (generalOptions, chartOptions)
  }
}