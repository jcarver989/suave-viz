

object Foo extends App {
  val x = Vector(
    Vector("Date", "Price"),
    Vector("2016-01-01", "10"),
    Vector("2016-01-02", "11"),
    Vector("2016-01-03", "12"),
    Vector("2016-01-04", "13"),
    Vector("2016-01-05", "14"))
    
    println(x.map { _.mkString("\t") }.mkString("\n"))
}