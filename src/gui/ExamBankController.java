package gui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import client.ChatClient;
import client.ClientUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Exam;
import models.ExamsInProgress;
import models.Messages;
import models.MessagesClass;
import models.Question;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ExamBankController implements Initializable {
	private URL location;
	private ResourceBundle resources;
	@FXML
	private Button search_ExamBankController;

	@FXML
	private Button editexam_ExamBankController;

	@FXML
	private Button deleteexam_ExamBankController;

	@FXML
	private Button addexam_ExamBankController;

	@FXML
	private Button cancel_ExamBankController;

	@FXML
	private Button Start_ExamBankController;

	@FXML
	private TextField searchtext_ExamBankController;

	@FXML
	TableView<Exam> examTbl;

	@FXML
	private TableColumn<Exam, String> examid;

	@FXML
	private TableColumn<Exam, String> numofquests;

	@FXML
	private TableColumn<Exam, String> topic;

	@FXML
	private TableColumn<Exam, String> course;

	@FXML
	private TableColumn<Exam, String> time;

	ObservableList<Exam> data;

	@FXML
	void search_ExamBankController(ActionEvent event) throws Exception {

		String searchtext = searchtext_ExamBankController.getText();
		MessagesClass msg = new MessagesClass(Messages.SearchExam, searchtext);
		ClientUI.chat.accept(msg);
		data = FXCollections.observableArrayList(ChatClient.examsbank);
		examTbl.setItems(data);
	}

	@FXML
	void cancel_ExamBankController(ActionEvent event) throws Exception {

		System.out.println("back");
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();

		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/LectureHomePage.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Lecture Home Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void editexam_ExamBankController(ActionEvent event) throws Exception {

		ObservableList<Exam> SingleExam;
		SingleExam = examTbl.getSelectionModel().getSelectedItems();

		if (SingleExam.size() == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Error");
			alert.setContentText("Ooops, You must choose an exam!");
			alert.showAndWait();
		} else {
			MessagesClass msg = new MessagesClass(Messages.GetEditExamData, SingleExam.get(0));
			ClientUI.chat.accept(msg);

			msg = new MessagesClass(Messages.GetEditExamQuestions, SingleExam.get(0).getExamid());
			ClientUI.chat.accept(msg);

			System.out.println("edit exam");
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.close();
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/gui/EditExam.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setTitle("Edit Exam");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	@FXML
	void deleteexam_ExamBankController(ActionEvent event) throws Exception {

		ObservableList<Exam> AllExams, SingleExam;
		AllExams = examTbl.getItems();
		SingleExam = examTbl.getSelectionModel().getSelectedItems();
		if (SingleExam.size() == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Error");
			alert.setContentText("Ooops, You must choose an exam!");
			alert.showAndWait();
		} else {
			MessagesClass msg = new MessagesClass(Messages.OpenedExamInProgressForm, ChatClient.user);
			ClientUI.chat.accept(msg);
			Boolean flag = false;
			if (ChatClient.examslist != null) {
				for (ExamsInProgress a : ChatClient.examslist) {
					if (a.getId().equals(SingleExam.get(0).getExamid()))
						flag = true;
				}
			}
			if (flag == true) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText("Error");
				alert.setContentText("Ooops, This exam is in progress!");
				alert.showAndWait();
			} else {

				if (AllExams.size() > 1) {
					msg = new MessagesClass(Messages.RemoveExam, SingleExam.get(0));
					ClientUI.chat.accept(msg);

					msg = new MessagesClass(Messages.RemoveExamQuestions, SingleExam.get(0).getExamid());
					ClientUI.chat.accept(msg);

					msg = new MessagesClass(Messages.OpenExamBank, null);
					ClientUI.chat.accept(msg);
					initialize(location, resources);
					SingleExam.forEach(AllExams::remove);
				} else {
					msg = new MessagesClass(Messages.RemoveExam, SingleExam.get(0));
					ClientUI.chat.accept(msg);

					msg = new MessagesClass(Messages.RemoveExamQuestions, SingleExam.get(0).getExamid());
					ClientUI.chat.accept(msg);

					msg = new MessagesClass(Messages.OpenExamBank, null);
					ClientUI.chat.accept(msg);

					data = FXCollections.observableArrayList(ChatClient.examsbank);
					examTbl.setItems(data);
				}
			}
		}

	}

	@FXML
	void addexam_ExamBankController(ActionEvent event) throws Exception {

		examTbl.getSelectionModel().clearSelection();
		System.out.println("add exam");
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/AddExam.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Add a New Exam");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void Start_ExamBankController(ActionEvent event) throws Exception {
		MessagesClass msg;
		int index;
		Exam exam = examTbl.getSelectionModel().getSelectedItem();
		System.out.println("fef");
		if (exam == null) {
			Alert a = new Alert(AlertType.NONE);
			// set alert type
			a.setAlertType(AlertType.ERROR);
			// set content text
			a.setContentText("Please Chose An Exam");
			// show the dialog
			a.showAndWait();
		} else {
			exam.setTempLecutrer(ChatClient.user.getUserName());
			System.out.println("Adding new exam in progress");
			msg = new MessagesClass(Messages.AddExamInProgress, exam);
			ClientUI.chat.accept(msg);

			Alert a = new Alert(AlertType.NONE);
			// set alert type
			a.setAlertType(AlertType.INFORMATION);
			// set content text
			a.setContentText("Your Code Is : " + ChatClient.code);
			// show the dialog
			a.showAndWait();

		}

		msg = new MessagesClass(Messages.OpenedExamInProgressForm, ChatClient.user);
		ClientUI.chat.accept(msg);
		System.out.println(ChatClient.code);
		for (ExamsInProgress a : ChatClient.examslist) {
			System.out.println(a.getCode());
			if (a.getCode() == ChatClient.code) {
				System.out.println(a.toString());
				a.setTimer();
			}
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.location = location;
		this.resources = resources;
		javafx.application.Platform.runLater(new Runnable() {
			@Override
			public void run() {
				examid.setCellValueFactory(new PropertyValueFactory<Exam, String>("examnumber"));
				course.setCellValueFactory(new PropertyValueFactory<Exam, String>("course"));
				topic.setCellValueFactory(new PropertyValueFactory<Exam, String>("topic"));
				time.setCellValueFactory(new PropertyValueFactory<Exam, String>("time"));
				numofquests.setCellValueFactory(new PropertyValueFactory<Exam, String>("numofquests"));

				data = FXCollections.observableArrayList(ChatClient.examsbank);
				examTbl.setItems(data);
			}
		});
	}

}
