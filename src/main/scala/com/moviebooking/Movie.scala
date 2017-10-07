package com.moviebooking

import java.time.Duration

import play.api.libs.json.{Json, OFormat}

case class Movie(id:String, name:String, cinemaIds:Seq[String], duration:Duration, summary:String, cast:String) {

}

object Movie {
  implicit val format: OFormat[Movie] = Json.format[Movie]
}