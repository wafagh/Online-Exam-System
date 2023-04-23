package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import client.ChatClient;
import client.ClientUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.ExamInProgressStudent;
import models.Messages;
import models.MessagesClass;
import models.Question;

public class ExamInProgressController implements Initializable {
	private URL location;
	private ResourceBundle resources;
	private String grey = "#d1caca";
//	private String blue = "#4754a2";
	private int counternumberofquestion = 0; // set number of question to 1 ( there is at least 1 question in exam )
	private int numberofquestionsinexam;
	private ExamInProgressStudent examinprogress;
	private int[] answers;
	private boolean visibleflag = false;
	private boolean colorflag = false;
	private long currenttime;
	private boolean examsubmitted = false;
	private boolean lockexam = false;
	private long min, sec, hr, totalSec = 0; // 
	LocalTime timenow;
	private Timer timer;
	private String Duration;
	private String currentdate;
	ActionEvent tempevent;
	
	public void settotalSec(long l) {
		this.totalSec = l;
	}
	public void convertTime()  {

		min = TimeUnit.SECONDS.toMinutes(totalSec);
		sec = totalSec - (min * 60);
		hr = TimeUnit.MINUTES.toHours(min);
		min = min - (hr * 60);
		Time_ExamInProgress.setText(format(hr) + ":" + format(min) + ":" + format(sec));
		totalSec--;
		
	}


	private String format(long value) {

		if (value < 10) {
			return 0 + "" + value;
		}

		return value + "";
	}

