package models;

import java.io.Serializable;

public class AnswersReview implements Serializable{
	
	private static final long serialVersionUID = 1L; 
	private int question_serial;
	private String questiontext;
	private int studentAnswer;
	private int correctAnswer;
	private String isCorrect;

	private String studentID;
	public AnswersReview(int question_serial, String questiontext, int studentAnswer, int correctAnswer) {
		this.question_serial = question_serial;
		this.questiontext = questiontext;
		this.studentAnswer = studentAnswer;
		this.correctAnswer = correctAnswer;
	}
	public String getIsCorrect() {
		return isCorrect;
	}
	public void setIsCorrect(String isCorrect) {
		this.isCorrect = isCorrect;
	}
	public int getQuestion_serial() {
		return question_serial;
	}
	public void setQuestion_serial(int question_serial) {
		this.question_serial = question_serial;
	}
	public String getQuestiontext() {
		return questiontext;
	}
	public void setQuestiontext(String questiontext) {
		this.questiontext = questiontext;
	}
	public int getStudentAnswer() {
		return studentAnswer;
	}
	public void setStudentAnswer(int studentAnswer) {
		this.studentAnswer = studentAnswer;
	}
	public int getCorrectAnswer() {
		return correctAnswer;
	}
	public void setCorrectAnswer(int correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	public String toString() {
		return question_serial+" Q:"+questiontext+"  "+studentAnswer+" "+correctAnswer+" "+isCorrect+" ";
	}
	
	public String getStudentID() {
		return studentID;
	}
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}
	

}
