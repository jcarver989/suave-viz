lazy val commonSettings = Seq(
  organization := "com.suaveviz",
  version := "0.1.0",
  scalaVersion := "2.11.7"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "suaveviz",
    libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"
  )
