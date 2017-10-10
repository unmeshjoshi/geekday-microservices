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
    val availableSeats = new MovieBookingRepository().getAvailableSeats(showId)
    Json.toJson(availableSeats.map(s ⇒ SeatNumber(s.rowNumber, s.number))).toString()
  }

  @POST
  @Path("show/{showId}")
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  def bookSeats(@PathParam("showId") showId: String, requestedSeatsJson:String): String = {
    println(requestedSeatsJson)
    try {
    val jsValue = Json.parse(requestedSeatsJson.trim)
    val requestedSeatNumbers: List[SeatNumber] = jsValue.as[List[SeatNumber]]
    val repository = new MovieBookingRepository()
    val show = repository.getShow(showId)
    show.reserveSeats(requestedSeatNumbers)
    repository.save(show)
    } catch {
      case e:Throwable ⇒ e.printStackTrace()
    }
    ""
  }
}
