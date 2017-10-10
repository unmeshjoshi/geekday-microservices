package com.moviebooking

case class Show(val showId:Int, val movieId: Int, val cinemaId: Int, val screenId: Int, val startTime:String, val showSeats: List[Seat]) {
  def reserveSeats(requestedSeats: List[SeatNumber]): Unit = {
    println(showSeats)
    val filteredSeats = showSeats.filter(seat ⇒ {
      val number = SeatNumber(seat.rowNumber, seat.number)
      requestedSeats.contains(number)
    })

    println(filteredSeats)

    filteredSeats.foreach(seat ⇒ {
      if(!seat.isAvailable)
        throw new RuntimeException(s"${seat} is already booked")
      else
        seat.book()
    })
  }
}

