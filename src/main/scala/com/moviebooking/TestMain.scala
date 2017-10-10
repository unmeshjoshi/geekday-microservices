package com.moviebooking

import play.api.libs.json._

object TestMain extends App {

  val movie = Movie("1", "Superman", Seq("1"), java.time.Duration.ofHours(2), "summary", "cast")
  val start = System.currentTimeMillis()
  val json = Json.toJson(movie)
  val end = System.currentTimeMillis()
  println(s"time=${(end - start)} ms")


  private val jsonValue: JsValue = Json.toJson(List(SeatNumber("A", "1"), SeatNumber("B", "2")))
  println(jsonValue)

  private val jsvalue: JsValue = Json.parse("[{\"row\":\"A\",\"number\":\"1\"},{\"row\":\"B\",\"number\":\"2\"}]")
  println(jsvalue.as[List[SeatNumber]]
  )
}
