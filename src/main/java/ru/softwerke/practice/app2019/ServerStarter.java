package ru.softwerke.practice.app2019;

import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class ServerStarter {
    private static final Logger logger = LoggerFactory.getLogger(ServerStarter.class);

    public static void main(String[] args) {
        System.setProperty("org.eclipse.jetty.LEVEL", "INFO");
        System.setProperty("org.glassfish.jersey.LEVEL", "INFO");
        System.setProperty("ru.softwerke.LEVEL", "INFO");

        try {
            new ServerStarter().run();
        } catch (Throwable t) {
            logger.error(t.getMessage());
        }
    }

    private void run() throws URISyntaxException, MalformedURLException {
        URL indexURL = this.getClass().getResource("/static/index.html");
        if (indexURL == null) {
            throw new RuntimeException("Unable to find static resources");
        }
        URI staticResourcesRootUri = URI.create(indexURL.toURI().toASCIIString().replaceFirst("/index.html$", "/"));

        Server server = new Server(8080);

        ServletContextHandler ctx = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        ctx.setContextPath("/");
        ctx.setBaseResource(Resource.newResource(staticResourcesRootUri));
        ctx.setWelcomeFiles(new String[]{"index.html"});
        ctx.getMimeTypes().addMimeMapping("html", MimeTypes.Type.TEXT_HTML_UTF_8.asString());
        ctx.getMimeTypes().addMimeMapping("js", MimeTypes.Type.APPLICATION_JSON_UTF_8.asString());
        ctx.getMimeTypes().addMimeMapping("css", "text/css;charset=utf-8");

        ServletHolder dynamicServletHolder = ctx.addServlet(ServletContainer.class, "/shop-api/*");
        dynamicServletHolder.setInitParameter("javax.ws.rs.Application", ShopApplication.class.getCanonicalName());

        ServletHolder staticServletHolder = new ServletHolder("default", DefaultServlet.class);
        staticServletHolder.setInitParameter("dirAllowed", "false");
        staticServletHolder.setInitParameter("fileEncoding", "utf-8");

        ctx.addServlet(staticServletHolder, "/");

        server.setHandler(ctx);

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            server.destroy();
        }
    }
}
