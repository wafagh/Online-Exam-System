package models;
import java.io.Serializable;

public class MessagesClass implements Serializable
 {
	private static final long serialVersionUID = 1L;

	private Messages msgType;
	private Object msgData;
	private Object msgData1;
	public MessagesClass(Messages msgType, Object msgData) {
		this.msgType = msgType;
		this.msgData = msgData;
	}
	public MessagesClass(Messages msgType,Object msgData,Object msgData1) {
		this.msgType = msgType;
		this.msgData = msgData;
		this.setMsgData1(msgData1);
	}
	public Messages getMsgType() {
		return msgType;
	}
	
	public Object getMsgData() {
		return msgData;
	}
	public Object getMsgData1() {
		return msgData1;
	}
	public void setMsgData1(Object msgData1) {
		this.msgData1 = msgData1;
	}
	
	


}
