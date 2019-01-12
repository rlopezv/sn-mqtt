package com.miot.sn.mqtt.message;

import java.io.Serializable;

@SuppressWarnings("serial")
public class OneSaitBody implements Serializable {

	private G2SnNodeData body;

	public G2SnNodeData getBody() {
		return body;
	}

	public void setBody(G2SnNodeData body) {
		this.body = body;
	}

}
