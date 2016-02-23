package com.suaveviz

case class DataSet(labels: StringColumn, values: Seq[DoublesColumn])

object DataSet {
  def parseFile(file: String): DataSet = {
    val lines = scala.io.Source.fromFile(file).getLines.toVector
    parse(lines)
  }

  def parse(lines: Seq[String], hasHeader: Boolean = true): DataSet = {
    val rows = lines.map { _.split("\t") }
    
    val header = rows.head
    val data = rows.tail

    if (header.size == 1) {
      val name = header(0)
      val values = data.foldLeft(DoublesColumn(name)) { case (column, Array(value)) => column :+ value }
      DataSet(StringColumn(name), Seq(values))
    } else {
      var labels = StringColumn(header.head)
      val columns = header.tail.map { name => DoublesColumn(name) }

      for {
        row <- rows.tail
        (value, columnIndex) <- row.zipWithIndex
      } {
        if (columnIndex == 0) {
          labels = labels :+ value
        } else {
          columns(columnIndex - 1) = (columns(columnIndex - 1) :+ value)
        }
      }

      DataSet(labels, columns)
    }
  }
}