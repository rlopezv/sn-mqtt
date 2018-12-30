/**
 *
 */
package com.miot.sn.mqtt;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miot.sn.config.MqttClientConfig;

/**
 * @author ramon
 *
 */
public abstract class AbstractClientHandler implements MqttCallback, Runnable {

	private Logger LOGGER = LoggerFactory.getLogger(AbstractClientHandler.class);

	private Client client = null;
	private Map<String, String> config = new HashMap<>();
	private boolean exit = false;
	private final BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();

	public AbstractClientHandler() {
	}

	public void config(MqttClientConfig clientConfig) {
		this.config = clientConfig.getAdditionalProperties();
		this.client = new Client(clientConfig);
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.
	 * Throwable)
	 */
	@Override
	public void connectionLost(Throwable th) {
		LOGGER.warn("Connection lost", th);
		while (!getClient().isConnected()) {
			LOGGER.warn("Reconnecting");
			try {
				getClient().connect();
				getClient().subscribe();
			} catch (MqttException e) {
				LOGGER.error("Error reconnecting", e);
				try {
					this.wait(1000);
				} catch (InterruptedException e1) {
					LOGGER.error("Interrupted Exeption", e);
				}
			}

		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.paho.client.mqttv3.MqttCallback#deliveryComplete(org.eclipse.
	 * paho.client.mqttv3.IMqttDeliveryToken)
	 */
	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.
	 * String, org.eclipse.paho.client.mqttv3.MqttMessage)
	 */
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		if (messageQueue.add(new Message().topic(topic).mqttMessage(message))) {
			LOGGER.info("Message added");
		} else {
			LOGGER.warn("Message discarded");
		}
	}

	public void stop() throws MqttException {
		LOGGER.info("Stopping client");
		this.exit = true;
		this.getClient().disconnect();
	}

	public boolean isActive() {
		return !exit;
	}

	public Map<String, String> getConfig() {
		return config;
	}

	public void setConfig(Map<String, String> config) {
		this.config = config;
	}

	protected Message nextMessage() throws InterruptedException {
		return messageQueue.take();
	}

	@Override
	public void run() {
		// If necessary it configure external connections
		try {
			connect();
			getClient().setHandler(this);
			init();
			while (isActive()) {
				try {
					Message message = nextMessage();
					if (MessageTypeEnum.MQTTMESSAGE == message.getType()) {
						handleMessage(message);
					} else {
						stop();
					}
				} catch (Exception e) {
					LOGGER.error("Error handling message", e);
				}
			}
		} catch (MqttException e1) {
			LOGGER.error("Cannot connect to broker, waiting");
			try {
				synchronized (this) {
					wait(100);
				}
			} catch (InterruptedException e) {
				LOGGER.error("Error syncing");
			}
		}

	}

	protected void connect() throws MqttException {
		getClient().start();
	}

	protected abstract void init();

	protected abstract void handleMessage(Message message);
}
