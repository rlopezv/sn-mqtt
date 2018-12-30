/**
 *
 */
package com.miot.sn.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ramon
 *
 */
public class AppConfig {

	public final List<MqttClientConfig> clients = new ArrayList<MqttClientConfig>();

	public List<MqttClientConfig> getClientConfigs() {
		return clients;
	}

	public void setClientConfigs(final List<MqttClientConfig> clients) {
		getClientConfigs().clear();
		if (clients != null) {
			getClientConfigs().addAll(clients);
		}
	}

	public void addClientConfig(final MqttClientConfig client) {
		if (client != null) {
			getClientConfigs().add(client);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AppConfig [clients=").append(clients).append("]");
		return builder.toString();
	}

}
