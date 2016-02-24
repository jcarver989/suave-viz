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

  def histogram(data: DataSet, options: ChartOptions): String = {
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

  def bar(data: DataSet, options: ChartOptions): String = {
    // assume 1st row is now the labels
    val labels = data.rows.map { r => "'" + r(0) + "'" }.mkString(",")
    val bars = data.rows.map { barGroup => s"[${barGroup.drop(1).mkString(",")}]" }

    s"""
    ${legend(data)}

    var chart = new Suave.BarChart("#chart", {
     ticks: ${options.ticks}
    })

    chart.draw({ 
      labels: [${labels}],
      bars: [
        ${bars.mkString(",\n")}
      ]
    })
    """
  }

  def line(data: DataSet, options: ChartOptions): String = {
    val columns = data.transpose
    val xLabels = columns.rows(0).map { "'" + _ + "'" }.mkString(",")

    // assume 1st cell is the label, so drop it
    val seriesLabels = data.header.get
    val lines = columns.rows.zipWithIndex.drop(1).map {
      case (row, i) =>
        s"""{
        label: '${seriesLabels(i)}', // name of this column (header row)
        values: [${row.mkString(",")}],
        dots: ${options.dots},
        smooth: ${options.smooth}
      }
      """
    }.mkString(",\n")

    s"""
    ${legend(data)}
    
    var chart = new Suave.LineChart(
    "#chart", 
    { 
      ticks: ${options.ticks},
      dotSize: ${options.dotSize}
    })

    chart.draw({ 
      labels: [${xLabels}],
      lines: [${lines}]
    })
    """
  }

  def scatter(data: DataSet, options: ChartOptions): String = {
    val columns = data.transpose
    val xLabels = columns.rows(0).map { "'" + _ + "'" }.mkString(",")
    val seriesLabels = data.header.get
    val lines = columns.rows.zipWithIndex.drop(1).map {
      case (row, i) =>
        s"""{
        label: '${seriesLabels(i)}', // name of this column (header row)
        values: [${row.mkString(",")}],
        dots: true,
        line: false
      }
      """
    }.mkString(",\n")

    s"""
    ${legend(data)}

    var chart = new Suave.LineChart(
    "#chart", 
    { 
      ticks: ${options.ticks},
      dotSize: ${options.dotSize}
    })

    chart.draw({ 
      labels: [${xLabels}],
      lines: [${lines}]
    })
    """
  }
}