package com.moviebooking

import org.scalatest.{BeforeAndAfterAll, FunSuite}

class MovieBookingRepositoryTest extends FunSuite with BeforeAndAfterAll {

  val repository = new ShowRepository()
  override def beforeAll() = {
    repository.runMigrations()
  }

  test("should create bookings") {
    val availableSeats = repository.getAvailableSeats("1")
    assert(false == availableSeats.isEmpty)
  }

  test("should get show with all seats") {
    val show = repository.getShow("1")
    assert(show != null)
  }

  test("should list shows") {
    println(repository.listShows())
  }

  test("should be able to book seats for a show") {
    val showId = "1"
    val availableSeats = repository.getAvailableSeats(showId)


    val show = repository.getShow(showId)
    assert(show != null)

    show.reserveSeats(List(SeatNumber("A", "1"), SeatNumber("A", "2"), SeatNumber("B", "2")))
    show.reserveSeats(List(SeatNumber("A", "5"), SeatNumber("A", "7"), SeatNumber("B", "10")))
    repository.save(show)

    assert((availableSeats.length - 6) == repository.getAvailableSeats(showId).length)
  }

}

