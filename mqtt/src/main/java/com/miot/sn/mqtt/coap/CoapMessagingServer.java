package com.miot.sn.mqtt.coap;

import org.eclipse.californium.core.CoapServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The executable main class.
 *
 */
public class CoapMessagingServer extends Thread {

	private Logger LOGGER = LoggerFactory.getLogger(CoapMessagingServer.class);

	private CoapServer server;

	public void setup() {
		server = new CoapServer();
		// server.setMessageDeliverer(deliverer);
		LOGGER.info("Coap Server Started:{}", server);
		CoapMessagingResource observableResource = new CoapMessagingResource("node");
		CoaPMessageHandler.getInstance().add(observableResource);
		server.add(observableResource);
	}

	@Override
	public void start() {
		try {
			server.start();
			LOGGER.info("COAP Server started", server);
		} catch (Exception e) {
			LOGGER.error("Error stating Server", e);
		}
	}

}