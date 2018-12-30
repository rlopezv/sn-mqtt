package com.miot.sn.mqtt.coap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoapMessagingServerTest {

	private Logger LOGGER = LoggerFactory.getLogger(CoapMessagingServerTest.class);

	@Test
	public void test() throws InterruptedException {
		CoapClient client = new CoapClient("coap://localhost:5683/node");
		client.useCONs();
		client.setTimeout(10000L);
		CoapObserveRelation relation = client.observeAndWait(new CoapHandler() {
			@Override
			public void onLoad(CoapResponse response) {
				String content = response.getResponseText();

				LOGGER.info("NOTIFICATION: {}", content);
			}

			@Override
			public void onError() {
				LOGGER.error("OBSERVING FAILED");
			}
		});
		Thread.sleep(100000);
	}

}
