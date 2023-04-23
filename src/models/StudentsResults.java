package models;

import java.io.Serializable;

public class StudentsResults implements Serializable {

	private static final long serialVersionUID = 1L;

	private String examid;
	private String examcourse;
	private String grade;
	private String comments;
	private String status;
	private String studentID;
	private String firstname;
	private String lastname;
private String examinprogressid;

	public StudentsResults(String examid, String examcourse, String grade, String comments, String status,
			String studentID, String firstname, String lastname) {
		super();
		this.examid = examid;
		this.examcourse = examcourse;
		this.grade = grade;
		this.comments = comments;
		this.status = status;
		this.studentID = studentID;
		this.firstname = firstname;
		this.lastname = lastname;
	}
	public StudentsResults(String examid, String examcourse, String grade, String comments, String status,
			String studentID, String firstname, String lastname,String examinprogressid) {
		super();
		this.examid = examid;
		this.examcourse = examcourse;
		this.grade = grade;
		this.comments = comments;
		this.status = status;
		this.studentID = studentID;
		this.firstname = firstname;
		this.lastname = lastname;
		this.examinprogressid=examinprogressid;
	}


	public String getExamid() {
		return examid;
	}

	public void setExamid(String examid) {
		this.examid = examid;
	}

	public String getExamcourse() {
		return examcourse;
	}

	public void setExamcourse(String examcourse) {
		this.examcourse = examcourse;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getExaminprogressid() {
		return examinprogressid;
	}

	public void setExaminprogressid(String examinprogressid) {
		this.examinprogressid = examinprogressid;
	}

}
