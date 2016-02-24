package com.suaveviz

object Charts {

  private def legend(data: DataSet): String = {
    val names = data.values.zipWithIndex.map { case (DoublesColumn(name, _), i) => "'" + name + "'" }
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
    val values = data.values(0).values
    val domain = options.domain.getOrElse((values.min, values.max + 1))

    s"""
    var values = [${values.mkString(",")}]
    var chart = new Suave.Histogram("#chart", { domain: [${domain._1}, ${domain._2}] })
    chart.draw({
      values: values
    })
    """
  }

  def bar(data: DataSet, options: ChartOptions): String = {
    val barGroups = for {
      (_, i) <- data.labels.values.zipWithIndex
    } yield data.values.map { column => column.values(i) }

    val bars = barGroups.map { barGroup => s"[${barGroup.mkString(",")}]" }

    s"""
    ${legend(data)}

    var chart = new Suave.BarChart("#chart", {
     ticks: ${options.ticks}
    })

    chart.draw({ 
      labels: [${data.labels.values.map(v => s"'${v}'").mkString(",")}],
      bars: [
        ${bars.mkString(",\n")}
      ]
    })
    """
  }

  def line(data: DataSet, options: ChartOptions): String = {
    val series = data.values.map {
      case DoublesColumn(label, values) =>
        s"""{ 
              label: '${label}', 
              values: [${values.mkString(",")}], 
              dots: ${options.dots},
              smooth: ${options.smooth}
            }
         """
    }

    s"""
    ${legend(data)}
    
    var chart = new Suave.LineChart(
    "#chart", 
    { 
      ticks: ${options.ticks},
      dotSize: ${options.dotSize}
    })

    chart.draw({ 
      labels: [${data.labels.values.map(v => s"'${v}'").mkString(",")}],
      lines: [${series.mkString(",\n")}]
    })
    """
  }

  def scatter(data: DataSet, options: ChartOptions): String = {
    val series = data.values.map {
      case DoublesColumn(label, values) =>
        s"""{ 
              label: '${label}', 
              values: [${values.mkString(",")}], 
              dots: true,
              line: false
            }
         """
    }

    s"""
    ${legend(data)}

    var chart = new Suave.LineChart(
    "#chart", 
    { 
      ticks: ${options.ticks},
      dotSize: ${options.dotSize}
    })

    chart.draw({ 
      labels: [${data.labels.values.map(v => s"'${v}'").mkString(",")}],
      lines: [${series.mkString(",\n")}]
    })
    """
  }
}