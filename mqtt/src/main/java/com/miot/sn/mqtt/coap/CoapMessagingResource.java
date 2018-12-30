package com.miot.sn.mqtt.coap;

import static org.eclipse.californium.core.coap.CoAP.ResponseCode.CHANGED;
import static org.eclipse.californium.core.coap.CoAP.ResponseCode.DELETED;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoapMessagingResource extends CoapResource {

	private String resourceStatus;

	private Logger LOGGER = LoggerFactory.getLogger(CoapMessagingResource.class);

	/**
	 * Instantiates a new publish resource.
	 */
	public CoapMessagingResource(String name) {
		// set resource identifier
		super(name);
		setObservable(true); // enable observing
		setObserveType(Type.CON); // configure the notification type to CONs
		getAttributes().setObservable(); // mark observable in the Link-Format
	}

	public void publish(String message) {
		LOGGER.info("Publish Message");
		resourceStatus = message;
	}

	@Override
	public void handleGET(CoapExchange exchange) {
		exchange.setMaxAge(1); // the Max-Age value should match the update interval
		exchange.respond(resourceStatus);
	}

	@Override
	public void handleDELETE(CoapExchange exchange) {
		delete(); // will also call clearAndNotifyObserveRelations(ResponseCode.NOT_FOUND)
		exchange.respond(DELETED);
	}

	@Override
	public void handlePUT(CoapExchange exchange) {
		exchange.respond(CHANGED);
		changed(); // notify all observers
	}
}