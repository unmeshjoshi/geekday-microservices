package com.moviebooking

import javax.ws.rs._
import javax.ws.rs.core.MediaType

import play.api.libs.json.Json


@Path("/")
class MovieBookingResource {
  @GET
  @Path("show/{showId}/available-seats")
  @Produces(Array(MediaType.APPLICATION_JSON))
  def getAvailableSeats(@PathParam("showId") showId: String): String = {
    val availableSeats = new ShowRepository().getAvailableSeats(showId)
    Json.toJson(availableSeats.map(s ⇒ SeatNumber(s.rowNumber, s.number))).toString()
  }

  @POST
  @Path("show/{showId}")
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  def bookSeats(@PathParam("showId") showId: String, requestedSeatsJson: String): String = {
    println(requestedSeatsJson)
    val repository = new ShowRepository()
    try {
      repository.beginTransaction()
      val jsValue = Json.parse(requestedSeatsJson.trim)
      val requestedSeatNumbers: List[SeatNumber] = jsValue.as[List[SeatNumber]]
      val show = repository.getShow(showId)
      show.reserveSeats(requestedSeatNumbers)
      repository.save(show)
      repository.commit()

    } catch {
      case e: Throwable ⇒ {
        repository.rollback()
        e.printStackTrace()
      }
    }
    ""
  }
}
