package com.moviebooking

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import com.geekday.accounting.account.domain.{Account, AccountRepository}

@Path("/")
class MovieBookingResource {
  @GET
  @Path("account/{accountId}")
  @Produces(Array(MediaType.APPLICATION_JSON))
  def getAvailableSeats(@PathParam("movieId") accountId: String, @PathParam("showId") showId: String): List[Seat] = {
    List().asInstanceOf[List[Seat]]
  }
}
