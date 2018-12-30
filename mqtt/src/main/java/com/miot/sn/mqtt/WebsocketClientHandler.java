/**
 *
 */
package com.miot.sn.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miot.sn.mqtt.websocket.WebsocketMessageHandler;
import com.miot.sn.mqtt.websocket.WebsocketMessagingServer;

/**
 * @author ramon
 *
 */
public class WebsocketClientHandler extends AbstractClientHandler {

	private Logger LOGGER = LoggerFactory.getLogger(WebsocketClientHandler.class);

	private WebsocketMessagingServer server;

	@Override
	protected void init() {
		server = new WebsocketMessagingServer();
		server.setup();
		server.start();
	}

	@Override
	public void handleMessage(Message message) {
		WebsocketMessageHandler.getInstance().send(message.getTopic(), message.getMqttMessage());
	}

}
