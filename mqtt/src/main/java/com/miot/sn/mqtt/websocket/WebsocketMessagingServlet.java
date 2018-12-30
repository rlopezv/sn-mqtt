package com.miot.sn.mqtt.websocket;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 * Well, it's just a servlet declaration.
 *
 */
@SuppressWarnings("serial")
public class WebsocketMessagingServlet extends WebSocketServlet {

	private final Map<String, Session> userSessions = new HashMap<>();

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.register(WebsocketMessagingAdapter.class);
	}

}