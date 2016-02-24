package com.suaveviz

import org.scalatest._
class DataSetTest extends FlatSpec with Matchers {

  "A data set" should "transpose twice to the identity" in {
    val vector = Vector(
      Vector("1", "2", "3"),
      Vector("4", "5", "6"),
      Vector("7", "8", "9"))

    val data = DataSet(vector, None)
    data.transpose.transpose.rows shouldBe vector
  }

  "A data set" should "transpose to a column vector" in {
    val vector = Vector(
      Vector("1", "2", "3"),
      Vector("4", "5", "6"),
      Vector("7", "8", "9"))

    val data = DataSet(vector, None)
    data.transpose.rows shouldBe Vector(
      Vector("1", "4", "7"),
      Vector("2", "5", "8"),
      Vector("3", "6", "9"))
  }

  "A data set" should "parse some TSV input data" in {
    val input = Vector("1\t2\t3", "4\t5\t6", "7\t8\t9")
    DataSet.parse(input, false).rows shouldBe Vector(
      Vector("1", "2", "3"),
      Vector("4", "5", "6"),
      Vector("7", "8", "9"))
  }

  "A data set" should "parse some TSV input data with a header line" in {
    val input = Vector("n1\tn2\tn3", "1\t2\t3", "4\t5\t6", "7\t8\t9")
    val data = DataSet.parse(input, true)
    data.header shouldBe Some(Vector("n1", "n2", "n3"))
    data.rows shouldBe Vector(
      Vector("1", "2", "3"),
      Vector("4", "5", "6"),
      Vector("7", "8", "9"))
  }
}