package com.moviebooking

import play.api.libs.json.{Json, OFormat}

case class SeatNumber(val row:String, val number:String) {

}


object SeatNumber {
  implicit val format: OFormat[SeatNumber] = Json.format[SeatNumber]
}