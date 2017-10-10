package com.moviebooking

case class Seat(id:Int, showId:Int, rowNumber:String, number:String, var booked:Boolean = false) {
  def isAvailable = !booked
  def book() = booked = true
}