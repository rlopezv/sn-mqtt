/**
 *
 */
package com.miot.sn.mqtt;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miot.sn.AppConstants;
import com.miot.sn.mqtt.message.MessageBuilder;
import com.miot.sn.mqtt.message.MessageException;
import com.miot.sn.mqtt.onesait.swagger.client.ApiCallback;
import com.miot.sn.mqtt.onesait.swagger.client.ApiClient;
import com.miot.sn.mqtt.onesait.swagger.client.ApiException;
import com.miot.sn.mqtt.onesait.swagger.client.api.DefaultApi;

/**
 * @author ramon
 *
 */
public class OneSaitClientHandler extends AbstractClientHandler {

	private Logger LOGGER = LoggerFactory.getLogger(OneSaitClientHandler.class);
	private DefaultApi dataApi;
	private ApiCallback<Void> callBack;
	private static final String[] CONTENT = { "application/json" };

	@Override
	public void handleMessage(Message message) {
		LOGGER.info("Message Arrived");
		try {
			dataApi.insertAsync(getApiKey(), parseMessage(message), getCallBack());
		} catch (ApiException | MessageException e) {
			LOGGER.error("Error sending message", e);
		}
	}

	private String parseMessage(Message message) throws MessageException {
		String result = MessageBuilder.getBuilder().buildStringMessage(message.getTopic(),
				message.getMqttMessage().toString());
		LOGGER.info("MessageBuild:{}", result);
		return result;

	}

	@Override
	protected void init() {
		ApiClient oneSaitClient = new ApiClient();
		oneSaitClient.setBasePath(getConfig().get(AppConstants.ONESAIT_API_PATH));
		oneSaitClient.selectHeaderContentType(CONTENT);
		oneSaitClient.setDebugging(true);
		oneSaitClient.setVerifyingSsl(false);
		dataApi = new DefaultApi(oneSaitClient);
	}

	protected DefaultApi getDataApi() {
		return dataApi;
	}

	protected ApiCallback<Void> getCallBack() {
		return callBack;
	}

	protected String getApiKey() {
		return getConfig().get(AppConstants.ONESAIT_API_KEY);
	}

	/**
	 * Callback for progress
	 *
	 * @author ramon
	 *
	 */
	@SuppressWarnings("unused")
	private class OneSaitCallBack implements ApiCallback<Void> {

		@Override
		public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
			LOGGER.error("Error: {}", statusCode, e);

		}

		@Override
		public void onSuccess(Void result, int statusCode, Map<String, List<String>> responseHeaders) {
			LOGGER.error("Success: {}", statusCode);
		}

		@Override
		public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {
			LOGGER.error("Uploading: {}/{}", bytesWritten, contentLength);

		}

		@Override
		public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
			// TODO Auto-generated method stub

		}

	}
}
