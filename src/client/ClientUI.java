package client;
import javafx.application.Application;

import javafx.stage.Stage;
//import logic.Exam;


import java.util.Vector;

//import gui.ExamBankController;
import client.ClientController;
import gui.LoginFormController;

public class ClientUI extends Application {
	public static ClientController chat; //only one instance

	public static void main( String args[] ) throws Exception
	   { 
		    launch(args);  
	   } // end main
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		 chat = new ClientController("localhost", 5555);
		// TODO Auto-generated method stub
						  		
		LoginFormController aFrame = new LoginFormController(); // create StudentFrame
		aFrame.start(primaryStage);
		
	}
	
	
}
