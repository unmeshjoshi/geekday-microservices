package com.moviebooking

class MovieBookingService {

  def bookSeats(showId:Int, seats:List[Seat]): Unit = {
        new MovieBookingRepository().getShow(showId)

  }

}
