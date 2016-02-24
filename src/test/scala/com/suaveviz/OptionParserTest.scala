package com.suaveviz

import org.scalatest._
class OptionParserTest extends FlatSpec with Matchers {

  "A option parser" should "parse a single input file" in {
    val parser = new OptionParser(Seq())
    parser.parse(Array("foo.txt")) shouldBe Map("input" -> "foo.txt")
  }

  "A option parser" should "parse a boolean flag" in {
    val parser = new OptionParser(Seq("arg1"))
    parser.parse(Array("--arg1")) shouldBe Map("arg1" -> "true")
  }

  "A option parser" should "parse a flag with a value" in {
    val parser = new OptionParser(Seq("arg1"))
    parser.parse(Array("--arg1", "foobar")) shouldBe Map("arg1" -> "foobar")
  }

  "A option parser" should "parse multiple flags" in {
    val parser = new OptionParser(Seq("arg1", "arg2", "arg3"))
    parser.parse(Array("--arg1", "foobar", "--arg2", "--arg3", "false")) shouldBe Map("arg1" -> "foobar", "arg2" -> "true", "arg3" -> "false")
  }

  "A option parser" should "parse an input file plus multiple flags" in {
    val parser = new OptionParser(Seq("arg1", "arg2", "arg3"))
    parser.parse(Array("foo.txt", "--arg1", "foobar", "--arg2", "--arg3", "false")) shouldBe Map(
      "arg1" -> "foobar",
      "arg2" -> "true",
      "arg3" -> "false",
      "input" -> "foo.txt")
  }

  "A option parser" should "ignore invalid option" in {
    val parser = new OptionParser(Seq(""))
    intercept[RuntimeException] { // we blow up because the options passed aren't valid
      parser.parse(Array("--arg1", "foobar", "--arg2", "--arg3", "false"))
    }
  }
}