package com.miot.sn.mqtt.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

/**
 * MessagingAdapter responsible to handle connection, receiving data, forward
 * the data to
 *
 */
public class WebsocketMessagingAdapter extends WebSocketAdapter {

	private Session session;

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		WebsocketMessageHandler.getInstance().removeSession(session.getRemoteAddress().toString());
		super.onWebSocketClose(statusCode, reason);
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		super.onWebSocketError(cause);
	}

	@Override
	public void onWebSocketConnect(Session session) {
		super.onWebSocketConnect(session);
		this.session = session;
		WebsocketMessageHandler.getInstance().addSession(session);
	}

}