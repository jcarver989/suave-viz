package com.suaveviz

import java.text.SimpleDateFormat
import scala.util.Try
import java.util.Date

object Dates {
  private val formats = Vector(
    new SimpleDateFormat("dd-MMM-yy"),

    new SimpleDateFormat("yyyy-MM-dd"),
    new SimpleDateFormat("yyyy/MM/dd"),

    new SimpleDateFormat("MM-dd-yyyy"),
    new SimpleDateFormat("MM/dd/yyyy")
  )

  def attemptToParse(strings: Seq[String]): Seq[Date] = {
    val format = strings.headOption match {
      case Some(s) => formats.find { f => Try(f.parse(s)).isSuccess }
      case None => None
    }
    
    for {
      f <- format.toVector
      s <- strings
    } yield f.parse(s)
  }

}