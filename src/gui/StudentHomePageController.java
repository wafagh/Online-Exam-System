package gui;

import java.io.IOException;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Messages;
import models.MessagesClass;

public class StudentHomePageController implements Initializable{

    @FXML
    private Button ExamsGradebtn_Student;

    @FXML
    private Button StartExamBtn_Student;

    @FXML
    Label StudentFirstNameLbl_StudentHome;
    
    @FXML
    Label StudentLastNameLbl_StudentHome;

    @FXML
    Label StudentIdLbl_StudentHome;

    @FXML
    Label StudentFacultyLbl_StudentHome;


    @FXML
    private Button LogOutBtn_StudentHome;

    @FXML
    void ExamsGradebtn_Student(ActionEvent event) throws IOException {
    	MessagesClass message=new MessagesClass(Messages.OpenGradesList,null);
    	ClientUI.chat.accept(message);
    	FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		((Node)event.getSource()).getScene().getWindow().hide();
		Pane root = loader.load(getClass().getResource("/gui/Gradeslist.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Start Exam Page");
		primaryStage.setScene(scene);
		primaryStage.show();
		System.out.println("move on Grades List page~>");
    }

    @FXML
    void LogOutBtn_StudentHome(ActionEvent event) throws Exception {
    	// get a handle to the stage
  	  Stage stage = (Stage) LogOutBtn_StudentHome.getScene().getWindow();
  	  // do what you have to do
  	  stage.close();
  	ClientUI.chat.accept(new MessagesClass(Messages.updateStatusOfLogIn, ChatClient.user));
	LoginFormController aFrame = new LoginFormController(); // create StudentFrame
	Stage primaryStage = new Stage();
	aFrame.start(primaryStage);
    }

    @FXML
    void StartExamBtn_Student(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		((Node)event.getSource()).getScene().getWindow().hide();
		Pane root = loader.load(getClass().getResource("/gui/StartExamForm.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Start Exam Page");
		primaryStage.setScene(scene);
		primaryStage.show();

    }
    
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		javafx.application.Platform.runLater( new Runnable() {
	           @Override
	           public void run() {
	        	   StudentFirstNameLbl_StudentHome.setText(ChatClient.user.getFirstName());
	        	   StudentLastNameLbl_StudentHome.setText(ChatClient.user.getLastName());
	        	   StudentIdLbl_StudentHome.setText(ChatClient.user.getID());
	        	   StudentFacultyLbl_StudentHome.setText(ChatClient.user.getFaculty());
	           }
			});
		
	}
	//test

}
