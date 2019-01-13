/**
 *
 */
package com.miot.sn;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParseException;
import com.miot.sn.config.AppConfig;
import com.miot.sn.config.MqttClientConfig;
import com.miot.sn.mqtt.AbstractClientHandler;

/**
 *
 * Class in charge of creating the environment for serving mqtt captured
 *
 * @author ramon
 *
 */
public class App {

	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
	private final ObjectMapper mapper = new ObjectMapper();

	private static App app = null;

	private AppConfig config;

	private ExecutorService executor = null;

	private List<AbstractClientHandler> clients = new ArrayList<>();

	protected AppConfig getConfig() {
		return config;
	}

	protected void setConfig(AppConfig config) {
		this.config = config;
	}

	protected ObjectMapper getMapper() {
		return mapper;
	}

	public static void main(final String[] args) {
		String configFile = null;
		System.out.println("Starting App");
		if (args == null || args.length == 0) {
			System.out.println("Usage: java -jar app.jar filename");
		} else {
			app = new App();
			try {
				configFile = args[0];
				app.configure(configFile);
				app.start();
			} catch (MqttException | JsonParseException | IOException e) {
				LOGGER.error("Error starting app", e);
			}
		}
		// Shutdown hook to ensure ordered close of consumers
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				app.finish();
			}
		});
	}

	private void finish() {
		for (AbstractClientHandler client : clients) {
			try {
				client.stop();
			} catch (MqttException e) {
				LOGGER.info("Error stopping client", client);
			}
		}
	}

	/**
	 * Start the application Uses ExecutorService to limit the number of threads
	 * involved
	 *
	 * @throws MqttException
	 */
	private void start() throws MqttException {

		executor = Executors.newFixedThreadPool(clients.size());
		for (AbstractClientHandler client : clients) {
			execute(client);
		}

	}

	/**
	 *
	 * @param consumer
	 */
	private void execute(Runnable client) {
		executor.execute(client);
	}

	/**
	 *
	 */
	public void shutdownAndAwaitTermination() {
		executor.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
				executor.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
					LOGGER.error("Pool did not terminate");
				}
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			executor.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Instantiate and configure clients from file
	 *
	 * @param configPath file containing client configuration
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private void configure(String configPath) throws JsonParseException, JsonMappingException, IOException {
		setConfig(parseConfig(configPath));
		for (MqttClientConfig clientConfig : getConfig().getClientConfigs()) {
			try {
				Class<?> handlerClass = Class.forName(clientConfig.getImplClassName());
				Constructor<?> handlerConstructor = handlerClass.getConstructor();
				AbstractClientHandler client = (AbstractClientHandler) handlerConstructor.newInstance();
				client.config(clientConfig);
				clients.add(client);
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				LOGGER.error("Error creating handler", e);
			}

		}

	}

	final AppConfig parseConfig(String path) throws JsonParseException, JsonMappingException, IOException {
		return getMapper().readValue(new File(path), AppConfig.class);

	}

}
