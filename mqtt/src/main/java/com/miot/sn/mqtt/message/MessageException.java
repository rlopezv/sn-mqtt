/**
 *
 */
package com.miot.sn.mqtt.message;

/**
 * @author ramon
 *
 */
@SuppressWarnings("serial")
public class MessageException extends Exception {

	public MessageException(Exception e) {
		super(e);
	}

}
