package com.suaveviz

/** Parses command line arguments and returns a Map[String, String] containing each option and its value
 *  
 *  ./foo file.txt --option1 value1 --option2 => Map("input" -> "file.txt", "option1" -> value1, "option2" -> true) 
  */
class OptionParser(validOptions: Seq[String]) {
  def parse(args: Array[String]): Map[String, String] = {
    parse(args.toList)
  }

  private def parse(args: List[String], map: Map[String, String] = Map.empty): Map[String, String] = {
    args match {
      // N-args
      case opt1 :: opt2 :: tail if validOption(opt1) && validOption(opt2) => parse(opt2 +: tail, map + (opt1.drop(2) -> "true"))
      case inputFile :: opt :: tail if !validOption(inputFile) && validOption(opt) => parse(opt +: tail, map + ("input" -> inputFile))
      case opt :: value :: tail if validOption(opt) => parse(tail, map + (opt.drop(2) -> value))

      // 1 arg
      case opt :: Nil if validOption(opt) => map + (opt.drop(2) -> "true")
      case inputFile :: Nil => map + ("input" -> inputFile)

      // no args
      case Nil => map
      case _ => sys.error("Invalid option")
    }
  }

  private def validOption(name: String): Boolean = {
    name.startsWith("--") && validOptions.exists { o => o == name.drop(2) }
  }
}