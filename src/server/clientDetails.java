package server;

import java.io.Serializable;

public class clientDetails implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String hostName;
	private String ip;
	private String Status;
	public clientDetails(String hostName, String ip, String status) {
		super();
		this.hostName = hostName;
		this.ip = ip;
		Status = status;
	}
	public clientDetails() {
		// TODO Auto-generated constructor stub
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	
	

}
