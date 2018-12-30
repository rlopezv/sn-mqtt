/**
 *
 */
package com.miot.sn.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ramon
 *
 */
public class LogClientHandler extends AbstractClientHandler {

	private Logger LOGGER = LoggerFactory.getLogger(LogClientHandler.class);

	@Override
	public void handleMessage(Message message) {
		LOGGER.info("Message Arrived");
		LOGGER.info("Topic:{}", message.getTopic());
		LOGGER.info("Id:{}", message.getMqttMessage().getId());
		LOGGER.info("Payload:{}", message.getMqttMessage().toString());
		LOGGER.info("Qos:{}", message.getMqttMessage().getQos());
	}

	@Override
	protected void init() {
		// Does nothing

	}

}
