package com.moviebooking

import org.scalatest.{BeforeAndAfter, FunSuite}

class MovieBookingRepositoryTest extends FunSuite with BeforeAndAfter {

  val repository = new MovieBookingRepository()
  before {
    repository.runMigrations()
  }

  test("should create bookings") {
    val availableSeats = repository.getAvailableSeats("1")
    assert(false == availableSeats.isEmpty)
  }

  test("should get show with all seats") {
    val show = repository.getShow(1)
    assert(show != null)
  }

  test("should be able to book seats for a show") {
    val show = repository.getShow(1)
    assert(show != null)
    show.reserveSeats()
    repository.save()
  }

}
