package gui;

import java.net.URL;
import java.util.ResourceBundle;

import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ExamCommentsController implements Initializable {

	@FXML
	private TextField commentsforteacher_ExamCommentsController;

	@FXML
	private TextField commentsforstudent_ExamCommentsController;

	@FXML
	private Button confirm_ExamCommentsController;

	@FXML
	private Button cancel_ExamCommentsController;

	@FXML
	void cancel_ExamCommentsController(ActionEvent event) throws Exception {

		if (ChatClient.examtoedit == null) {
			System.out.println("back");
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.close();
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/gui/AddExam.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setTitle("Add Exam Page");
			primaryStage.setScene(scene);
			primaryStage.show();
		} else {
			System.out.println("back");
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.close();
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/gui/EditExam.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setTitle("Edit Exam Page");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	@FXML
	void confirm_ExamCommentsController(ActionEvent event) throws Exception {

		if (ChatClient.examtoedit == null) {
			AddExamController.commentsforteacher = commentsforteacher_ExamCommentsController.getText();
			AddExamController.commentsforstudent = commentsforstudent_ExamCommentsController.getText();
			System.out.println("back");
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.close();
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/gui/AddExam.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setTitle("Add Exam Page");
			primaryStage.setScene(scene);
			primaryStage.show();
		} else {
			EditExamController.commentsforteacher = commentsforteacher_ExamCommentsController.getText();
			EditExamController.commentsforstudent = commentsforstudent_ExamCommentsController.getText();
			System.out.println("back");
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.close();
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/gui/EditExam.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setTitle("Edit Exam Page");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		javafx.application.Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (ChatClient.examtoedit != null) {
					if (EditExamController.commentsforteacher != null || EditExamController.commentsforstudent != null) {
						commentsforteacher_ExamCommentsController.setText(EditExamController.commentsforteacher);
						commentsforstudent_ExamCommentsController.setText(EditExamController.commentsforstudent);
					} else {
						commentsforteacher_ExamCommentsController.setText(ChatClient.examtoedit.getCommentforteacher());
						commentsforstudent_ExamCommentsController.setText(ChatClient.examtoedit.getCommentforstudent());
					}
				} else {
					commentsforteacher_ExamCommentsController.setText(AddExamController.commentsforteacher);
					commentsforstudent_ExamCommentsController.setText(AddExamController.commentsforstudent);
				}
			}
		});
	}
}
