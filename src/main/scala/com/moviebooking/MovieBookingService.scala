package com.moviebooking

class MovieBookingService {

  def bookSeats(showId:Int, seats:List[Seat]): Unit = {
        val show = new MovieBookingRepository().getShow(showId)
        show.reserveSeats(List())

  }

}
