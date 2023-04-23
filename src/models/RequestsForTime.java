package models;

import java.io.Serializable;

public class RequestsForTime implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private int id_request;
	private String exam_id;
	private String requesterId;
	private String requesterName;
	private String preduration;
	private String reduration;
	private String topic;
	private String course;
	private String reason;
	
	public RequestsForTime(int id_request, String exam_id, String requesterId, String requesterName,
			String preduration, String reduration, String topic, String course,String reason) {
		this.id_request = id_request;
		this.exam_id = exam_id;
		this.requesterId = requesterId;
		this.requesterName = requesterName;
		this.preduration = preduration;
		this.reduration = reduration;
		this.topic = topic;
		this.course = course;
		this.reason=reason;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getId_request() {
		return id_request;
	}
	public void setId_request(int id_request) {
		this.id_request = id_request;
	}
	public String getExam_id() {
		return exam_id;
	}
	public void setExam_id(String exam_id) {
		this.exam_id = exam_id;
	}
	public String getRequesterId() {
		return requesterId;
	}
	public void setRequesterId(String requesterId) {
		this.requesterId = requesterId;
	}
	public String getRequesterName() {
		return requesterName;
	}
	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}
	public String getPreduration() {
		return preduration;
	}
	public void setPreduration(String preduration) {
		this.preduration = preduration;
	}
	public String getReduration() {
		return reduration;
	}
	public void setReduration(String reduration) {
		this.reduration = reduration;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}

	
	
   
}
