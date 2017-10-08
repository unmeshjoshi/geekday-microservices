package com.moviebooking
import java.sql.{ResultSet, Timestamp}

import java.util

import com.geekday.common.Repository

import scala.collection.JavaConverters._

class MovieBookingRepository extends Repository {
  protected def getDbName = "moviebooking"

  override def getMigrations: util.List[String] = {
    List(
      "create table cinemas(id INT PRIMARY KEY, name varchar(50))",
      "create table screens(id INT PRIMARY KEY, cinema_id INT, name varchar(50))",

      "create table movies(id INT PRIMARY KEY, name varchar(50), movie_cast varchar(200), summary varchar(500))",
      "create table buyers(id INT PRIMARY KEY, cinema_id INT, screen_id varchar(50))",

      "create table shows(id INT PRIMARY KEY, cinema_id INT, screen_id INT, movie_id INT, start_time TIMESTAMP, name varchar(20))",
      "create table show_seats(id INT PRIMARY KEY, show_id INT, row varchar(10), number varchar(10), booked Boolean)",

      //test data
      "insert into cinemas(id, name) values(1, \'city pride\')",
      "insert into cinemas(id, name) values(2, \'e-square\')",

      "insert into screens(id, cinema_id, name) values(1, 1, \'screen1\')",
      "insert into screens(id, cinema_id, name) values(2, 1, \'screen2\')",

      "insert into screens(id, cinema_id, name) values(5, 2, \'screen1\')",
      "insert into screens(id, cinema_id, name) values(6, 2, \'screen2\')",

      "insert into movies(id, name, movie_cast, summary) values (1, 'kabali', 'rajanikant', 'action movie')",

      "insert into shows(id, cinema_id, screen_id, movie_id, start_time, name) values(1, 1, 1, 1, TIMESTAMP '2017-02-02 13:00:00', 'matiny')",

      "insert into show_seats(id, show_id, row, number, booked) values (1, 1,  'A', '1', false)",
      "insert into show_seats(id, show_id, row, number, booked) values (2, 1,  'A', '2', false)",
      "insert into show_seats(id, show_id, row, number, booked) values (3, 1,  'B', '1', false)",
      "insert into show_seats(id, show_id, row, number, booked) values (4, 1,  'B', '2', false)",

      "insert into show_seats(id, show_id, row, number, booked) values (5, 2,  'A', '1', false)",
      "insert into show_seats(id, show_id, row, number, booked) values (6, 2,  'A', '2', false)",
      "insert into show_seats(id, show_id, row, number, booked) values (7, 2,  'B', '1', false)",
      "insert into show_seats(id, show_id, row, number, booked) values (8, 2,  'B', '2', false)",

      "insert into show_seats(id, show_id, row, number, booked) values (9, 5,  'A',  '1', false)",
      "insert into show_seats(id, show_id, row, number, booked) values (10, 5,  'A', '2', false)",
      "insert into show_seats(id, show_id, row, number, booked) values (11, 5,  'B', '1', false)",
      "insert into show_seats(id, show_id, row, number, booked) values (12, 5,  'B', '2', false)",

    )
  }.asJava


  def save(show: Show): Any = {
    val connection = getConnection()
    try {
      connection.setAutoCommit(false);


      show.showSeats.foreach(seat ⇒ {
        val ps = connection.prepareStatement(s"update show_seats set booked=?" +
          s" where id = ?")
        ps.setBoolean(1, seat.booked)
        ps.setInt(2, seat.id)
        ps.executeUpdate()
      })

      connection.commit()
    } catch {
      case e:Exception ⇒ connection.rollback()
    }
  }

  def getShow(showId:Int):Show = {

    val connection = getConnection()
    connection.setReadOnly(true)

    val ps = connection.prepareStatement(s"select * from shows " +
      s" where id = ${showId}")

    val resultSet = ps.executeQuery()

    resultSet.next()

    val id = resultSet.getInt("Id")
    val cinemaId = resultSet.getInt("cinema_id")
    val screenId = resultSet.getInt("screen_id")
    val movieId = resultSet.getInt("movie_id")
    val startTime: Timestamp = resultSet.getTimestamp("start_time")
    val name = resultSet.getString("name")

    val ps1 = connection.prepareStatement(s"select * from show_seats " +
      s" where show_id = ${showId}")

    val resultSet1 = ps1.executeQuery()
    resultSet1.next()
    var availableSeats = List[Seat]()
    availableSeats = availableSeats :+ newSeat(resultSet1)
    while (resultSet1.next()) {
      availableSeats = availableSeats :+ newSeat(resultSet1)
    }

    connection.close()

    new Show(id, movieId, cinemaId, screenId, startTime.toString, availableSeats)
  }

  def getAvailableSeats(showId: String): List[Seat] = {
    val connection = getConnection()
    try {
      val ps = connection.prepareStatement(s"select * from show_seats " +
        s" where show_id = ${showId} and booked=false")
      val resultSet = ps.executeQuery

      var availableSeats = List[Seat]()
      while (resultSet.next()) {
        availableSeats = availableSeats :+ newSeat(resultSet)
      }

      availableSeats

    } catch {
      case e: Exception ⇒ throw new RuntimeException(e)
    }
  }

  private def newSeat(resultSet: ResultSet) = {
    new Seat(resultSet.getInt("id"),
      resultSet.getInt("show_id"),
      resultSet.getString("row"),
      resultSet.getString("number"),
      resultSet.getBoolean("booked"))
  }
}
