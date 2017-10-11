package com.moviebooking

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.glassfish.jersey.servlet.ServletContainer

object Main extends App {
  new ShowRepository().runMigrations()
  startWebServer()

  private def startWebServer() = {
    val context = new ServletContextHandler(ServletContextHandler.SESSIONS)
    context.setContextPath("/")
    val jettyServer = new Server(8051)
    jettyServer.setHandler(context)
    val jerseyServlet = context.addServlet(classOf[ServletContainer], "/*")
    jerseyServlet.setInitOrder(0)
    jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", classOf[MovieBookingResource].getCanonicalName)
    try {
      System.out.println("jerseyServlet = " + jerseyServlet)
      jettyServer.start()
      jettyServer.join()
    } catch {
      case t: Throwable ⇒
        t.printStackTrace()
    } finally jettyServer.destroy()
  }
}

