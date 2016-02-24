package com.suaveviz

case class DataSet(rows: Seq[Seq[String]], header: Option[Seq[String]]) {
  def transpose(): DataSet = {
    val nColumns = rows(0).length
    val nRows = rows.length

    val data = for (c <- 0 until nColumns) yield {
      for (r <- 0 until nRows) yield rows(r)(c)
    }

    DataSet(data, header)
  }
}

object DataSet {
  def parse(lines: Seq[String], hasHeader: Boolean = true): DataSet = {
    val rows = lines.map { _.split("\t"): Seq[String] }
    if (hasHeader) DataSet(rows.tail, Some(rows.head)) else DataSet(rows, None)
  }
}