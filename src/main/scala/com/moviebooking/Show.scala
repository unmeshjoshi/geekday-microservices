package com.moviebooking

import javax.persistence.{Entity, Table}

@Entity
@Table(name = "shows")
class Show(val showId:Int, val movieId: Int, val cinemaId: Int, val screenId: Int, val startTime:String, val showSeats: List[Seat]) {
  def reserveSeats(requestedSeats: List[Seat]): Unit = {
    val filteredSeats = showSeats.filter(seat ⇒ requestedSeats.contains(seat))
    filteredSeats.foreach(seat ⇒ {
      if(!seat.isAvailable)
        throw new RuntimeException(s"${seat} is already booked")
      else
        seat.book()
    })
  }
}

