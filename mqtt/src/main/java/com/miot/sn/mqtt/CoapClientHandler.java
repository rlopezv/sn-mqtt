/**
 *
 */
package com.miot.sn.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miot.sn.mqtt.coap.CoaPMessageHandler;
import com.miot.sn.mqtt.coap.CoapMessagingServer;

/**
 * @author ramon
 *
 */
public class CoapClientHandler extends AbstractClientHandler {

	private Logger LOGGER = LoggerFactory.getLogger(CoapClientHandler.class);

	private CoapMessagingServer server;

	@Override
	protected void init() {
		server = new CoapMessagingServer();
		server.setup();
		server.start();
	}

	@Override
	public void handleMessage(Message message) {
		CoaPMessageHandler.getInstance().send(message.getTopic(), message.getMqttMessage());
	}

}
