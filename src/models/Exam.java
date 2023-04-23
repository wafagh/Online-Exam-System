package models;

import java.io.Serializable;

public class Exam implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String examid;
	private String course;
	private String topic;
	private String gradeperquestion;
	private String time;
	private String lecture;
	private int numofquests;
	//private String notesforstudent;
	//private String notesforlecture;
	private String examinprogressid;
	private boolean firstenterflag;
	private String date;
	private String starttime;
	private String tempLecutrer;
	private String examnumber;
	private String status;

	private String commentforteacher;
	private String commentforstudent;

	public Exam(String examid, String topic, String course, String gradeperquestion, String time, String lecture,
			int numofquests) {
		super();
		this.examid = examid;
		this.topic = topic;
		this.course = course;
		this.gradeperquestion = gradeperquestion;
		this.time = time;
		this.lecture = lecture;
		this.numofquests = numofquests;
		setFirstenterflag(true);

	}
	public void addNotesForExam(String notesforstudent , String notesforlecture) {
		this.commentforstudent = notesforstudent;
		this.commentforteacher = notesforlecture;
	}
	public String getExamid() {
		return examid;
	}
	public void setExamid(String examid) {
		this.examid = examid;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getGradeperquestion() {
		return gradeperquestion;
	}
	public void setGradeperquestion(String gradeperquestion) {
		this.gradeperquestion = gradeperquestion;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getLecture() {
		return lecture;
	}
	public void setLecture(String lecture) {
		this.lecture = lecture;
	}
	public int getNumofquests() {
		return numofquests;
	}
	public void setNumofquests(int numofquests) {
		this.numofquests = numofquests;
	}
	/*public String getNotesforstudent() {
		return notesforstudent;
	}
	public void setNotesforstudent(String notesforstudent) {
		this.notesforstudent = notesforstudent;
	}
	public String getNotesforlecture() {
		return notesforlecture;
	}
	public void setNotesforlecture(String notesforlecture) {
		this.notesforlecture = notesforlecture;
	}*/
	public String getExaminprogressid() {
		return examinprogressid;
	}
	public void setExaminprogressid(String examinprogressid) {
		this.examinprogressid = examinprogressid;
	}
	public boolean getFirstenterflag() {
		return firstenterflag;
	}
	public void setFirstenterflag(boolean firstenterflag) {
		this.firstenterflag = firstenterflag;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getTempLecutrer() {
		return tempLecutrer;
	}
	public void setTempLecutrer(String tempLecutrer) {
		this.tempLecutrer = tempLecutrer;
	}
	public String getExamnumber() {
		return examnumber;
	}
	public void setExamnumber(String examnumber) {
		this.examnumber = examnumber;
	}
	public String getCommentforteacher() {
		return commentforteacher;
	}
	public void setCommentforteacher(String commentforteacher) {
		this.commentforteacher = commentforteacher;
	}
	public String getCommentforstudent() {
		return commentforstudent;
	}
	public void setCommentforstudent(String commentforstudent) {
		this.commentforstudent = commentforstudent;
	}	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
