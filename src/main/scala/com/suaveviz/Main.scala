package com.suaveviz

import java.awt.Desktop
import java.net.URI
import java.io.File
import java.io.PrintWriter

object Main extends App {
  def saveAndOpen(chartCode: String): Unit = {
    val tempFile = File.createTempFile("chart", ".tmp.html")
    val writer = new PrintWriter(tempFile)
    writer.println(chartCode)
    writer.close()
    val url = tempFile.getCanonicalFile().getAbsolutePath()
    Desktop.getDesktop().browse(new URI(s"file://${url}"))
  }

  val options = ChartOptions.parse(args)
  val lines = (for (ln <- io.Source.stdin.getLines) yield ln).toVector
  val data = DataSet.parse(lines)

  val chart = options.chartType match {
    case Line => Charts.line(data, options)
    case Bar => Charts.bar(data, options)
    case Histogram => Charts.histogram(data, options)
    case _ => sys.error("unsupported chart type")
  }

  val chartCode = Template.render(chart)
  saveAndOpen(chartCode)
}