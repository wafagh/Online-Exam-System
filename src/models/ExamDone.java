package models;

import java.io.Serializable;

public class ExamDone implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String Exam_id;
	private String Exam_course;
	private String grade;
	private String comment;
	private String examCopy;
	private String exam_status;
	private String finishTime;
	private boolean suspectedCopying=false;
	private boolean IsConfirmed=false;
	private String examInporgID;
	
	public ExamDone( String exam_id, String exam_course, String grade ,String status,String comment,String time,String examInporgID) {
	
		this.Exam_id = exam_id;
		this.Exam_course = exam_course;
		this.grade = grade;
		this.exam_status=status;
		this.comment=comment;
		this.finishTime=time;
		this.examInporgID=examInporgID;
		
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public String getExam_status() {
		return exam_status;
	}
	public void setExam_status(String exam_status) {
		this.exam_status = exam_status;
	}
	public boolean isSuspectedCopying() {
		return suspectedCopying;
	}
	public void setSuspectedCopying(boolean suspectedCopying) {
		this.suspectedCopying = suspectedCopying;
	}
	public boolean isIsConfirmed() {
		return IsConfirmed;
	}
	public void setIsConfirmed(boolean isConfirmed) {
		IsConfirmed = isConfirmed;
	}
	public String getExam_id() {
		return Exam_id;
	}

	public String getExam_course() {
		return Exam_course;
	}

	public String getExamInporgID() {
		return examInporgID;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getExamCopy() {
		return examCopy;
	}
	public void setExamCopy(String examCopy) {
		this.examCopy = examCopy;
	}
	public String toString() {
		return Exam_id+" "+Exam_course+" "+grade+" "+exam_status+" "+suspectedCopying+" "+IsConfirmed;
	}
	

}
