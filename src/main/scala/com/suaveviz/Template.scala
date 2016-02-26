package com.suaveviz

object Template {
  val prodCSS = Vector("http://suavecharts.com/dist/suave-charts.css")

  val prodJS = Vector(
    "http://suavecharts.com/js/vendor/d3.min.js",
    "http://suavecharts.com/dist/suave-charts.min.js"
  )

  val localCSS = Vector(s"file:///${sys.env("SUAVE_CHARTS_HOME")}/suave-charts.css")

  val localJS = Vector(
    "http://suavecharts.com/js/vendor/d3.min.js",
    s"file:///${sys.env("SUAVE_CHARTS_HOME")}/suave-charts.min.js"
  )

  private def jsTag(s: String): String = {
    s"<script src='${s}'></script>"
  }

  private def cssTag(s: String): String = {
    s"<link href='${s}' rel='stylesheet' />"
  }

  def render(code: String, isLocal: Boolean): String = {
    val css = if (isLocal) localCSS else prodCSS
    val js = if (isLocal) localJS else prodJS
    
    s"""
    <!doctype html>
    <html>
      <head>
        ${css.map { cssTag }.mkString("\n")}
      </head>
      <body>
        <div id="chart"></div>
        ${js.map { jsTag }.mkString("\n")}
        <script>${code}</script>
      </body>
    </html>
  """
  }
}