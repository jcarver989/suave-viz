package com.suaveviz

import org.scalatest._
class OptionsTest extends FlatSpec with Matchers {

  val defaultGeneral = GeneralOptions(
    inputFile = None,
    header = true,
    delimiter = "\t",
    isLocal = false)

  val defaultLine = LineAndScatterOptions(
    dots = false,
    dotSize = 4,
    smooth = false,
    ticks = 10,
    xScale = "auto",
    yScale = "auto",
    isScatter = false)

  val defaultScatter = defaultLine.copy(isScatter = true)
  val defaultBar = BarOptions(layout = "vertical", ticks = 10)
  val defaultHistogram = HistogramOptions(bins = 10, ticks = 10, domain = None)

  val defaults = Map(
    "bar" -> defaultBar,
    "histogram" -> defaultHistogram,
    "line" -> defaultLine,
    "scatter" -> defaultScatter)

  "Options" should "return defaults if no args" in {
    val (generalOptions, chartOptions) = Options.parse(Array())
    generalOptions shouldBe defaultGeneral
    chartOptions shouldBe defaultLine
  }

  "Options" should "set input file" in {
    val (generalOptions, chartOptions) = Options.parse(Array("foo.txt"))
    generalOptions shouldBe defaultGeneral.copy(inputFile = Some("foo.txt"))
  }

  "Options" should "set --local" in {
    val (generalOptions, chartOptions) = Options.parse(Array("--local"))
    generalOptions shouldBe defaultGeneral.copy(isLocal = true)
  }

  "Options" should "set --no-header" in {
    val (generalOptions, chartOptions) = Options.parse(Array("--no-header"))
    generalOptions shouldBe defaultGeneral.copy(header = false)
  }

  "Options" should "set --chart" in {
    defaults.foreach {
      case (chart, options) =>
        val (generalOptions, chartOptions) = Options.parse(Array("--chart", chart))
        generalOptions shouldBe defaultGeneral
        chartOptions shouldBe options
    }
  }

  "Options" should "set --x on line chart" in {
    Seq("linear", "log", "time", "ordinal").foreach { scale =>
      val (generalOptions, chartOptions) = Options.parse(Array("--x", scale))
      generalOptions shouldBe defaultGeneral
      chartOptions shouldBe defaultLine.copy(xScale = scale)
    }
  }

  "Options" should "set --y on line chart" in {
    Seq("linear", "log").foreach { scale =>
      val (generalOptions, chartOptions) = Options.parse(Array("--y", scale))
      generalOptions shouldBe defaultGeneral
      chartOptions shouldBe defaultLine.copy(yScale = scale)
    }
  }

  "Options" should "set --dots on line chart" in {
    val (generalOptions, chartOptions) = Options.parse(Array("--dots"))
    generalOptions shouldBe defaultGeneral
    chartOptions shouldBe defaultLine.copy(dots = true)
  }

  "Options" should "set --dotSize on line chart" in {
    val (generalOptions, chartOptions) = Options.parse(Array("--dotSize", "80"))
    generalOptions shouldBe defaultGeneral
    chartOptions shouldBe defaultLine.copy(dotSize = 80)
  }

  "Options" should "set --smooth on line chart" in {
    val (generalOptions, chartOptions) = Options.parse(Array("--smooth"))
    generalOptions shouldBe defaultGeneral
    chartOptions shouldBe defaultLine.copy(smooth = true)
  }

  "Options" should "set --bins on histogram" in {
    val (generalOptions, chartOptions) = Options.parse(Array("--chart", "histogram", "--bins", "1"))
    generalOptions shouldBe defaultGeneral
    chartOptions shouldBe defaultHistogram.copy(bins = 1)
  }

  "Options" should "set --domain on histogram" in {
    val (generalOptions, chartOptions) = Options.parse(Array("--chart", "histogram", "--domain", "1,100"))
    generalOptions shouldBe defaultGeneral
    chartOptions shouldBe defaultHistogram.copy(domain = Some((1, 100)))
  }
  
  "Options" should "set --horizontal on bar" in {
    val (generalOptions, chartOptions) = Options.parse(Array("--chart", "bar", "--horizontal"))
    generalOptions shouldBe defaultGeneral
    chartOptions shouldBe defaultBar.copy(layout = "horizontal")
  }

  "Options" should "set --ticks on everything" in {
    defaults.foreach {
      case (chart, options) =>
        val (generalOptions, chartOptions) = Options.parse(Array("--ticks", "20"))
        generalOptions shouldBe defaultGeneral
        chartOptions.ticks shouldBe 20
    }
  }

}