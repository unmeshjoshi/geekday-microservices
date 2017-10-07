package com.retail.catalog

import java.sql.{DriverManager, SQLException}

class CatalogRepository {
  def initialize(): Unit = {
    try {
      val connection = getConnection
      val statement = connection.createStatement
      statement.execute("create table catalog(id varchar(50), name varchar(50))")
      statement.execute("create table catalog_item(id varchar(50), catalog_id varchar(50), name varchar(50))")
      statement.execute("create table cart_item(id varchar(50), catalog_item_id varchar(50), cart_id varchar(50), quantity int")
      statement.execute("create table cart(id varchar(50), )")
      statement.execute("create table inventory(id varchar(50), customerName varchar(50))")
      statement.execute("create table inventory_item(id varchar(50), customerName varchar(50))")
      connection.commit()
    } catch {
      case e: Exception ⇒
        throw new RuntimeException(e)
    }
  }

  private def getConnection = try
    DriverManager.getConnection("jdbc:hsqldb:mem:catalog", "sa", "")
  catch {
    case e: SQLException ⇒
      throw new RuntimeException(e)
  }


  def get() = {
    new Catalog(Seq(new CatalogItem("1", "book1", "/book1.png", BigDecimal("100.12")),
                    new CatalogItem("2", "book2", "/book2.png", BigDecimal("90.12"))))
  }
}
