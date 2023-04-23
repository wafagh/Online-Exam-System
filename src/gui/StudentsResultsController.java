package gui;

import java.net.URL;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.ExamsInProgress;
import models.Messages;
import models.MessagesClass;
import models.StudentsResults;

public class StudentsResultsController implements Initializable {
	URL location;
	ResourceBundle resources;
	@FXML
	private Button cancel_StudentsResultsController;

	@FXML
	private Button setfinalgrade_StudentsResultsController;

	@FXML
	Label ExamID_StudentsResultsController;

	@FXML
	Label Course_StudentsResultsController;

	@FXML
	TableView<StudentsResults> examTbl;

	@FXML
	private TableColumn<StudentsResults, String> studentid;

	@FXML
	private TableColumn<StudentsResults, String> studentfirstname;

	@FXML
	private TableColumn<StudentsResults, String> studentlastname;

	@FXML
	private TableColumn<StudentsResults, String> grade;

	@FXML
	private TableColumn<StudentsResults, String> status;

	@FXML
	private TableColumn<StudentsResults, String> teachercomments;

	ObservableList<StudentsResults> data;
	public static StudentsResults student = null;
	public static ExamsInProgress exam = null;

	@FXML
	void cancel_StudentsResultsController(ActionEvent event) throws Exception {

		System.out.println("back");
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();

		
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/ExamsDoneReport.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Exams Done");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void setfinalgrade_StudentsResultsController(ActionEvent event) throws Exception {

		ObservableList<StudentsResults> SingleExam;
		SingleExam = examTbl.getSelectionModel().getSelectedItems();

		if (SingleExam.size() == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Error");
			alert.setContentText("Ooops, You must choose an exam!");
			alert.showAndWait();
		} else {
			System.out.println(ChatClient.user.getFirstName());
			student = SingleExam.get(0);

			for (ExamsInProgress a : ChatClient.examslist) {

				if (a.getExamid().equals(SingleExam.get(0).getExamid())&& SingleExam.get(0).getExaminprogressid().equals(a.getId())) {
					System.out.println(a.getExamid());
					System.out.println(SingleExam.get(0).getExamid());
					exam = new ExamsInProgress(a.getId(), a.getTopic(), a.getCourse(), a.getTime(), a.getReduration(),
							a.getStatus(), a.getCode());
					exam.setExamid(a.getExamid());
					System.out.println(exam.getExamid());
				}
			}
			MessagesClass msg = new MessagesClass(Messages.GetStudentResult, exam);
			ClientUI.chat.accept(msg);

			msg = new MessagesClass(Messages.GetStudentanswers, exam);
			ClientUI.chat.accept(msg);

			System.out.println("set final grade");
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.close();

			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/gui/ConfirmGrade.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setTitle("Confirm Grade");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.location = location;
		this.resources = resources;
		javafx.application.Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (!ChatClient.examresultlist.isEmpty()) {
					ExamID_StudentsResultsController.setText(ChatClient.examresultlist.get(0).getExamid());
					Course_StudentsResultsController.setText(ChatClient.examresultlist.get(0).getExamcourse());
				}
				studentid.setCellValueFactory(new PropertyValueFactory<StudentsResults, String>("studentID"));
				studentfirstname.setCellValueFactory(new PropertyValueFactory<StudentsResults, String>("firstname"));
				studentlastname.setCellValueFactory(new PropertyValueFactory<StudentsResults, String>("lastname"));
				grade.setCellValueFactory(new PropertyValueFactory<StudentsResults, String>("grade"));
				status.setCellValueFactory(new PropertyValueFactory<StudentsResults, String>("status"));
				teachercomments.setCellValueFactory(new PropertyValueFactory<StudentsResults, String>("comments"));

				data = FXCollections.observableArrayList(ChatClient.examresultlist);
				examTbl.setItems(data);
			}
		});
	}

}
