package com.miot.sn.mqtt.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "temperature", "humidity", "preasure", "battery", "acc_x", "acc_y", "acc_z", "sn_id" })
public class G2SnNodeData implements Serializable {

	@JsonProperty("temperature")
	private Double temperature;
	@JsonProperty("humidity")
	private Double humidity;
	@JsonProperty("preasure")
	private Double preasure;
	@JsonProperty("battery")
	private Double battery;
	@JsonProperty("acc_x")
	private Double accX;
	@JsonProperty("acc_y")
	private Double accY;
	@JsonProperty("acc_z")
	private Double accZ;
	@JsonProperty("sn_id")
	private String snId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	private final static long serialVersionUID = -2948864050102078522L;

	@JsonProperty("temperature")
	public Double getTemperature() {
		return temperature;
	}

	@JsonProperty("temperature")
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public G2SnNodeData withTemperature(Double temperature) {
		this.temperature = temperature;
		return this;
	}

	@JsonProperty("humidity")
	public Double getHumidity() {
		return humidity;
	}

	@JsonProperty("humidity")
	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}

	public G2SnNodeData withHumidity(Double humidity) {
		this.humidity = humidity;
		return this;
	}

	@JsonProperty("preasure")
	public Double getPreasure() {
		return preasure;
	}

	@JsonProperty("preasure")
	public void setPreasure(Double preasure) {
		this.preasure = preasure;
	}

	public G2SnNodeData withPreasure(Double preasure) {
		this.preasure = preasure;
		return this;
	}

	@JsonProperty("battery")
	public Double getBattery() {
		return battery;
	}

	@JsonProperty("battery")
	public void setBattery(Double battery) {
		this.battery = battery;
	}

	public G2SnNodeData withBattery(Double battery) {
		this.battery = battery;
		return this;
	}

	@JsonProperty("acc_x")
	public Double getAccX() {
		return accX;
	}

	@JsonProperty("acc_x")
	public void setAccX(Double accX) {
		this.accX = accX;
	}

	public G2SnNodeData withAccX(Double accX) {
		this.accX = accX;
		return this;
	}

	@JsonProperty("acc_y")
	public Double getAccY() {
		return accY;
	}

	@JsonProperty("acc_y")
	public void setAccY(Double accY) {
		this.accY = accY;
	}

	public G2SnNodeData withAccY(Double accY) {
		this.accY = accY;
		return this;
	}

	@JsonProperty("acc_z")
	public Double getAccZ() {
		return accZ;
	}

	@JsonProperty("acc_z")
	public void setAccZ(Double accZ) {
		this.accZ = accZ;
	}

	public G2SnNodeData withAccZ(Double accZ) {
		this.accZ = accZ;
		return this;
	}

	@JsonProperty("sn_id")
	public String getSnId() {
		return snId;
	}

	@JsonProperty("sn_id")
	public void setSnId(String snId) {
		this.snId = snId;
	}

	public G2SnNodeData withSnId(String snId) {
		this.snId = snId;
		return this;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public G2SnNodeData withAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("temperature", temperature).append("humidity", humidity)
				.append("preasure", preasure).append("battery", battery).append("accX", accX).append("accY", accY)
				.append("accZ", accZ).append("snId", snId).append("additionalProperties", additionalProperties)
				.toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(humidity).append(accX).append(additionalProperties).append(battery)
				.append(accZ).append(accY).append(snId).append(preasure).append(temperature).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if (other instanceof G2SnNodeData == false) {
			return false;
		}
		G2SnNodeData rhs = (G2SnNodeData) other;
		return new EqualsBuilder().append(humidity, rhs.humidity).append(accX, rhs.accX)
				.append(additionalProperties, rhs.additionalProperties).append(battery, rhs.battery)
				.append(accZ, rhs.accZ).append(accY, rhs.accY).append(snId, rhs.snId).append(preasure, rhs.preasure)
				.append(temperature, rhs.temperature).isEquals();
	}

}
