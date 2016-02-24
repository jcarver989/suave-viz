package com.suaveviz

case class DataSet(rows: Seq[Seq[String]], header: Option[Seq[String]]) {
  def transpose(): DataSet = {
    val nColumns = rows(0).length
    val nRows = rows.length

    val data = for (c <- 0 to nColumns-1) yield {
      for (r <- 0 to nRows-1) yield rows(r)(c)
    }

    DataSet(data, header)
  }
}

object DataSet {
  def parseFile(file: String): DataSet = {
    val lines = scala.io.Source.fromFile(file).getLines.toVector
    parse(lines)
  }

  def parse(lines: Seq[String], hasHeader: Boolean = true): DataSet = {
    val rows = lines.map { _.split("\t"): Seq[String] }
    if (hasHeader) DataSet(rows.tail, Some(rows.head)) else DataSet(rows, None)
  }
}