/**
 *
 */
package com.miot.sn.mqtt;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ramon
 *
 */
public class ThingspeakClientHandler extends AbstractClientHandler {

	private Logger LOGGER = LoggerFactory.getLogger(ThingspeakClientHandler.class);

	private MqttClient outClient;

	@Override
	public void handleMessage(Message message) {

		// Connect to the MQTT server
		LOGGER.info("Connecting Thingspeak");
		LOGGER.info("Connected Thingspeak");
		// Send the message to the server, control is not returned until
		// it has been delivered to the server meeting the specified
		// quality of service.
		try {
			getOutClient().publish(getPublishTopic(message.getTopic()), message.getMqttMessage());
			getOutClient().disconnect();
		} catch (MqttException e) {
			LOGGER.error("Error publishing message", e);
		}
		// Disconnect the client
		LOGGER.info("Disconnected");

	}

	private String getPublishTopic(String topicName) {
		String result = null;
		if (topicName != null) {
			String groupName = getConfig().getOrDefault("groupName", "g2");
			int pos = topicName.indexOf(groupName);
			if (pos >= 0) {
				result = topicName.substring(groupName.length() + 1, topicName.length());
			}
		}
		return result;
	}

	private MqttConnectOptions getConOpts() {
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		return connOpts;
	}

	public MqttClient getOutClient() throws MqttException {
		if (outClient == null) {
			outClient = new MqttClient(getConfig().getOrDefault("thingspeakUrl", "tcp://mqtt.thingspeak.com:1883"),
					UUID.randomUUID().toString());
		}
		if (!outClient.isConnected()) {
			outClient.connect(getConOpts());
		}
		return outClient;
	}

	public void setOutClient(MqttClient outClient) {
		this.outClient = outClient;
	}

	@Override
	protected void init() {
		// May be maintain open connection

	}

}
