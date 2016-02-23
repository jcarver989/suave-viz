package com.suaveviz

object Template {
  val css = Vector(
    "/Users/jcarver/code/suave-charts/dist/suave-charts.css"
  ).map { f => s"<link href='${f}' rel='stylesheet' />" }

  val js = Vector(
    "http://suavecharts.com/js/vendor/d3.min.js",
    "/Users/jcarver/code/suave-charts/dist/suave-charts.min.js"
  ).map { f => s"<script src='${f}'></script>" }

  def render(code: String): String = {
    s"""
    <!doctype html>
    <html>
      <head>
        ${css.mkString("\n")}
      </head>
      <body>
        <div id="chart"></div>
        ${js.mkString("\n")}
        <script>${code}</script>
      </body>
    </html>
  """
  }
}