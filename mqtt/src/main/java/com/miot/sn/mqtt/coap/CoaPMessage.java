package com.miot.sn.mqtt.coap;

import java.io.Serializable;

@SuppressWarnings({ "serial" })
public class CoaPMessage implements Serializable {

	private String topic;
	private String payLoad;
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getPayLoad() {
		return payLoad;
	}

	public void setPayLoad(String payLoad) {
		this.payLoad = payLoad;
	}

	public CoaPMessage payLoad(String payLoad) {
		this.payLoad = payLoad;
		return this;
	}

	public CoaPMessage topic(String topic) {
		this.topic = topic;
		return this;
	}

	public CoaPMessage id(int id) {
		this.id = id;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CoaPMessage [topic=").append(topic).append(", payLoad=").append(payLoad).append(", id=")
				.append(id).append("]");
		return builder.toString();
	}

}
