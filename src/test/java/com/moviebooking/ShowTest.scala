package com.moviebooking

import org.scalatest.FunSuite

class ShowTest extends FunSuite {

  test("should book seats if available") {
    val show = new Show(1, 1, 2, 1, "10:00", List(Seat(1, 1, "A", "1"), Seat(1, 1, "A", "2")))
    show.reserveSeats(List(Seat(1, 1, "A", "1"), Seat(1, 1, "A", "2")))
    assert(show.showSeats.filter(seat⇒seat.isAvailable).isEmpty == true)
  }

  test("should fail if seats are not available") {
    val show = new Show(1, 1, 2, 1, "10:00", List(Seat(1, 1, "A", "1")))
    show.reserveSeats(List(Seat(1, 1, "A", "1"), Seat(1, 1, "A", "2")))
    assert(show.showSeats.filter(seat⇒seat.isAvailable).isEmpty == true)
  }
}