	private void setTimer() {
		int temphr,temphr1,tempmin,tempmin1,tempsec,tempsec1;/////////////////////////////
		LocalTime now = LocalTime.now();/////////////////////////////////////
		String[] str;/////////////////////////////////////////////////////
		str = (ChatClient.examforstudent.getStarttime().split(":"));//////////////////////////////////
		temphr = now.getHour();/////////////////////////
		tempmin = now.getMinute();/////////////////////////////
		tempsec = now.getSecond();/////////////////////////////
		temphr1 = Integer.parseInt(str[0]);///////////////////////////////
		tempmin1 = Integer.parseInt(str[1]);////////////////////////////
		tempsec1 = Integer.parseInt(str[2]);///////////////////////
		totalSec = ((temphr-temphr1)*60*60);/////////////////////////////////
		totalSec += (tempmin - tempmin1)*60;///////////////////////////////////
		totalSec = (Integer.parseInt(ChatClient.examforstudent.getTime()) * 60) - totalSec + (tempsec1 - tempsec);///////////////////////////////////////////
		this.timer = new Timer();

		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						// System.out.println("After 1 sec...");
						convertTime();
						if ((Integer.parseInt(examinprogress.getTime()) - (totalSec*60) == 10 )) {
							System.out.println("Time reminder");
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Time reminder!!");
							alert.setHeaderText(
									"You Have 10 minutes left to finish \n otherwise your grade will be 0 !!");
							alert.setContentText("Please submit the Exam in 10min");
							alert.show();
						}
						if (totalSec  == 600) {
							showAlert("Time Reminder","Time Reminder: 10 minutes left for the Exam", "Please make sure you submit in the next 10 minutes!");
						}
						if(totalSec <= 0) {
							try {
								submitexamtime();
								checkstatus();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}else {
							try {
								checkstatus();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}


				});
			}
		};

		timer.schedule(timerTask, 0, 1000);
	}
	private void submitexamtime() throws IOException {
	submitexam("No");
	Stage stage = (Stage) NextQuestionBtn_Pane_ExamInProgress.getScene().getWindow();
	// do what you have to do
	stage.close();
	FXMLLoader loader = new FXMLLoader();
	Stage primaryStage = new Stage();
	//((Node) event.getSource()).getScene().getWindow().hide();
	Pane root = loader.load(getClass().getResource("/gui/StudentHomePage.fxml").openStream());
	Scene scene = new Scene(root);
	primaryStage.setTitle("Student Home Page");
	primaryStage.setScene(scene);
	primaryStage.show();
	MessagesClass message = new MessagesClass(Messages.FinishedExam, examinprogress);
	ClientUI.chat.accept(message);
		
	}

	ArrayList<Question> questions = new ArrayList<Question>();

	@FXML
	private Label Topic_ExamInProgress;

	@FXML
	private Label Course_ExamInProgress;

	@FXML
	private Label Time_ExamInProgress;
	
    @FXML
    private Label ExamLock_ExamInProgress;

	@FXML
	private Text QuestionTxt_Pane_ExamInProgress;
	@FXML
	private Text NotesTxt_Pane_ExamInProgress;

	@FXML
	private RadioButton Option1_Pane_ExamInProgress;

	@FXML
	private RadioButton Option2_Pane_ExamInProgress;

	@FXML
	private RadioButton Option3_Pane_ExamInProgress;

	@FXML
	private RadioButton Option4_Pane_ExamInProgress;

	@FXML
	private ToggleGroup options;

	@FXML
	private Button SaveBtn_Pane_ExamInProgress;

	@FXML
	private Button NextQuestionBtn_Pane_ExamInProgress;

	@FXML
	private Button PreviousQuestionBtn_Pane_ExamInProgress;

	@FXML
	private Label QuestionNumberTopic_Pane_ExamInProgress;

	@FXML
	private Label Notes_Pane_ExamInProgress;

	@FXML
	private Label Q1Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q2Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q3Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q4Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q5Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q6Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q7Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q8Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q9Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q10Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q11Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q12Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q13Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q14Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q15Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q16Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q17Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q18Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q19Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q20Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q21Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q22Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q23Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q24Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q25Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q26Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q27Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q28Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q29Lbl_Pane_ExamInProgress;

	@FXML
	private Label Q30Lbl_Pane_ExamInProgress;

	@FXML
	private Button SubmitExam_ExamInProgress;

	@FXML
	private Button ExitExam_ExamInProgress;

    @FXML
    private Label ExamDone_ExamInProgress;
    
	@FXML
	void ClickQuestionLbl_Pane_ExamInProgress(MouseEvent event) {
			counternumberofquestion = Integer.parseInt(((Label) event.getSource()).getText()); // get the question you
																							   //want
			
			counternumberofquestion--; // the array list starts from 0 we need to decrease the value by 1
			initialize(location, resources);
	}

	@FXML
	void ExitExam_ExamInProgress(ActionEvent event) throws IOException {

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Exiting the Exam");
			alert.setHeaderText("If you Exit the Exam your Grade will be 0 ! \nall your answers will be deleted");
			alert.setContentText("Are you Sure you want to Exit ?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				for (int i = 0; i < answers.length; i++) // reset all answers
					answers[i] = 0;
				
				submitexam("Yes");
				Stage stage = (Stage) ExitExam_ExamInProgress.getScene().getWindow();
				// do what you have to do
				stage.close();
				FXMLLoader loader = new FXMLLoader();
				Stage primaryStage = new Stage();
				//((Node) event.getSource()).getScene().getWindow().hide();
				Pane root = loader.load(getClass().getResource("/gui/StudentHomePage.fxml").openStream());
				Scene scene = new Scene(root);
				primaryStage.setTitle("Start Exam Page");
				primaryStage.setScene(scene);
				primaryStage.show();
				ChatClient.examInProgressStudent.firstenterflag = false;
				MessagesClass message = new MessagesClass(Messages.FinishedExam, examinprogress);
				ClientUI.chat.accept(message);

			} else {

				// ... user chose CANCEL or closed the dialog
			}
			// get a handle to the stage

		
	}

	@FXML
	void NextQuestionBtn_Pane_ExamInProgress(ActionEvent event) {
			counternumberofquestion++;
			initialize(location, resources);


	}

	@FXML
	void PreviousQuestionBtn_Pane_ExamInProgress(ActionEvent event) {
			counternumberofquestion--;
			initialize(location, resources);
	}

	@FXML
	void SaveBtn_Pane_ExamInProgress(ActionEvent event) {
			RadioButton answer = (RadioButton) options.getSelectedToggle();
			if (answer != null) {
				String studentanswer = answer.getText();
				int studentanswernumber = getanswernumber(studentanswer);
				answers[counternumberofquestion] = studentanswernumber;
				colorflag = true;
				setQuestionsbuttons(counternumberofquestion);
			}
	}

	@FXML
	void SubmitExam_ExamInProgress(ActionEvent event) throws IOException {

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Submitting the Exam");
			alert.setHeaderText("Are you Sure you want to Exit ? \n You cannot change your answer after submitting.");
			alert.setContentText("To Submit the Exam Press Ok");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
					if(ChatClient.examforstudent.getStatus().equals("Done")) {
						submitexam("No");
					}else {
						submitexam("yes");
					}

				Stage stage = (Stage) ExitExam_ExamInProgress.getScene().getWindow();
				// do what you have to do
				stage.close();
				FXMLLoader loader = new FXMLLoader();
				Stage primaryStage = new Stage();
				((Node) event.getSource()).getScene().getWindow().hide();
				Pane root = loader.load(getClass().getResource("/gui/StudentHomePage.fxml").openStream());
				Scene scene = new Scene(root);
				primaryStage.setTitle("Student Home Page");
				primaryStage.setScene(scene);
				primaryStage.show();
				MessagesClass message = new MessagesClass(Messages.FinishedExam, examinprogress);
				ClientUI.chat.accept(message);
			} else {

				// ... user chose CANCEL or closed the dialog
			}

	}




	public long getCurrenttime() {
		return currenttime;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
			// TODO Auto-generated method stub
			this.location = location;
			this.resources = resources;
			javafx.application.Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if (ChatClient.examInProgressStudent != null && ChatClient.examInProgressStudent.firstenterflag) {// the first time we enter
						unlockExam();
						examisdone();
						examinprogress = ChatClient.examInProgressStudent;
						examinprogress.setStudentid(ChatClient.user.getID());
						numberofquestionsinexam = examinprogress.getNumofquests();
						answers = new int[numberofquestionsinexam];
						Topic_ExamInProgress.setText(examinprogress.getTopic());
						Course_ExamInProgress.setText(examinprogress.getCourse());
						if (null != ChatClient.examInProgressStudent.getNotesforstudent()) { // if there is any notes
																								// for
																								// student show them
							NotesTxt_Pane_ExamInProgress.setText(ChatClient.examInProgressStudent.getNotesforstudent());
						}else {
							NotesTxt_Pane_ExamInProgress.setText("");
						}
						timenow = LocalTime.now();
						ChatClient.examInProgressStudent.setTimestarted(String.valueOf(timenow.getHour())+":"+String.valueOf(timenow.getMinute()));
						visibleflag = true;
						setQuestionsbuttons(numberofquestionsinexam);
						setQuestionUI(counternumberofquestion);
						ChatClient.examInProgressStudent.firstenterflag = false;
						setTimer();


					}
					
					setQuestionUI(counternumberofquestion);
					radioButtonupdate();
				}


			});
	}

	private int getanswernumber(String studentanswer) {

			if (examinprogress.getQuestions().get(counternumberofquestion).getAnswer1().equals(studentanswer)) {
				return 1;
			} else if (examinprogress.getQuestions().get(counternumberofquestion).getAnswer2().equals(studentanswer)) {
				return 2;
			} else if (examinprogress.getQuestions().get(counternumberofquestion).getAnswer3().equals(studentanswer)) {
				return 3;
			} else
				return 4;
	}

	private void radioButtonupdate() {
			switch (answers[counternumberofquestion]) {
			case 0:
				RadioButton marked = (RadioButton) options.getSelectedToggle();
				if (marked != null) {
					marked.setSelected(false);
				}
				break;
			case 1:
				Option1_Pane_ExamInProgress.setSelected(true);
				break;
			case 2:
				Option2_Pane_ExamInProgress.setSelected(true);
				break;
			case 3:
				Option3_Pane_ExamInProgress.setSelected(true);
				break;
			case 4:
				Option4_Pane_ExamInProgress.setSelected(true);
				break;

			}
	}

	private void setQuestionUI(int numberofquestion) {

			Question tempquestion = examinprogress.getQuestions().get(numberofquestion);
			if (numberofquestion == 0) {
				QuestionNumberTopic_Pane_ExamInProgress.setText(String.valueOf(numberofquestion + 1));
				QuestionTxt_Pane_ExamInProgress.setText(tempquestion.getQuesttext());
				Option1_Pane_ExamInProgress.setText(tempquestion.getAnswer1());
				Option2_Pane_ExamInProgress.setText(tempquestion.getAnswer2());
				Option3_Pane_ExamInProgress.setText(tempquestion.getAnswer3());
				Option4_Pane_ExamInProgress.setText(tempquestion.getAnswer4());
				PreviousQuestionBtn_Pane_ExamInProgress.setVisible(false);
				NextQuestionBtn_Pane_ExamInProgress.setVisible(true);
				if (numberofquestion == numberofquestionsinexam - 1) {
					NextQuestionBtn_Pane_ExamInProgress.setVisible(false);
				}
			} else if (numberofquestion == 30) {
				QuestionNumberTopic_Pane_ExamInProgress.setText(String.valueOf(numberofquestion + 1));
				QuestionTxt_Pane_ExamInProgress.setText(tempquestion.getQuesttext());
				Option1_Pane_ExamInProgress.setText(tempquestion.getAnswer1());
				Option2_Pane_ExamInProgress.setText(tempquestion.getAnswer2());
				Option3_Pane_ExamInProgress.setText(tempquestion.getAnswer3());
				Option4_Pane_ExamInProgress.setText(tempquestion.getAnswer4());
				NextQuestionBtn_Pane_ExamInProgress.setVisible(false);
			} else {
				QuestionNumberTopic_Pane_ExamInProgress.setText(String.valueOf(numberofquestion + 1));
				QuestionTxt_Pane_ExamInProgress.setText(tempquestion.getQuesttext());
				Option1_Pane_ExamInProgress.setText(tempquestion.getAnswer1());
				Option2_Pane_ExamInProgress.setText(tempquestion.getAnswer2());
				Option3_Pane_ExamInProgress.setText(tempquestion.getAnswer3());
				Option4_Pane_ExamInProgress.setText(tempquestion.getAnswer4());
				PreviousQuestionBtn_Pane_ExamInProgress.setVisible(true);
				NextQuestionBtn_Pane_ExamInProgress.setVisible(true);
				if (numberofquestion == numberofquestionsinexam - 1) {
					NextQuestionBtn_Pane_ExamInProgress.setVisible(false);
				}
			}


	}

	private void setQuestionsbuttons(int numofquestions) {// remove all buttons of questions that don't exist in exam
			int i = numofquestions + 1;
			do {
				switch (i) { // if the visible flag removes all unwanted questions buttons and if the color
								// flag true it paint the label background with blue
				case 1:
					if (visibleflag)
						Q1Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q1Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 2:
					if (visibleflag)
						Q2Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q2Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 3:
					if (visibleflag)
						Q3Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q3Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 4:
					if (visibleflag)
						Q4Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q4Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 5:
					if (visibleflag)
						Q5Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q5Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 6:
					if (visibleflag)
						Q6Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q6Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 7:
					if (visibleflag)
						Q7Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q7Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 8:
					if (visibleflag)
						Q8Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q8Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 9:
					if (visibleflag)
						Q9Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q9Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 10:
					if (visibleflag)
						Q10Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q10Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 11:
					if (visibleflag)
						Q11Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q11Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 12:
					if (visibleflag)
						Q12Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q12Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 13:
					if (visibleflag)
						Q13Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q13Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 14:
					if (visibleflag)
						Q14Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q14Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 15:
					if (visibleflag)
						Q15Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q15Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 16:
					if (visibleflag)
						Q16Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q16Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 17:
					if (visibleflag)
						Q17Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q17Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 18:
					if (visibleflag)
						Q18Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q18Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 19:
					if (visibleflag)
						Q19Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q19Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 20:
					if (visibleflag)
						Q20Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q20Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 21:
					if (visibleflag)
						Q21Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q21Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 22:
					if (visibleflag)
						Q22Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q22Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 23:
					if (visibleflag)
						Q23Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q23Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 24:
					if (visibleflag)
						Q24Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q24Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 25:
					if (visibleflag)
						Q25Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q25Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 26:
					if (visibleflag)
						Q26Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q26Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 27:
					if (visibleflag)
						Q27Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q27Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 28:
					if (visibleflag)
						Q28Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q28Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 29:
					if (visibleflag)
						Q29Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q29Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				case 30:
					if (visibleflag)
						Q30Lbl_Pane_ExamInProgress.setVisible(false);
					if (colorflag) {
						Q30Lbl_Pane_ExamInProgress.setStyle("-fx-background-color: " + grey + ";");
						colorflag = false;
					}
					break;
				}

			} while (i++ <= 30);
			
			visibleflag = false;
	}

	public String getDuration() {
		return Duration;
	}

	public void setDuration(String duration) {
		Duration = duration;
	}

	public String getCurrentdate() {
		return currentdate;
	}

	public void setCurrentdate(String currentdate) {
		this.currentdate = currentdate;
	}
	
	public void lockExam() {

		lockexam = true;
		ExamLock_ExamInProgress.setVisible(lockexam);

	}
	public void unlockExam() {
		lockexam = false;
		ExamLock_ExamInProgress.setVisible(lockexam);
	}

	
	private void examisdone() {
		if(ChatClient.examInProgressStudent.firstenterflag) {
			ExamLock_ExamInProgress.setVisible(false);
			ExamDone_ExamInProgress.setVisible(false);
			return;
		}
		ExamDone_ExamInProgress.setVisible(true);
		ExamLock_ExamInProgress.setVisible(false);
		
	}
	private void checkstatus() throws IOException {
		//System.out.println(ChatClient.examInProgressStudent.getStatus());
		
		if(ChatClient.examInProgressStudent.getStatus().equals("Done")) {
			timer.cancel();
			examisdone();
		}
		if(ChatClient.examInProgressStudent.getStatus().equals("Locked")){
			lockExam();
		}
		if(ChatClient.examInProgressStudent.getStatus().equals("UnLocked")){
			unlockExam();
		}
		if(ChatClient.examInProgressStudent.getStatus().equals("Requested")) {
			settotalSec(ChatClient.examInProgressStudent.getTotalSecounds());
		}
		
	}
	public void showAlert(String message1,String message2,String message3) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(message1);
		alert.setHeaderText(message2);
		alert.setContentText(message3);
		alert.showAndWait();
	}
	private void submitexam(String submittedintime) {
		setDuration(String.valueOf(totalSec));
		timer.cancel();
		setCurrentdate(currentdate);
		ChatClient.examInProgressStudent.setAnswers(answers); // setting answers in examinporgressstudent class
		ChatClient.examInProgressStudent.setExamtype("Online");
		if(!ChatClient.examInProgressStudent.getStatus().equals("Locked")){
			ChatClient.examInProgressStudent.setSubmitintime(submittedintime);
		}else {
			ChatClient.examInProgressStudent.setSubmitintime("SubmittedWhileLocked");
		}
		
		timenow = LocalTime.now();
		ChatClient.examInProgressStudent.setTimefinished((String.valueOf(timenow.getHour())+":"+String.valueOf(timenow.getMinute())));
		ChatClient.examInProgressStudent.firstenterflag = false;
		
	}

}
