package com.miot.sn.mqtt.websocket;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The executable main class.
 *
 */
public class WebsocketMessagingServer extends Thread {

	private Logger LOGGER = LoggerFactory.getLogger(WebsocketMessagingServer.class);

	private Server server;

	public void setup() {
		server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(1080);
		server.addConnector(connector);
		ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		handler.setContextPath("/");
		server.setHandler(handler);
		handler.addServlet(WebsocketMessagingServlet.class, "/ws");
		LOGGER.info("Websocket Server Started:{}", server);
	}

	@Override
	public void start() {
		try {
			server.start();
			server.dump(System.err);
			// server.join();
		} catch (Exception e) {
			LOGGER.error("Error stating Server", e);
		}
	}

}