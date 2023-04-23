package models;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckExam {

	private String examid;
	private String topic;
	private String course;
	private String grade;
	private String gradeperquestion;
	private String LecturerID;
	private int numofquestions;
	private String examinprogressid;
	
	private HashMap<String, String> questionanswers; // hash map for questions answers the key is question id and the
														// value is the question answer
	
	private HashMap<String, int[]> correctanswermap; // hash map for correct answers the key is studentid and the value
														// is the array of student correct answer

	private HashMap<String, StudentAnswers> studentanswers; // hash map for student answers the key is studentid and the
															// value is student answers class
	private ArrayList<String> studentidlist;

	private ArrayList<String> questionidlist;
	
	int studentidlistcounter = 0;
	String tempquestionid;
	int numberofstudents;
	int[] tempanswersarray;

	public CheckExam(String examid, String topic, String course, String gradeperquestion, String lecturerID,
			String numofquestions) {
		super();
		this.examid = examid;
		this.topic = topic;
		this.course = course;
		this.grade = Integer.toString(Integer.parseInt(grade) * Integer.parseInt(numofquestions));
		this.LecturerID = lecturerID;
		this.numofquestions = Integer.parseInt(numofquestions);
		this.gradeperquestion = gradeperquestion;
	}

	public void startCheckExam() {// check the exams for student who done the exam and give each student his final
									// grade
		if (null != examinprogressid) {
			numberofstudents = studentidlist.size();
			correctanswermap = new HashMap<String, int[]>();

			for (int i = 0; i < numberofstudents; i++) { // compare each student answer to the right answers and build
															// array which contains the right and wrong answer for each
															// student ( 1 the answer is right - 0 is wrong) and
															// calculate the final grade per each student
				tempanswersarray = new int[numofquestions];
				for (int j = 0; j < numofquestions; j++) {

					tempquestionid = questionidlist.get(j);
					if (studentanswers.get(studentidlist.get(i)).getQuestionsanswers().get(tempquestionid)
							.equals(questionanswers.get(tempquestionid))) {
						tempanswersarray[j] = 1;
						studentanswers.get(studentidlist.get(i)).addgradeandstatus(Integer.parseInt(gradeperquestion));
					} else {
						tempanswersarray[j] = 0;
					}
				}
				correctanswermap.put(studentidlist.get(i), tempanswersarray);

			}

			for (int i = 0; i < numberofstudents; i++) {
				if (!(studentanswers.get(studentidlist.get(i)).getStatus().equals("Suspected copying"))) {
					copytest(studentidlist.get(i), i);
				}
				
			}

		}
	}

	public void copytest(String studentid, int i) { // check each student if copied in the exam and update their status
													// if
													// studentanswers exam
		for (int k = i + 1; k < numberofstudents; k++) {
			if (!(studentanswers.get(studentidlist.get(k)).getStatus().equals("Suspected copying"))) {
				if (comparearrays(correctanswermap.get(studentid), correctanswermap.get(studentidlist.get(k)))) {
					studentanswers.get(studentid).setStatus("Suspected copying");
					studentanswers.get(studentidlist.get(k)).setStatus("Suspected copying");
				}
			}
		}

	}

	public boolean comparearrays(int[] array1, int[] array2) { // compare two correctanswers arrays if two student have
		int counter = 0; // more then 75% wrong answers in common return true
							// (they copied) otherwise return false
		for (int i = 0; i < numofquestions; i++) {
			if (array1[i] == 0) {
				if (array1[i] == array2[i]) {
					if ((++counter / numofquestions) * 100 > 75) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public void addstudentidtolist(String studentid) {
		studentidlist.add(studentid);
	}

	public void addquestionidtolist(String questionid) {
		questionidlist.add(questionid);
	}

	public void addquestion(String questionid, String correctanswer) {

		questionanswers.put(questionid, correctanswer);

	}

	public void addStudentAnswers(String studentid, StudentAnswers answers) {

		studentanswers.put(studentid, answers);
	}
	public StudentAnswers getStudentAnswer(String studentid) {
		return studentanswers.get(studentid);
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

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getGradeperquestion() {
		return gradeperquestion;
	}

	public void setGradeperquestion(String gradeperquestion) {
		this.gradeperquestion = gradeperquestion;
	}

	public String getLecturerID() {
		return LecturerID;
	}

	public void setLecturerID(String lecturerID) {
		LecturerID = lecturerID;
	}

	public int getNumofquestions() {
		return numofquestions;
	}

	public void setNumofquestions(String numofquestions) {
		this.numofquestions = Integer.parseInt(numofquestions);
	}

	public String getExaminprogressid() {
		return examinprogressid;
	}

	public void setExaminprogressid(String examinprogressid) {
		this.examinprogressid = examinprogressid;
	}

	public ArrayList<String> getStudentidlist() {
		return studentidlist;
	}

	public void setStudentidlist(ArrayList<String> studentidlist) {
		this.studentidlist = studentidlist;
	}
}
