package gui;

import java.net.URL;
import java.util.ResourceBundle;

import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ReportByExamController implements Initializable {

	
	@FXML
	private Label Examid1;
	
	@FXML
	private Label Course;
	
	@FXML
	private Label Average;
	
	@FXML
	private Label Median;
	
	@FXML
	private Label Date;
	
	@FXML
	private Label Students;
	
	@FXML
	private Label Hgrade;
	
	@FXML
	private Label Lgrade;
	
	@FXML
	private Button cancel_ReportByExamform;
	
	@FXML
	void cancel_ReportByExamform(ActionEvent event) throws Exception {

		System.out.println("Canceled");
		Stage stage = (Stage) cancel_ReportByExamform.getScene().getWindow();
	
		stage.close();
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/gui/ExamsDoneReport.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Exams Done");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("getting into inizle");
		javafx.application.Platform.runLater( new Runnable() {
	           @Override
	           public void run() {
				if (ChatClient.ReportForExam != null) {
				System.out.println(ChatClient.ReportForExam.getExaminprogID());
					Examid1.setText(ChatClient.ReportForExam.getExaminprogID());
					Course.setText(ChatClient.ReportForExam.getCourse());
					Average.setText(String.valueOf(ChatClient.ReportForExam.getAverage()));
					Median.setText(String.valueOf(ChatClient.ReportForExam.getMedianX()));
					Date.setText(ChatClient.ReportForExam.getDate());
					Students.setText(String.valueOf(ChatClient.ReportForExam.getNumbOfStudents()));
					Hgrade.setText(String.valueOf(ChatClient.ReportForExam.getHighestGrade()));
					Lgrade.setText(String.valueOf(ChatClient.ReportForExam.getLowestGrade()));
					
					
				}
	           }
		});
		
	}


}
