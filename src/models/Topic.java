package models;

import java.io.Serializable;

public class Topic implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String topicID;
	private String topic;
	public Topic(String topicID, String topic) {
		super();
		this.topicID = topicID;
		this.topic = topic;
	}

	public String getTopicID() {
		return topicID;
	}

	public void setTopicID(String topicID) {
		this.topicID = topicID;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

}
