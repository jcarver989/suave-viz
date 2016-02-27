package com.suaveviz

object Charts {
  private def legend(data: DataSet): String = {
    val names = data.header.map { _.drop(1).map { "'" + _ + "'" } }.getOrElse(Seq())

    s"""
     var div = document.getElementById("chart")
     var legend = document.createElement("div")

     var labels = [${names.mkString(",")}]
     for (var i = 0; i < labels.length; i++) {
        var label = document.createElement("span")
        label.classList.add("series-" + i)
        label.innerText = labels[i]
        legend.appendChild(label)
     }      

     legend.classList.add("legend")
     div.appendChild(legend)
    """
  }

  def histogram(data: DataSet, options: HistogramOptions): String = {
    val values = data.transpose.rows(0)
    val domain = options.domain.getOrElse((values.min, values.max + 1))

    s"""
    var values = [${values.mkString(",")}]
    var chart = new Suave.Histogram("#chart", { domain: [${domain._1}, ${domain._2}], bins: ${options.bins} })
    chart.draw({
      values: values
    })
    """
  }

  def bar(data: DataSet, options: BarOptions): String = {
    // assume 1st row is now the labels
    val labels = data.rows.map { r => "'" + r(0) + "'" }.mkString(",")
    val bars = data.rows.map { barGroup => s"[${barGroup.drop(1).mkString(",")}]" }

    s"""
    ${legend(data)}

    var chart = new Suave.BarChart("#chart", {
      layout: "${options.layout}"
    })

    chart.draw({ 
      labels: [${labels}],
      bars: [
        ${bars.mkString(",\n")}
      ]
    })
    """
  }

  def line(data: DataSet, options: LineAndScatterOptions): String = {
    val columns = data.transpose
    val xs = columns.rows(0)

    val (xScale, xLabels) = options.xScale match {
      case "auto" | "time" =>
        Dates.attemptToParse(xs) match {
          case dates if !dates.isEmpty => ("time", dates.map { _.getTime })
          case _ => ("ordinal", columns.rows(0).map { "'" + _ + "'" })
        }

      case _ => (options.xScale, xs.map { "'" + _ + "'" })
    }

    val yScale = if (options.yScale == "auto") "linear" else options.yScale

    val seriesLabels = data.header.getOrElse(Seq())

    // assume 1st row contains the x-labels, so drop it
    val lines = columns.rows.zipWithIndex.drop(1).map {
      case (row, i) =>
        val label = if (seriesLabels.isEmpty) s"line-${i - 1}" else seriesLabels(i)
        s"""{
        label: '${label}', // name of this column (header row)
        values: [${row.mkString(",")}],
        dots: ${if (options.isScatter) true else options.dots},
        smooth: ${options.smooth},
        line: ${!options.isScatter}
      }
      """
    }.mkString(",\n")

    s"""
    ${legend(data)}
    
    var chart = new Suave.LineChart(
    "#chart", 
    { 
      ticks: ${options.ticks},
      dotSize: ${options.dotSize},
      xScale: "${xScale}",
      yScale: "${yScale}"
    })

    chart.draw({ 
      labels: [${xLabels.mkString(",")}],
      lines: [${lines}]
    })
    """
  }
}