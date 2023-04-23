package models;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import client.ChatClient;
import client.ClientUI;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ExamsInProgress implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String topic;
	private String course;
	private String time;
	private String reduration;
	private String status;
	private String examid;
	private int code;
	private String DateTime;
	private Timer timer;
	public static long totalsec=0;
	long min;
	long sec;
	long hr;
	long temptime = 0;
	private int numofstudents = 0;

	public ExamsInProgress(String id, String topic, String course, String time, String reduration, String status,
			int code) {
		super();
		this.id = id;
		this.topic = topic;
		this.course = course;
		this.time = time;
		this.reduration = reduration;
		this.status = status;
		this.code = code;
	}

	public int getNumofstudents() {
		return numofstudents;
	}

	public void setNumofstudents(int numofstudents) {
		this.numofstudents = numofstudents;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getReduration() {
		return reduration;
	}

	public void setReduration(String reduration) {
		this.reduration = reduration;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExamid() {
		return examid;
	}

	public void setExamid(String examid) {
		this.examid = examid;
	}

	public String getDateTime() {
		return DateTime;
	}

	public void setDateTime(String dateTime) {
		DateTime = dateTime;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public long getTemptime() {
		return temptime;
	}

	public void setTemptime(long temptime) {
		this.temptime = temptime;
	}

	public Timer getTimer() {
		return this.timer;
	}
	
	
	public void convertTime()  {

		
		min = TimeUnit.SECONDS.toMinutes(totalsec);
		sec = totalsec - (min * 60);
		hr = TimeUnit.MINUTES.toHours(min);
		min = min - (hr * 60);
		totalsec--;
	}


	

	public void setTimer() {
		int temphr,temphr1,tempmin,tempmin1,tempsec,tempsec1;/////////////////////////////
		LocalTime now = LocalTime.now();/////////////////////////////////////
		
		String[] str1 = getDateTime().split(" ");
		String[] timeStr = str1[1].split(":");
		
		temphr = now.getHour();/////////////////////////
		tempmin = now.getMinute();/////////////////////////////
		tempsec = now.getSecond();/////////////////////////////
		temphr1 = Integer.parseInt(timeStr[0]);///////////////////////////////
		tempmin1 = Integer.parseInt(timeStr[1]);////////////////////////////
		tempsec1 = Integer.parseInt(timeStr[2]);///////////////////////
		totalsec = ((temphr-temphr1)*60*60);/////////////////////////////////
		totalsec += (tempmin - tempmin1)*60;///////////////////////////////////
		
		
		
		totalsec = (Integer.parseInt(getTime()) * 60) - totalsec + (tempsec1 - tempsec);///////////////////////////////////////////
		this.timer = new Timer();
System.out.println(totalsec);
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						 System.out.println(totalsec);
						convertTime();					
						
						if(totalsec <= 0)
							try {		
								timer.cancel();
								//checkstatus();
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						if(totalsec <= 0) {
							setStatus("Done");
							totalsec=0;
							for(ExamsInProgress a : ChatClient.examslist)
							{
								if(a.getId().equals(getId()))
								{
									
									reduration=a.getReduration();
									time=a.getTime();
								}
							}
							MessagesClass msg = new MessagesClass(Messages.UpdateStatus,new ExamsInProgress(id,topic,course,time,reduration,status,code));
							ClientUI.chat.accept(msg);
							msg= new MessagesClass(Messages.OpenedExamInProgressForm,ChatClient.user);
							ClientUI.chat.accept(msg);
							
							
							try {/////////////////////////////////////////////////////
								Thread.sleep(5000);
							} catch (InterruptedException e) {////////////////////////////////////
						
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							msg = new MessagesClass(Messages.CheckExam,id);
							ClientUI.chat.accept(msg);
						}
					}

				});
			}
		};

		timer.schedule(timerTask, 0, 1000);
		while(totalsec>0)
		{
			MessagesClass msg = new MessagesClass(Messages.UpdateStatus,new ExamsInProgress(id,topic,course,time,reduration,status,code));
			ClientUI.chat.accept(msg);
			msg= new MessagesClass(Messages.OpenedExamInProgressForm,ChatClient.user);
			ClientUI.chat.accept(msg);
			
			
			try {/////////////////////////////////////////////////////
				Thread.sleep(5000);
			} catch (InterruptedException e) {////////////////////////////////////
		
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			msg = new MessagesClass(Messages.CheckExam,id);
			ClientUI.chat.accept(msg);
		}
		
		
	}

	public long getTotalsec() {
		return totalsec;
	}

	public void setTotalsec(long totalsec) {
		this.totalsec = totalsec;
	}

}
