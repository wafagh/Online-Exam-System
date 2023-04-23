package models;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class ExamInProgressStudent implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String examid;
	private String topic;
	private String course;
	private String gradeperquestion;
	private String time;
	private String lecture;
	private int numofquests;
	private String studentid;
	private String notesforstudent;
	private String notesforlecture;
	private String status;
	private String examinprogressid;
	private String code;
	public boolean firstenterflag = true;
	private LocalDate localDate;
	private String timefinished;
	private String timestarted;
	private String submitintime;
	private String examstartingtime;
	private String examtype;
	private ArrayList<Question> questions;
	private int[] Answers;
	private String answersfile;
	private long totalSecounds;


	public ExamInProgressStudent(Exam exam,ArrayList<Question> questions,String code,String status){
		this.examid = exam.getExamid();
		this.topic = exam.getTopic();
		this.course = exam.getCourse();
		this.gradeperquestion = exam.getGradeperquestion();
		this.time = exam.getTime();
		this.lecture = exam.getLecture();
		this.numofquests = exam.getNumofquests();
		this.examinprogressid = exam.getExaminprogressid();
		localDate = LocalDate.now();
		this.code = code;
		this.status = status;
		this.questions = questions;

	}
	
	public LocalDate getLocalDate() {
		return localDate;
	}

	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}

	public String getTimefinished() {
		return timefinished;
	}

	public void setTimefinished(String timefinished) {
		this.timefinished = timefinished;
	}

	public String getTimestarted() {
		return timestarted;
	}

	public void setTimestarted(String timestarted) {
		this.timestarted = timestarted;
	}

	public String getExamid() {
		return examid;
	}
	public void setExamid(String examid) {
		this.examid = examid;
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

	public String getNotesforstudent() {
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
	}

	public String getExaminprogressid() {
		return examinprogressid;
	}

	public void setExaminprogressid(String examinprogressid) {
		this.examinprogressid = examinprogressid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ArrayList<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<Question> questions) {
		this.questions = questions;
	}

	public int[] getAnswers() {
		return Answers;
	}

	public void setAnswers(int[] answers2) {
		Answers = answers2;
	}

	public void printAnswers() {
		for(int i = 0 ; i< Answers.length;i++) {
			System.out.println("Question number "+(i+1)+" Answer : "+ Answers[i]);
		
		}
		
	}

	public String getStudentid() {
		return studentid;
	}

	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubmitintime() {
		return submitintime;
	}

	public void setSubmitintime(String submitintime) {
		this.submitintime = submitintime;
	}
	public String getExamstartingtime() {
		return examstartingtime;
	}
	
	public void setExamstartingtime(String examstartingtime) {
		this.examstartingtime = examstartingtime;
	}
	public String getExamtype() {
		return examtype;
	}

	public void setExamtype(String examtype) {
		this.examtype = examtype;
	}

	public String getAnswersfile() {
		return answersfile;
	}

	public void setAnswersfile(String answersfile) {
		this.answersfile = answersfile;
	}


	public long getTotalSecounds() {
		return totalSecounds;
	}

	public void setTotalSecounds(long totalSecounds) {
		this.totalSecounds = totalSecounds;
	}
}
