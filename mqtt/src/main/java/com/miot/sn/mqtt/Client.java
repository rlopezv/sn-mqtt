package com.miot.sn.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miot.sn.config.MqttClientConfig;

public class Client {

	private Logger LOGGER = LoggerFactory.getLogger(Client.class);

	private MqttClient client = null;
	private MqttClientConfig config = null;
	private MemoryPersistence persistence = null;

	public Client(MqttClientConfig clientConfig) {
		this.config = clientConfig;
		this.persistence = new MemoryPersistence();
	}

	public MqttClient getClient() {
		return client;
	}

	protected void setClient(MqttClient client) {
		this.client = client;
	}

	public MqttClientConfig getConfig() {
		return config;
	}

	public void setConfig(MqttClientConfig config) {
		this.config = config;
	}

	public void start() throws MqttException {
		client = new MqttClient(getConfig().getBrokerUrl(), getConfig().getClientId(), persistence);
		while (!client.isConnected()) {
			try {
				connect();
			} catch (MqttException e) {
				LOGGER.error("Cannot connect to mqttBorker");
				synchronized (this) {
					try {
						wait(1000);
					} catch (InterruptedException e1) {
						LOGGER.error("Interrupted exception");
					}
				}
			}
		}
		subscribe();
		LOGGER.info("Started client");
	}

	public void stop() throws MqttException {
		client.disconnect();
	}

	void connect() throws MqttException {
		if (this.client != null && !this.client.isConnected()) {
			this.client.connect(getMqttConnectOptions());
		}
		LOGGER.info("Connected to mqtt broker");
	}

	public boolean isConnected() {
		return client.isConnected();
	}

	private MqttConnectOptions getMqttConnectOptions() {
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setAutomaticReconnect(getConfig().getAutomaticReconnect());
		connOpts.setConnectionTimeout(getConfig().getConTimeout());
		connOpts.setKeepAliveInterval(getConfig().getKeepAliveInterval());
		if (getConfig().getLastWillMessage() != null && getConfig().getControlTopic() != null) {
			connOpts.setWill(getConfig().getControlTopic(), getConfig().getLastWillMessage().getBytes(), 0, true);
		}
		if (getConfig().getUser() != null) {
			connOpts.setUserName(getConfig().getUser());
		}
		if (getConfig().getPassword() != null) {
			connOpts.setPassword(getConfig().getPassword().toCharArray());
		}

		connOpts.setCleanSession(getConfig().getCleanSession());
		return connOpts;
	}

	void subscribe() throws MqttException {
		client.subscribe(getConfig().getTopic() + '#', getConfig().getQos());
		LOGGER.info("Subscribed to:{}", getConfig().getTopic() + '#');
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Client [config=").append(config).append("]");
		return builder.toString();
	}

	public void disconnect() throws MqttException {
		if (client.isConnected()) {
			this.client.disconnect();
		}
	}

	public void setHandler(AbstractClientHandler abstractClientHandler) {
		this.client.setCallback(abstractClientHandler);

	}

}
