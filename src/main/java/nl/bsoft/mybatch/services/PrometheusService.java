package nl.bsoft.mybatch.services;

import com.codahale.metrics.servlets.MetricsServlet;
import lombok.extern.slf4j.Slf4j;
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.servlet.ServletContextHandler;
//import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
//@Service
public class PrometheusService {

    public PrometheusService() {
        try {
            startUp();
        } catch (Exception e) {
            log.error("Couldnot startup the Prometheus server");
        }
    }

    public void startUp() throws IOException {
/*
        Server server = new Server(1234);
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new MetricsServlet()), "/metrics");

 */
    }
}
