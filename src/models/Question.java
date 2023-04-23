package models;

import java.io.Serializable;

public class Question implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idquest;
	private String questtext;
	private String questtopic;
	private String answer1;
	private String answer2;
	private String answer3;
	private String answer4;
	private int correctanswer;
	private String teachername;
	private int questnumber;
	
	
	public Question(String idquest, String questtopic, String questtext , String answer1, String answer2, String answer3,
			 String answer4, int correctanswer, String teachername, int questnumber) {
		this.idquest = idquest;
		this.questtext = questtext;
		this.questtopic = questtopic;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.answer4 = answer4;
		this.correctanswer = correctanswer;
		this.teachername = teachername;
		this.questnumber = questnumber;
	}

	public String getIdquest() {
		return idquest;
	}

	public void setIdquest(String idquest) {
		this.idquest = idquest;
	}

	public String getQuesttext() {
		return questtext;
	}

	public void setQuesttext(String questtext) {
		this.questtext = questtext;
	}

	public String getQuesttopic() {
		return questtopic;
	}

	public void setQuesttopic(String questtopic) {
		this.questtopic = questtopic;
	}

	public String getAnswer1() {
		return answer1;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	public String getAnswer2() {
		return answer2;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	public String getAnswer3() {
		return answer3;
	}

	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}

	public String getAnswer4() {
		return answer4;
	}

	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}

	public int getCorrectanswer() {
		return correctanswer;
	}

	public void setCorrectanswer(int correctanswer) {
		this.correctanswer = correctanswer;
	}

	public String getTeachername() {
		return teachername;
	}

	public void setTeachername(String teachername) {
		this.teachername = teachername;
	}

	public int getQuestnumber() {
		return questnumber;
	}

	public void setQuestnumber(int questnumber) {
		this.questnumber = questnumber;
	}
	
}
