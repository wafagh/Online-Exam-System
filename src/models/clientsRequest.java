package models;

import ocsf.server.ConnectionToClient;

public class clientsRequest {
private ConnectionToClient client;
private RequestsForTime conifrmInfo;
public clientsRequest(ConnectionToClient client, RequestsForTime conifrmInfo) {
	super();
	this.client = client;
	this.conifrmInfo = conifrmInfo;
}
public ConnectionToClient getClient() {
	return client;
}
public void setClient(ConnectionToClient client) {
	this.client = client;
}
public RequestsForTime getConifrmInfo() {
	return conifrmInfo;
}
public void setConifrmInfo(RequestsForTime conifrmInfo) {
	this.conifrmInfo = conifrmInfo;
}
	
	
}
