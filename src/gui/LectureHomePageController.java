package gui;

import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Messages;
import models.MessagesClass;
public class LectureHomePageController  {

	@FXML
	private Button changetime_lecturerForm;
	
	
	
	@FXML
	private Button questionbank_lecturerForm;
	
	@FXML
	private Button exambank_lecturerForm;

	@FXML
	private Button logout_lecturerForm;
	
	@FXML
	private Button examreport_lecturerForm;
	
	 @FXML
	    void logout_lecturerForm(ActionEvent event) throws Exception{
		 //MessagesClass msg = new MessagesClass(Messages.loggedout,null);
	 	 //ClientUI.chat.accept(msg);
	
		 ClientUI.chat.accept(new MessagesClass(Messages.updateStatusOfLogIn, ChatClient.user));
		 System.out.println("Logged Out");
		 FXMLLoader loader = new FXMLLoader();
 		 Stage primaryStage = new Stage();
 		 ((Node)event.getSource()).getScene().getWindow().hide();// do this 3 lines for all the roles no matter what
 		 Pane root = loader.load(getClass().getResource("/gui/LoginForm.fxml").openStream());
		 Scene scene = new Scene(root);			
		 primaryStage.setTitle("Login");
		 primaryStage.setScene(scene);		
		 primaryStage.show();
		 
	    }
	 
	 @FXML
	 void changetime_lecturerForm(ActionEvent event) throws Exception {
		 
		 MessagesClass msg = new MessagesClass(Messages.OpenedExamInProgressForm,ChatClient.user);
		 	 ClientUI.chat.accept(msg);
		
			 System.out.println("Opened The Exams In Progress List");
			 FXMLLoader loader = new FXMLLoader();
	 		 Stage primaryStage = new Stage();
	 		 ((Node)event.getSource()).getScene().getWindow().hide();// do this 3 lines for all the roles no matter what
	 		 Pane root = loader.load(getClass().getResource("/gui/ExamsInProgressTime.fxml").openStream());
			 Scene scene = new Scene(root);			
			 primaryStage.setTitle("Exams In Progress");
			 primaryStage.setScene(scene);		
			 primaryStage.show();
			 
			 
	 }
	 
	 @FXML
	 void questionbank_lecturerForm(ActionEvent event) throws Exception {
		 MessagesClass msg = new MessagesClass(Messages.OpenQuestionBank,null);
		 ClientUI.chat.accept(msg);
		 System.out.println("Opened The Question Bank ");
		 FXMLLoader loader = new FXMLLoader();
 		 Stage primaryStage = new Stage();
 		 ((Node)event.getSource()).getScene().getWindow().hide();// do this 3 lines for all the roles no matter what
 		 Pane root = loader.load(getClass().getResource("/gui/QuestionBank.fxml").openStream());
		 Scene scene = new Scene(root);			
		 primaryStage.setTitle("Question Bank");
		 primaryStage.setScene(scene);		
		 primaryStage.show();
		 
	 }
	 
	 @FXML
	 void exambank_lecturerForm(ActionEvent event) throws Exception {
		 MessagesClass msg = new MessagesClass(Messages.OpenExamBank,null);
		 ClientUI.chat.accept(msg);
		 System.out.println("Opened The Exam Bank ");
		 FXMLLoader loader = new FXMLLoader();
 		 Stage primaryStage = new Stage();
 		 ((Node)event.getSource()).getScene().getWindow().hide();// do this 3 lines for all the roles no matter what
 		 Pane root = loader.load(getClass().getResource("/gui/ExamBank.fxml").openStream());
		 Scene scene = new Scene(root);			
		 primaryStage.setTitle("Exams Bank");
		 primaryStage.setScene(scene);		
		 primaryStage.show();
		 
	 }
	 
	 @FXML
	 void examreport_lecturerForm(ActionEvent event) throws Exception {   //
		 MessagesClass msg = new MessagesClass(Messages.OpenExamreportlecturer,ChatClient.user);///////////////////////
		 ClientUI.chat.accept(msg);
		 System.out.println("Opened The Exam Done List");
		 FXMLLoader loader = new FXMLLoader();
 		 Stage primaryStage = new Stage();
 		 ((Node)event.getSource()).getScene().getWindow().hide();// do this 3 lines for all the roles no matter what
 		 Pane root = loader.load(getClass().getResource("/gui/ExamsDoneReport.fxml").openStream());
		 Scene scene = new Scene(root);			
		 primaryStage.setTitle("Exams Done");
		 primaryStage.setScene(scene);		
		 primaryStage.show();
		 
	 }
	
	 
}
