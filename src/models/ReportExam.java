package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class ReportExam implements Serializable {
	private static final long serialVersionUID = 1L;
	private String examinprogID;
	private String course;
	private String topic;
	private double Average;//calc   // DONE 
	private int numbOfStudents;//calc   //DONE 
	private double StandDeviation;//calc   //DONE
	private String Date;
	private double medianX;//calc   /DONE 
	private double HighestGrade;//calc  //DONE 
	private double lowestGrade;//calc   //DONE
	private  ArrayList<Double> grades=new ArrayList<Double>();
	
	public ReportExam() {
		
	}
	
	public ReportExam(String examinprogID,String course, String topic, double average, int numbOfStudents, double standDeviation,
			String date, double medianX, double highestGrade, double lowestGrade) {
		
		this.examinprogID=examinprogID;
		this.course = course;
		this.topic = topic;
		Average = average;
		this.numbOfStudents = numbOfStudents;
		StandDeviation = standDeviation;
		Date = date;
		this.medianX = medianX;
		HighestGrade = highestGrade;
		this.lowestGrade = lowestGrade;
	}
	public ReportExam(String examinprogID,String course,String topic,String Date)
	{
		this.examinprogID=examinprogID;
		this.course = course;
		this.topic = topic;
		this.Date = Date;
	}



	public ReportExam(String examinprogID) {
		
	}
	public String getExaminprogID() {
		return examinprogID;
	}



	public void setExaminprogID(String examinprogID) {
		this.examinprogID = examinprogID;
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



	public double getAverage() {
		return Average;
	}



	public void setAverage(double average) {
		Average = average;
	}



	public int getNumbOfStudents() {
		return numbOfStudents;
	}



	public void setNumbOfStudents(int numbOfStudents) {
		this.numbOfStudents = numbOfStudents;
	}



	public double getStandDeviation() {
		return StandDeviation;
	}



	public void setStandDeviation(double standDeviation) {
		StandDeviation = standDeviation;
	}



	public String getDate() {
		return Date;
	}



	public void setDate(String date) {
		Date = date;
	}



	public double getMedianX() {
		return medianX;
	}



	public void setMedianX(double medianX) {
		this.medianX = medianX;
	}



	public double getHighestGrade() {
		return HighestGrade;
	}



	public void setHighestGrade(double highestGrade) {
		HighestGrade = highestGrade;
	}



	public double getLowestGrade() {
		return lowestGrade;
	}



	public void setLowestGrade(double lowestGrade) {
		this.lowestGrade = lowestGrade;
	}
	public void addGrade(double grade) {// when adding a grade to spsecif exam then add the correct size 
										// and update the max and min grades 
		grades.add(grade);
		System.out.println("Grade"+grade);
//		this.numbOfStudents=grades.size();
		numbOfStudents++;
		if(HighestGrade==0||grade>HighestGrade) {
			HighestGrade=grade;
		}
		if(lowestGrade==0||grade<lowestGrade) {
			lowestGrade=grade;
		}
		
	}
	public void calcmedianX() {// calc the median
		Collections.sort(grades);
		System.out.println("num of students "+numbOfStudents);
		if(numbOfStudents%2==1)
		{
			medianX=grades.get((numbOfStudents+1)/2-1);
			
		}
		else {
			medianX=((grades.get((numbOfStudents)/2-1)+grades.get((numbOfStudents)/2))/2);
		}
	}
	public void calcAverage()
	{
		  this.Average = grades.stream().mapToDouble(d -> d).average().orElse(0.0);
	}
	public void calcStandDeviation() {
		StandDeviation=0;
		for(double num: grades) {
			StandDeviation += Math.pow(num - Average, 2);
        }

		StandDeviation = Math.sqrt(StandDeviation/numbOfStudents);
		
	}
	
	public ArrayList<Double> getGrades() {
		return grades;
	}
	
	
	

}
