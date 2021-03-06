package com.moviebooking

import java.sql.{Connection, ResultSet, Timestamp}
import java.util

import com.geekday.common.Repository

import scala.collection.JavaConverters._
import scala.collection.immutable

abstract class MovieBookingRepository extends Repository {
  def connection:Connection
  def beginTransaction(): Unit = {
    connection.setAutoCommit(false)
  }

  def commit() = {
    connection.commit()
  }

  def rollback() = {
    connection.rollback()
  }

  protected def getDbName = "moviebooking"

  override def getMigrations: util.List[String] = {
    val migrations = List(
      "create table cinemas(id INT PRIMARY KEY, name varchar(50))",
      "create table screens(id INT PRIMARY KEY, cinema_id INT, name varchar(50))",

      "create table movies(id INT PRIMARY KEY, name varchar(50), movie_cast varchar(200), summary varchar(500))",
      "create table buyers(id INT PRIMARY KEY, cinema_id INT, screen_id varchar(50))",

      "create table shows(id INT PRIMARY KEY, cinema_id INT, screen_id INT, movie_id INT, start_time TIMESTAMP, name varchar(20))",
      "create table show_seats(id INT PRIMARY KEY, show_id INT, row varchar(10), number varchar(10), booked Boolean)"
    )

    migrations ++ seedData

  }.asJava

  private def seedData = {
    val seed = List(
      //test data
      "insert into cinemas(id, name) values(1, \'city pride\')",
      "insert into cinemas(id, name) values(2, \'e-square\')",

      "insert into screens(id, cinema_id, name) values(1, 1, \'screen1\')",
      "insert into screens(id, cinema_id, name) values(2, 1, \'screen2\')",

      "insert into screens(id, cinema_id, name) values(5, 2, \'screen1\')",
      "insert into screens(id, cinema_id, name) values(6, 2, \'screen2\')",

      "insert into movies(id, name, movie_cast, summary) values (1, 'kabali', 'rajanikant', 'action movie')",

      "insert into shows(id, cinema_id, screen_id, movie_id, start_time, name) values(1, 1, 1, 1, TIMESTAMP '2017-02-02 13:00:00', 'matiny')",
    )
    val seatNumbers = (1 to 25)
    val rows = ('A' to 'R')
    val showId = 1
    val tuples: immutable.Seq[(Int, Char, Int, Boolean)] = rows.flatMap(row => seatNumbers.map(n => {
      (showId, row, n, false)
    }))
    val zipWithIndex: immutable.Seq[((Int, Char, Int, Boolean), Int)] = tuples.zipWithIndex
    val generatedData = zipWithIndex.map(tuple ⇒ s"""insert into show_seats(id, show_id, row, number, booked) values (${tuple._2 + 1} , ${tuple._1._1}, '${tuple._1._2}', '${tuple._1._3}', ${tuple._1._4})""")
    seed ++ generatedData
  }

  def update[T](callback: Connection ⇒ T): Unit = {
    execute(connection ⇒ callback(connection))
  }

  def query[T](callback: Connection ⇒ T): T = {
    execute(connection ⇒ callback(connection))
  }

  def execute[T](callback:Connection ⇒ T): T = {
    val connection = getConnection
    try {
      connection.setAutoCommit(false)
      callback(connection)

    } finally{
      connection.commit()
    }
  }
}

class ShowRepository extends MovieBookingRepository {

  val connection = getConnection

  def listShows(): List[Show] = {

      val ps = connection.prepareStatement(s"select * from shows")
      val resultSet = ps.executeQuery()
      var shows = List[Show]()
      while(resultSet.next()) {
        shows = shows :+ newShow(resultSet, connection)
      }
      shows
  }

  def getShow(showId: String): Show = {
      val ps = connection.prepareStatement(s"select * from shows " +
        s" where id = ${showId}")
      val resultSet = ps.executeQuery()
      resultSet.next()
      newShow(resultSet, connection)
  }


  def newShow(resultSet: ResultSet, connection: Connection) = {
    val id = resultSet.getInt("Id")
    val cinemaId = resultSet.getInt("cinema_id")
    val screenId = resultSet.getInt("screen_id")
    val movieId = resultSet.getInt("movie_id")
    val startTime: Timestamp = resultSet.getTimestamp("start_time")
    val name = resultSet.getString("name")

    val ps1 = connection.prepareStatement(s"select * from show_seats " +
      s" where show_id = ${id}")

    val resultSet1 = ps1.executeQuery()
    var availableSeats = List[Seat]()
    while (resultSet1.next()) {
      availableSeats = availableSeats :+ newSeat(resultSet1)
    }
    new Show(id, movieId, cinemaId, screenId, startTime.toString, availableSeats)
  }

  def getAvailableSeats(showId: String): List[Seat] = {
      val ps = connection.prepareStatement(s"select * from show_seats " +
        s" where show_id = ${showId} and booked=false")
      val resultSet = ps.executeQuery

      var availableSeats = List[Seat]()
      while (resultSet.next()) {
        availableSeats = availableSeats :+ newSeat(resultSet)
      }

      availableSeats
  }

  private def newSeat(resultSet: ResultSet) = {
    new Seat(resultSet.getInt("id"),
      resultSet.getInt("show_id"),
      resultSet.getString("row"),
      resultSet.getString("number"),
      resultSet.getBoolean("booked"))
  }


  def save(show: Show): Any = {
      show.showSeats.foreach(seat ⇒ {
        val ps = connection.prepareStatement(s"update show_seats set booked=?" +
          s" where id = ?")
        ps.setBoolean(1, seat.booked)
        ps.setInt(2, seat.id)
        ps.executeUpdate()
      })
  }

}