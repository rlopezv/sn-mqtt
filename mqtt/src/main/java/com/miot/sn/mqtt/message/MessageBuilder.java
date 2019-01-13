/**
 *
 */
package com.miot.sn.mqtt.message;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ramon
 *
 */
public class MessageBuilder {

	private final static MessageBuilder builder = new MessageBuilder();

	private ObjectMapper objectMapper = new ObjectMapper();

	private MessageBuilder() {

	}

	public static MessageBuilder getBuilder() {
		return builder;
	}

	public String buildStringMessage(String node, String message) throws MessageException {
		String result = null;
		G2SnNodeData json = buildMessage(node, message);
		if (json != null) {
			try {
				// OneSaitBody request = new OneSaitBody();
				// request.setBody(json);
				result = objectMapper.writeValueAsString(json);
			} catch (JsonProcessingException e) {
				throw new MessageException(e);
			}
		}
		return result;
	}

	public G2SnNodeData buildMessage(String node, String message) {
		Map<String, String> valueMap = buildMap(message);
		G2SnNodeData result = null;
		if (!valueMap.isEmpty()) {
			result = new G2SnNodeData();
			result.setAccX(parseDouble(valueMap.get("field1")));
			result.setAccY(parseDouble(valueMap.get("field2")));
			result.setAccZ(parseDouble(valueMap.get("field3")));
			result.setBattery(parseDouble(valueMap.get("field4")));
			result.setHumidity(parseDouble(valueMap.get("field5")));
			result.setPreasure(parseDouble(valueMap.get("field6")));
			result.setTemperature(parseDouble(valueMap.get("field7")));
			result.setSnId(node);

		}
		return result;
	}

	private Double parseDouble(String value) {
		Double result = null;
		if (value != null) {
			result = Double.valueOf(value);
		}
		return result;
	}

	/**
	 * Splits string on values
	 *
	 * @param message
	 * @return
	 */
	public Map<String, String> buildMap(String message) {
		Map<String, String> result = new HashMap<>();
		if (message != null) {
			String[] sValues = StringUtils.split(message, "&");
			for (String sValue : sValues) {
				String[] value = StringUtils.split(sValue, "=");
				if (value.length == 2) {
					result.put(value[0], value[1]);
				}
			}
		}
		return result;
	}

}
