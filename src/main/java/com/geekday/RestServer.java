package com.geekday;

import com.geekday.accounting.account.domain.AccountRepository;
import com.geekday.accounting.account.domain.CustomerSubscriber;
import com.geekday.accounting.customer.domain.AccountSubscriber;
import com.geekday.accounting.customer.domain.CustomerRepository;
import com.geekday.accounting.account.web.AccountResource;
import com.geekday.accounting.customer.web.CustomerResource;
import com.geekday.common.DomainEventPublisher;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


public class RestServer {

    public static void main(String[] args) throws Exception {
        initializeApplication();
        startWebServer();
    }

    private static void initializeApplication() {
        CustomerRepository.initialize();
        AccountRepository.initialize();

        DomainEventPublisher.getInstance();
        new CustomerSubscriber().start();
        new AccountSubscriber().start();
    }

    private static void startWebServer() {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server jettyServer = new Server(8080);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                CustomerResource.class.getCanonicalName()+"," +
                        AccountResource.class.getCanonicalName());

        try {
            System.out.println("jerseyServlet = " + jerseyServlet);
            jettyServer.start();
            jettyServer.join();
        } catch(Throwable t) {
            t.printStackTrace();
        }finally {
            jettyServer.destroy();
        }
    }
}