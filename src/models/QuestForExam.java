package models;

import java.io.Serializable;

public class QuestForExam implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String questid;
	private String examid;
	private String lecturerid;
	
	public QuestForExam(String questid, String examid, String lecturerid) {
		super();
		this.questid = questid;
		this.examid = examid;
		this.lecturerid = lecturerid;
	}

	public String getQuestid() {
		return questid;
	}

	public void setQuestid(String questid) {
		this.questid = questid;
	}

	public String getExamid() {
		return examid;
	}

	public void setExamid(String examid) {
		this.examid = examid;
	}

	public String getLecturerid() {
		return lecturerid;
	}

	public void setLecturerid(String lecturerid) {
		this.lecturerid = lecturerid;
	}

}
