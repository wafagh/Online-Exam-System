package models;

import java.util.HashMap;

public class StudentAnswers {

	private String examinprogressid;
	private String studentid;
	private String startedtime;
	private String finishedtime;
	private int numberofquestions;
	private String status;
	private int finalgrade;


	private HashMap<String, String> questionsanswers; // HashMap contains student answer for exam , the key is question id and
												// the value is the student answer for exam
	

	
	public StudentAnswers(String studentid, int numberofquestions) {
		super();
		this.studentid = studentid;
		this.numberofquestions = numberofquestions;
		this.finalgrade = 0;
		this.status = "Fail";
	}
	public void addquestionanswer(String questionid,String studentanswer) {
		
		questionsanswers.put(questionid,studentanswer);	
	}
	public void addgradeandstatus(int gradeofquestion) {
		finalgrade += gradeofquestion;
		if(finalgrade >= 55) {
			setStatus("Pass");
		}
	}
	public String getExaminprogressid() {
		return examinprogressid;
	}
	public void setExaminprogressid(String examinprogressid) {
		this.examinprogressid = examinprogressid;
	}
	public String getStudentid() {
		return studentid;
	}
	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}
	public String getStartedtime() {
		return startedtime;
	}
	public void setStartedtime(String startedtime) {
		this.startedtime = startedtime;
	}
	public String getFinishedtime() {
		return finishedtime;
	}
	public void setFinishedtime(String finishedtime) {
		this.finishedtime = finishedtime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public HashMap<String, String> getQuestionsanswers() {
		return questionsanswers;
	}
	public int getNumberofquestions() {
		return numberofquestions;
	}
	public String getFinalgrade() {
		return String.valueOf(finalgrade);
	}
}
