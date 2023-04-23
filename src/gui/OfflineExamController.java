package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.ExamInProgressStudent;
import models.Messages;
import models.MessagesClass;
import common.MyFile;

public class OfflineExamController implements Initializable {

	private ExamInProgressStudent examinprogress;
	private long min, sec, hr, totalSec = 0; //
	LocalTime timenow;
	private Timer timer;
	private boolean lockexam = false;
	private String Duration;
	private String currentdate;

	public void convertTime() {

		min = TimeUnit.SECONDS.toMinutes(totalSec);
		sec = totalSec - (min * 60);
		hr = TimeUnit.MINUTES.toHours(min);
		min = min - (hr * 60);
		Time_offlineexam.setText(format(hr) + ":" + format(min) + ":" + format(sec));
		totalSec--;

	}

	private String format(long value) {

		if (value < 10) {
			return 0 + "" + value;
		}

		return value + "";
	}

	private void setTimer() {
		int temphr, temphr1, tempmin, tempmin1, tempsec, tempsec1;
		LocalTime now = LocalTime.now();
		String[] str;
		str = (ChatClient.examforstudent.getStarttime().split(":"));
		temphr = now.getHour();
		tempmin = now.getMinute();
		tempsec = now.getSecond();
		temphr1 = Integer.parseInt(str[0]);
		tempmin1 = Integer.parseInt(str[1]);
		tempsec1 = Integer.parseInt(str[2]);
		totalSec = ((temphr - temphr1) * 60 * 60);
		totalSec += (tempmin - tempmin1) * 60;
		totalSec = (Integer.parseInt(ChatClient.examforstudent.getTime()) * 60) - totalSec + (tempsec1 - tempsec);
		this.timer = new Timer();

		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						// System.out.println("After 1 sec...");
						convertTime();
						if ((Integer.parseInt(examinprogress.getTime()) - (totalSec * 60) == 10)) {
							System.out.println("Time reminder");
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Time reminder!!");
							alert.setHeaderText(
									"You Have 10 minutes left to finish \n otherwise your grade will be 0 !!");
							alert.setContentText("Please submit the Exam in 10min");
							alert.show();
						}
						if (totalSec == 600) {
							showAlert("Time Reminder", "Time Reminder: 10 minutes left for the Exam",
									"Please make sure you submit in the next 10 minutes!");
						}
						if (totalSec <= 0) {
							try {
								submitexamtime();
								checkstatus();

							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} else {
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

	@FXML
	private Label Course_offlineexam;

	@FXML
	private Label Topic_offlineexam;

	@FXML
	private Button Uploadexambtn_offlineexam;

	@FXML
	private Button Downloadexambtn_offlineexam;

	@FXML
	private Label Time_offlineexam;

	@FXML
	private Button Exitbtn_offlineexam;

	@FXML
	private Label Starttime_offlineexam;

	@FXML
	private Button Submitbtn_offlineexam;

	@FXML
	private Label ExamLocked_OfflineExam;

	@FXML
	void Exitbtn_offlineexam(ActionEvent event) throws IOException {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Exiting the Exam");
		alert.setHeaderText(
				"If you Exit the Exam your Grade will be 0 ! \nYour file wont be Uploaded\nIf you want to submit your exam please click in Submit.");
		alert.setContentText("Are you Sure you want to Exit ?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			System.out.println("Back to Home page..");
			Stage stage = (Stage) Exitbtn_offlineexam.getScene().getWindow();
			// do what you have to do
			stage.close();
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			// ((Node) event.getSource()).getScene().getWindow().hide();
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

	FileChooser fileChooser = new FileChooser();

	@FXML
	void Uploadexambtn_offlineexam(ActionEvent event) {

		Stage primaryStage = new Stage();
		File selectedFile = fileChooser.showOpenDialog(primaryStage);
		if (null != selectedFile) {
			ChatClient.examInProgressStudent.setAnswersfile(selectedFile.toString());
		}
		System.out.println(selectedFile);

	}

	@FXML
	void Downloadexambtn_offlineexam(ActionEvent event) {
		// Downloadexambtn_offlineexam.setStyle("-fx-background-color: #b8bee0;");

		MessagesClass message = new MessagesClass(Messages.readFormFile, null);// create message to server to read exam
																				// file
		ClientUI.chat.accept(message);
		System.out.println("File had saved on you computer!");
		Downloadexambtn_offlineexam.setStyle("-fx-background-color: #b8bee0;");

		// start timer to calculated how much he/she taken to finishing the exam
	}

	@FXML
	void Submitbtn_offlineexam(ActionEvent event) throws IOException {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Submitting the Exam");
		alert.setHeaderText("Are you Sure you want to Exit ? \n You cannot change your answer after submitting.");
		alert.setContentText("To Submit the Exam Press Ok");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			if (ChatClient.examforstudent.getStatus().equals("Done")) {
				submitexam("No");
			} else {
				submitexam("yes");
			}

			Stage stage = (Stage) Submitbtn_offlineexam.getScene().getWindow();
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
			MessagesClass message = new MessagesClass(Messages.FinishedExamOffline, examinprogress);
			ClientUI.chat.accept(message);
		} else {

			// ... user chose CANCEL or closed the dialog
		}
	}

	private void submitexamtime() throws IOException {
		submitexam("No");
		Stage stage = (Stage) Submitbtn_offlineexam.getScene().getWindow();
		// do what you have to do
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		// ((Node) event.getSource()).getScene().getWindow().hide();
		Pane root = loader.load(getClass().getResource("/gui/StudentHomePage.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Student Home Page");
		primaryStage.setScene(scene);
		primaryStage.show();
		MessagesClass message = new MessagesClass(Messages.FinishedExamOffline, examinprogress);
		ClientUI.chat.accept(message);

	}

	private void submitexam(String submittedintime) {
		setDuration(String.valueOf(totalSec));
		timer.cancel();
		setCurrentdate(currentdate);
		ChatClient.examInProgressStudent.setExamtype("Offline"); // set offline exam
		if (!ChatClient.examInProgressStudent.getStatus().equals("Locked")) {
			ChatClient.examInProgressStudent.setSubmitintime(submittedintime);
		} else {
			ChatClient.examInProgressStudent.setSubmitintime("SubmittedWhileLocked");
		}

		timenow = LocalTime.now();
		ChatClient.examInProgressStudent
				.setTimefinished((String.valueOf(timenow.getHour()) + ":" + String.valueOf(timenow.getMinute())));
		ChatClient.examInProgressStudent.firstenterflag = false;

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		javafx.application.Platform.runLater(new Runnable() {
			@Override
			public void run() {

				if (ChatClient.examInProgressStudent != null && ChatClient.examInProgressStudent.firstenterflag) {// the
																													// first
																													// time
																													// we
																													// enter
					unlockExam();
					examinprogress = ChatClient.examInProgressStudent;
					examinprogress.setStudentid(ChatClient.user.getID());
					Course_offlineexam.setText(ChatClient.examforstudent.getCourse());
					Topic_offlineexam.setText(ChatClient.examforstudent.getTopic());
					timenow = LocalTime.now();
					ChatClient.examInProgressStudent.setTimestarted(
							String.valueOf(timenow.getHour()) + ":" + String.valueOf(timenow.getMinute()));
					ChatClient.examInProgressStudent.firstenterflag = false;
					setTimer();

				}
			}
		});

	}

	public void lockExam() {
		lockexam = true;
		ExamLocked_OfflineExam.setVisible(lockexam);

	}

	public void unlockExam() {
		lockexam = false;
		ExamLocked_OfflineExam.setVisible(lockexam);
	}

	public void showAlert(String message1, String message2, String message3) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(message1);
		alert.setHeaderText(message2);
		alert.setContentText(message3);
		alert.showAndWait();
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

	private void checkstatus() throws IOException {
		// System.out.println(ChatClient.examInProgressStudent.getStatus());

		if (ChatClient.examInProgressStudent.getStatus().equals("Done")) {
			timer.cancel();
			examisdone();
		}
		if (ChatClient.examInProgressStudent.getStatus().equals("Locked")) {
			lockExam();
		}
		if (ChatClient.examInProgressStudent.getStatus().equals("UnLocked")) {
			unlockExam();
		}

	}

	private void examisdone() {
		ExamLocked_OfflineExam.setVisible(false);

	}
}
