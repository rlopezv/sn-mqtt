package com.miot.sn.mqtt.websocket;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebsocketMessageHandler {

	private static WebsocketMessageHandler instance;

	private Logger LOGGER = LoggerFactory.getLogger(WebsocketMessageHandler.class);

	public static WebsocketMessageHandler getInstance() {
		if (instance == null) {
			instance = new WebsocketMessageHandler();
		}
		return instance;
	}

	/**
	 * Mantains open user Sessions
	 */
	private HashMap<String, Session> userSessions = new HashMap<>();

	private WebsocketMessageHandler() {
	}

	public void send(String message) {
		for (Entry<String, Session> userSessionEntry : userSessions.entrySet()) {
			try {
				userSessionEntry.getValue().getRemote().sendString(message);
			} catch (IOException e) {
				LOGGER.error("Error sending Websocket Message: {}", userSessionEntry.getKey(), e);
			}
		}
	}

	public void addSession(Session session) {
		LOGGER.info("New session:{}", session.getRemote().getInetSocketAddress().toString());
		userSessions.put(session.getRemoteAddress().toString(), session);

	}

	public void removeSession(String sessionId) {
		LOGGER.info("Removed session:{}", sessionId);
		userSessions.remove(sessionId);
	}

	public void send(String topic, MqttMessage message) {
		for (Entry<String, Session> userSessionEntry : userSessions.entrySet()) {
			try {
				userSessionEntry.getValue().getRemote().sendString(buildMessage(topic, message));
			} catch (IOException e) {
				LOGGER.error("Error sending Websocket Message: {}", userSessionEntry.getKey(), e);
			}
		}
	}

	private String buildMessage(String topic, MqttMessage message) {
		WebsocketMessage wsMessage = new WebsocketMessage().topic(topic).id(message.getId())
				.payLoad(message.toString());
		return wsMessage.toString();
	}

	@SuppressWarnings({ "serial" })
	private class WebsocketMessage implements Serializable {

		private String topic;
		private String payLoad;
		private int id;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getTopic() {
			return topic;
		}

		public void setTopic(String topic) {
			this.topic = topic;
		}

		public String getPayLoad() {
			return payLoad;
		}

		public void setPayLoad(String payLoad) {
			this.payLoad = payLoad;
		}

		public WebsocketMessage payLoad(String payLoad) {
			this.payLoad = payLoad;
			return this;
		}

		public WebsocketMessage topic(String topic) {
			this.topic = topic;
			return this;
		}

		public WebsocketMessage id(int id) {
			this.id = id;
			return this;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("WebsocketMessage [topic=").append(topic).append(", payLoad=").append(payLoad)
					.append(", id=").append(id).append("]");
			return builder.toString();
		}

	}

}