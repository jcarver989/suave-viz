package com.suaveviz

case class DataSet(rows: Seq[Seq[String]], header: Option[Seq[String]]) {
  def transpose(): DataSet = {
    val data = scala.collection.mutable.Buffer[Seq[String]]()
    val nColumns = rows(0).length
    val nRows = rows.length
    var c = 0
    while (c < nColumns) {
      val newRow = scala.collection.mutable.Buffer[String]()
      var r = 0
      while (r < nRows) {
        newRow += rows(r)(c)
        r += 1
      }

      data += newRow
      c += 1
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