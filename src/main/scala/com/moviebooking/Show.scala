package com.moviebooking

case class Show(val showId:Int, val movieId: Int, val cinemaId: Int, val screenId: Int, val startTime:String, val showSeats: List[Seat]) {
  def reserveSeats(requestedSeats: List[SeatNumber]): Unit = {
    val filteredSeats = showSeats.filter(seat ⇒ {
        requestedSeats.contains(SeatNumber(seat.rowNumber, seat.number))
    })

    filteredSeats.foreach(seat ⇒ {
      if(!seat.isAvailable)
        throw new RuntimeException(s"${seat} is already booked")
      else
        seat.book()
    })
  }
}

