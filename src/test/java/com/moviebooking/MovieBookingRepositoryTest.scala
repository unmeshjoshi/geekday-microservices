package com.moviebooking

import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FunSuite}

class MovieBookingRepositoryTest extends FunSuite with BeforeAndAfterAll {

  val repository = new MovieBookingRepository()
  override def beforeAll() = {
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

  test("should list shows") {
    println(repository.listShows())
  }

  test("should be able to book seats for a show") {
    val availableSeats = repository.getAvailableSeats("1")
    assert(4 == availableSeats.length)

    val show = repository.getShow(1)
    assert(show != null)

    show.reserveSeats(List(SeatNumber("A", "1"), SeatNumber("A", "2"), SeatNumber("B", "2")))
    repository.save(show)

    val seats = repository.getAvailableSeats("1")
    assert(1 == seats.length)
  }

}
