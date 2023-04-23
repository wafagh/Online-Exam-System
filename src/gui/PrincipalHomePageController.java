package gui;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Messages;
import models.MessagesClass;

import java.io.IOException;

import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class PrincipalHomePageController {
	@FXML
	private Button requestsBtn;
	
	@FXML
	private Button reportsBtn;
	
	@FXML Button exitBtn;
	 
	 @FXML
	    void exitBtn(ActionEvent event) throws Exception {
//	    	ClientUI.chat.client.quit();
//	    	System.out.println("Principal home page Exit");
//	    	System.exit(0);
			// get a handle to the stage
		// ClientUI.chat.client.quit();
	  	  Stage stage = (Stage) exitBtn.getScene().getWindow();
	  	  // do what you have to do
	  	  stage.close();
	  	ClientUI.chat.accept(new MessagesClass(Messages.updateStatusOfLogIn, ChatClient.user));
	  	  
		LoginFormController aFrame = new LoginFormController(); // create StudentFrame
		Stage primaryStage = new Stage();
		aFrame.start(primaryStage);
	    }
	 @FXML
	 void ReportHandler(ActionEvent event) throws IOException{
		 	FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			
			((Node) event.getSource()).getScene().getWindow().hide();
			Pane root = loader.load(getClass().getResource("/gui/ReportMain.fxml").openStream());
		
			Scene scene = new Scene(root);
			primaryStage.setTitle("Requests Page");
			primaryStage.setScene(scene);
			primaryStage.show();
			
	 }
	@FXML
	void RequestsHandle(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		
		((Node) event.getSource()).getScene().getWindow().hide();
		Pane root = loader.load(getClass().getResource("/gui/ConfrmationRequestForm.fxml").openStream());
	
		ConfrmationRequestController CRFC = new ConfrmationRequestController();
		
		Scene scene = new Scene(root);
		primaryStage.setTitle("Requests Page");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	
}
