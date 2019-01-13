/**
 *
 */
package com.miot.sn.mqtt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miot.sn.AppConstants;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;

/**
 *
 * Implementation of RS Client based on okhttp
 *
 * @author ramon
 *
 */
public class ThingspeakRSClientHandler extends AbstractClientHandler {

	private Logger LOGGER = LoggerFactory.getLogger(ThingspeakRSClientHandler.class);
	OkHttpClient client = new OkHttpClient();
	private static final String[] CONTENT = { "application/json" };

	@Override
	public void handleMessage(Message message) {
		LOGGER.info("Message Arrived");
		try {
			Builder builder = new Request.Builder().url(getUrl(message)).addHeader("accept", CONTENT[0]);
			Request request = builder.build();
			LOGGER.info("Request done:{}" + request.httpUrl());
			Response response = client.newCall(request).execute();
			LOGGER.info("Response ({}):{}", response.code(), response.body().string());
		} catch (IOException e) {
			LOGGER.error("Error sending message", e);
		}
	}

	private String getUrl(Message message) {
		StringBuilder result = new StringBuilder();
		result.append(getApiPath()).append("?api_key=").append(getKey(message.getTopic())).append("&")
				.append(message.getMqttMessage().toString());
		return result.toString();

	}

	private String getKey(String topic) {
		String result = null;
		if (topic != null) {
			String[] topicElements = topic.split("/");
			result = topicElements[topicElements.length - 1];
		}
		return result;
	}

	protected String getApiPath() {
		return getConfig().get(AppConstants.THINKGSPEAK_API_PATH);
	}

	@Override
	protected void init() {
		// Does nothing

	}

}
