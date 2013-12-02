/**
 * 
 */
package com.mgc.inbound.pojo;

/**
 * @author momogod415
 * 
 */
public class ClientInfo {

	Integer clinetType;
	String clientIpAddress;
	Integer clinetPortNumber;

	public Integer getClinetType() {
		return clinetType;
	}

	public void setClinetType(Integer clinetType) {
		this.clinetType = clinetType;
	}

	public String getClientIpAddress() {
		return clientIpAddress;
	}

	public void setClientIpAddress(String clientIpAddress) {
		this.clientIpAddress = clientIpAddress;
	}

	public Integer getClinetPortNumber() {
		return clinetPortNumber;
	}

	public void setClinetPortNumber(Integer clinetPortNumber) {
		this.clinetPortNumber = clinetPortNumber;
	}

}
