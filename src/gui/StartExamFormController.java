package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

import com.mysql.cj.protocol.Message;

import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Messages;
import models.MessagesClass;

public class StartExamFormController implements Initializable {
	private Boolean VerifyExam = false;
	private Boolean ExamLocked = false;
	private String ExamCode;
	URL location;
	ResourceBundle resources;
	LocalDate localDate;
	String strlocaldate;
	LocalTime localTime;
	
	
	@FXML
	private Button OnlineExamBtn_Start;

	@FXML
	private Button OfflineExamBtn_StartExam;

	@FXML
	private TextField ExamCodeTxt_StartExam;

	@FXML
	private Button CancelBtn_StartExam;

	@FXML
	private Button CheckCodeBtn_StartExam;

	@FXML
	private Label Course_StartExam;

	@FXML
	private Label Code_StartExam;

	@FXML
	private Label Duration_StartExam;

	@FXML
	private Label StartTime_StartExam;

	@FXML
	private Label Date_StartExam;
	
    @FXML
    private Label Status_StartExam;

	@FXML
	void CancelBtn_StartExam(ActionEvent event) throws IOException {
		// get a handle to the stage
		Stage stage = (Stage) CancelBtn_StartExam.getScene().getWindow();
		// do what you have to do
		stage.close();

		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/StudentHomePage.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Student Home Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void CheckCodeBtn_StartExam(ActionEvent event) {
		setExamCode(ExamCodeTxt_StartExam.getText());
		if (getExamCode().length() != 4) {
			WrongExamCode();
		} else {
			MessagesClass message = new MessagesClass(Messages.StudentCheckExamCode, getExamCode());
			ClientUI.chat.accept(message);
			if (ChatClient.examforstudent != null) {
				message = new MessagesClass(Messages.getExamQuestionsbycode, getExamCode(), ChatClient.user.getID());
				ClientUI.chat.accept(message);
				if (ChatClient.examInProgressStudent != null) {
					setVerifyExam(true);
					if (ChatClient.examInProgressStudent.getStatus().equals("Locked")
							|| ChatClient.examInProgressStudent.getStatus().equals("Done")) {
						setExamLocked(true);
					}else {
						setExamLocked(false);
					}
					initialize(location, resources);
				} else {

				}
			} else {
				WrongExamCode();
			}
		}
	}

	private void WrongExamCode() {
		System.out.println("Wrong Exam Code!");
		setVerifyExam(false);
		initialize(location, resources);

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Warrning");
		alert.setHeaderText("Wrong or missing Exam Code");
		alert.setContentText("Please ReEtner ExamCode");

		alert.showAndWait();

	}

	@FXML
	void OfflineExamBtn_StartExam(ActionEvent event) throws IOException {
		MessagesClass message = new MessagesClass(Messages.getExamQuestionsbycode, getExamCode(), ChatClient.user.getID());
		ClientUI.chat.accept(message);
		if (ChatClient.examInProgressStudent != null) {
			setVerifyExam(true);
			if (ChatClient.examInProgressStudent.getStatus().equals("Locked")
					|| ChatClient.examInProgressStudent.getStatus().equals("Done")) {
				setExamLocked(true);
			}else{
				setExamLocked(false);
			}
		}
		if (VerifyExam) {
			if (!ExamLocked) {

				TextInputDialog dialog = new TextInputDialog("Enter ID to Start Exam");
				dialog.setTitle("ID validation");
				dialog.setHeaderText("Please Enter Your ID to Start the Exam");
				dialog.setContentText("Student ID :");

				// Traditional way to get the response value.
				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()) {
					if (result.get().equals(ChatClient.user.getID())) {
						if (LocalDate.parse(ChatClient.examforstudent.getDate()).isEqual(localDate)) {
							localTime = LocalTime.now();
							if(LocalTime.parse(ChatClient.examforstudent.getStarttime()).isBefore(localTime)) {
							if (ChatClient.examInProgressStudent.firstenterflag) {
								FXMLLoader loader = new FXMLLoader();
								Stage primaryStage = new Stage();
								((Node) event.getSource()).getScene().getWindow().hide();
								Pane root = loader.load(getClass().getResource("/gui/OfflineExamstudent.fxml").openStream());
								Scene scene = new Scene(root);
								primaryStage.setTitle("Exam in Progress");
								primaryStage.setScene(scene);
								primaryStage.show();
							} else {
								showAlert("Exam Access Denied","You already entered the Exam","Please contact the Lecturer");
							}
							}else {
								showAlert("Wrong Time Exam","You can't access the Exam Check the time Exam Starts","Please contact the Lecturer");
							}
						}else {
							showAlert("Wrong Date Exam","You can't access the Exam Check the Date!","Please contact the Lecturer");
						}
					} else {
						dialog.close();
						showAlert("Wrong ID","Wrong ID number","The ID you entered do not match the Student ID");
					}
				}

			} else {
				showAlert("Exam Access Denied","The Exam is Locked!","Please contact the Lecturer");
			}
		} else {
			showAlert("Warrning","Wrong or missing Exam Code","Please ReEtner ExamCode");
		}

	}

