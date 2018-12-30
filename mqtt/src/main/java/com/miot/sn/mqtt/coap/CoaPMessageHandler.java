package com.miot.sn.mqtt.coap;

import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoaPMessageHandler {

	private static CoaPMessageHandler instance;

	private Logger LOGGER = LoggerFactory.getLogger(CoaPMessageHandler.class);

	public static CoaPMessageHandler getInstance() {
		if (instance == null) {
			instance = new CoaPMessageHandler();
		}
		return instance;
	}

	private HashMap<String, CoapMessagingResource> sessions = new HashMap<>();

	private CoaPMessageHandler() {
	}

	public void send(String topic, MqttMessage message) {
		for (Entry<String, CoapMessagingResource> sessionEntry : sessions.entrySet()) {
			sessionEntry.getValue().publish(buildMessage(topic, message));

		}
	}

	private String buildMessage(String topic, MqttMessage message) {
		CoaPMessage wsMessage = new CoaPMessage().topic(topic).id(message.getId()).payLoad(message.toString());
		return wsMessage.toString();
	}

	public void add(CoapMessagingResource observableResource) {
		sessions.put(observableResource.getName(), observableResource);

	}

}