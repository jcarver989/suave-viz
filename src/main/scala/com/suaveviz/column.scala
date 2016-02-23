package com.suaveviz

trait Column[T] {
  def :+(datum: String): Column[T]
  def values: Seq[T]
}

case class StringColumn(name: String, values: Seq[String] = Vector.empty) extends Column[String] {
  override def :+(datum: String): StringColumn = StringColumn(name, values :+ datum)
}

case class DoublesColumn(name: String, values: Seq[Double] = Vector.empty) extends Column[Double] {
  override def :+(datum: String): DoublesColumn = DoublesColumn(name, values :+ datum.toDouble)
  def domain(): (Double, Double) = (values.min, values.max)
}