	@FXML
	void OnlineExamBtn_Start(ActionEvent event) throws IOException {
		MessagesClass message = new MessagesClass(Messages.getExamQuestionsbycode, getExamCode(), ChatClient.user.getID());
		ClientUI.chat.accept(message);
		if (ChatClient.examInProgressStudent != null) {
			setVerifyExam(true);
			if (ChatClient.examInProgressStudent.getStatus().equals("Locked")
					|| ChatClient.examInProgressStudent.getStatus().equals("Done")) {
				setExamLocked(true);
			}else{
				setExamLocked(false);
			}
		}
		if (VerifyExam) {
			if (!ExamLocked) {

				TextInputDialog dialog = new TextInputDialog("Enter ID to Start Exam");
				dialog.setTitle("ID validation");
				dialog.setHeaderText("Please Enter Your ID to Start the Exam");
				dialog.setContentText("Student ID :");

				// Traditional way to get the response value.
				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()) {
					if (result.get().equals(ChatClient.user.getID())) {
						if (LocalDate.parse(ChatClient.examforstudent.getDate()).isEqual(localDate)) {
							localTime = LocalTime.now();
							if(LocalTime.parse(ChatClient.examforstudent.getStarttime()).isBefore(localTime)) {
							if (ChatClient.examInProgressStudent.firstenterflag) {
								FXMLLoader loader = new FXMLLoader();
								Stage primaryStage = new Stage();
								((Node) event.getSource()).getScene().getWindow().hide();
								Pane root = loader
										.load(getClass().getResource("/gui/ExamInProgress.fxml").openStream());
								Scene scene = new Scene(root);
								primaryStage.setTitle("Exam in Progress");
								primaryStage.setScene(scene);
								primaryStage.show();
							} else {
								showAlert("Exam Access Denied","You already entered the Exam","Please contact the Lecturer");
							}
							}else {
								showAlert("Wrong Time Exam","You can't access the Exam Check the time Exam Starts","Please contact the Lecturer");
							}
						}else {
							showAlert("Wrong Date Exam","You can't access the Exam Check the Date!","Please contact the Lecturer");
						}
					} else {
						dialog.close();
						showAlert("Wrong ID","Wrong ID number","The ID you entered do not match the Student ID");
					}
				}

			} else {
				showAlert("Exam Access Denied","The Exam is Locked!","Please contact the Lecturer");
			}
		} else {
			showAlert("Warrning","Wrong or missing Exam Code","Please ReEtner ExamCode");
		}

	}

	public Boolean getVerifyExam() {
		return VerifyExam;
	}

	public void setVerifyExam(Boolean verifyExam) {
		VerifyExam = verifyExam;
	}

	public String getExamCode() {
		return ExamCode;
	}

	public void setExamCode(String examCode) {
		ExamCode = examCode;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.location = location;
		this.resources = resources;
		javafx.application.Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (VerifyExam) {

					Course_StartExam.setText(ChatClient.examforstudent.getCourse());
					Code_StartExam.setText(ExamCode);
					Duration_StartExam.setText(ChatClient.examforstudent.getTime() + " Minutes");
					Date_StartExam.setText(ChatClient.examforstudent.getDate());
					StartTime_StartExam.setText(ChatClient.examforstudent.getStarttime());
					Status_StartExam.setText(ChatClient.examforstudent.getStatus());
					localDate = LocalDate.now();

				} else {
					Course_StartExam.setText("");
					Code_StartExam.setText("");
					Duration_StartExam.setText("");
					Date_StartExam.setText("");
					StartTime_StartExam.setText("");
					Status_StartExam.setText("");
				}
			}
		});

	}

	public Boolean getExamLocked() {
		return ExamLocked;
	}

	public void setExamLocked(Boolean examLocked) {
		ExamLocked = examLocked;
	}
	public void showAlert(String message1,String message2,String message3) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(message1);
		alert.setHeaderText(message2);
		alert.setContentText(message3);
		alert.showAndWait();
	}
}
