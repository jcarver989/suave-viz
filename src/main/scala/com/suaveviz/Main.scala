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

  val (options, chartOptions) = Options.parse(args)

  val lines = options.inputFile match {
    case Some(f) => io.Source.fromFile(f).getLines.toVector
    case None => (for (ln <- io.Source.stdin.getLines) yield ln).toVector
  }

  val data = DataSet.parse(lines, options.header, options.delimiter)
 
  val chart = chartOptions match {
    case o: BarOptions => Charts.bar(data, o)
    case o: HistogramOptions => Charts.histogram(data, o)
    case o: LineAndScatterOptions => Charts.line(data, o)
  }

  val chartCode = Template.render(chart, options.isLocal)
  saveAndOpen(chartCode)
}