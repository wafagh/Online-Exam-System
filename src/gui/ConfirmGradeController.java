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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.AnswersReview;
import models.Messages;
import models.MessagesClass;
import models.StudentsResults;

public class ConfirmGradeController implements Initializable {

	@FXML
	private Button cancel_ConfirmGradeController;

	@FXML
	private Button save_ConfirmGradeController;

	@FXML
	Label StudentID_ConfirmGradeController;

	@FXML
	Label testgrade_ConfirmGradeController;

	@FXML
	private TextField newgrade_ConfirmGradeController;

	@FXML
	private TextArea comments_ConfirmGradeController;

	@FXML
	private RadioButton confirmgrade_ConfirmGradeController;

	@FXML
	private RadioButton changegrade_ConfirmGradeController;

	@FXML
	ToggleGroup group = new ToggleGroup();

	@FXML
	TableView<AnswersReview> answersTbl;

	@FXML
	private TableColumn<AnswersReview, String> questionid;

	@FXML
	private TableColumn<AnswersReview, String> studentanswer;

	@FXML
	private TableColumn<AnswersReview, String> correctanswer;

	ObservableList<AnswersReview> data;

	@FXML
	void cancel_ConfirmGradeController(ActionEvent event) throws Exception {

		System.out.println("back");
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();

		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/Students_Result.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Cancel");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void save_ConfirmGradeController(ActionEvent event) throws Exception {

		confirmgrade_ConfirmGradeController.setToggleGroup(group);
		confirmgrade_ConfirmGradeController.setUserData(1);
		changegrade_ConfirmGradeController.setToggleGroup(group);
		changegrade_ConfirmGradeController.setUserData(2);

		
		if (group.getSelectedToggle() == null
				|| (group.getSelectedToggle().getUserData().equals(1) && !newgrade_ConfirmGradeController.getText().equals("") )
				|| (group.getSelectedToggle().getUserData().equals(2) && newgrade_ConfirmGradeController.getText().equals(""))) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Error");
			alert.setContentText("Ooops, there's somthing wrong!");
			alert.showAndWait();
		} else {

			StudentsResults newresult;

			String grade = null;
			String StudentID = null;
			String firstname = null;
			String lastname = null;
			for (StudentsResults a : ChatClient.examresultlist) {
				if (a.getExamid().equals(ChatClient.studentexamconfirm.getExamid())) {
					grade = a.getGrade();
					StudentID = a.getStudentID();
					firstname = a.getFirstname();
					lastname = a.getLastname();
				}
			}

			if (group.getSelectedToggle().getUserData().equals(1)) {

				newresult = new StudentsResults(ChatClient.studentexamconfirm.getExamid(),
						ChatClient.studentexamconfirm.getCourse(), grade, comments_ConfirmGradeController.getText(),
						ChatClient.studentexamconfirm.getStatus(), StudentID, firstname, lastname);
			} else {
				newresult = new StudentsResults(ChatClient.studentexamconfirm.getExamid(),
						ChatClient.studentexamconfirm.getCourse(), newgrade_ConfirmGradeController.getText(),
						comments_ConfirmGradeController.getText(), ChatClient.studentexamconfirm.getStatus(), StudentID,
						firstname, lastname);
			}

			MessagesClass msg = new MessagesClass(Messages.UpdateStudentResult, newresult);
			ClientUI.chat.accept(msg);
			msg = new MessagesClass(Messages.GetExamResults, StudentsResultsController.exam);
			ClientUI.chat.accept(msg);

			System.out.println("save");
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.close();

			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/gui/Students_Result.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setTitle("Save/Confirm the new grade");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		javafx.application.Platform.runLater(new Runnable() {
			@Override
			public void run() {
				answersTbl.refresh();
				StudentID_ConfirmGradeController.setText(StudentsResultsController.student.getStudentID());
				for (StudentsResults a : ChatClient.examresultlist) {
					if (a.getExamid().equals(ChatClient.studentexamconfirm.getExamid())) {
						testgrade_ConfirmGradeController.setText(a.getGrade());
					}
				}

				questionid.setCellValueFactory(new PropertyValueFactory<AnswersReview, String>("question_serial"));
				studentanswer.setCellValueFactory(new PropertyValueFactory<AnswersReview, String>("studentAnswer"));
				correctanswer.setCellValueFactory(new PropertyValueFactory<AnswersReview, String>("correctAnswer"));

				data = FXCollections.observableArrayList(ChatClient.studentanswerslist);
				System.out.println(ChatClient.studentanswerslist.size()+"Test");
				answersTbl.setItems(data);
			}
		});

	}

}
