package com.geekday

import akka.actor.{ActorSystem, Props}
import com.geekday.accounting.AccountActor
import com.geekday.accounting.account.domain.{AccountRepository, CustomerSubscriber}
import com.geekday.accounting.account.web.AccountResource
import com.geekday.accounting.customer.domain.{AccountSubscriber, CustomerRepository}
import com.geekday.accounting.customer.web.CustomerResource
import com.geekday.common.DomainEventPublisher
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.glassfish.jersey.servlet.ServletContainer

object RestServer {
  @throws[Exception]
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("micro-actor")
    val actorRef = system.actorOf(Props(classOf[AccountActor]))
    actorRef ! "update"

    initializeApplication()
    startWebServer()
  }

  private def initializeApplication() = {
    CustomerRepository.initialize()
    AccountRepository.initialize()
    DomainEventPublisher.start
    new CustomerSubscriber().start()
    new AccountSubscriber().start()
  }

  private def startWebServer() = {
    val context = new ServletContextHandler(ServletContextHandler.SESSIONS)
    context.setContextPath("/")
    val jettyServer = new Server(8051)
    jettyServer.setHandler(context)
    val jerseyServlet = context.addServlet(classOf[ServletContainer], "/*")
    jerseyServlet.setInitOrder(0)
    jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", classOf[CustomerResource].getCanonicalName + "," + classOf[AccountResource].getCanonicalName)
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