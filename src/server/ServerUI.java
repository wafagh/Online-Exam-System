package server;

import javafx.application.Application;
import javafx.stage.Stage;
//import logic.Exam;
import server.EchoServer;

//import java.util.Vector;
//import gui.ServerPortFrameController;

public class ServerUI extends Application {
	final public static int DEFAULT_PORT = 5555;
	
	public static ServerPortFrameController aFrame;
	public static void main( String args[] ) throws Exception
	   {   
		 launch(args);
	  } // end main
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub				  		
		aFrame = new ServerPortFrameController(); // create ServerFrame
		
		aFrame.start(primaryStage);
		
	}
	
	public static void runServer(int defaultPort)
	{
		 int port = 0; //Port to listen on

	        try
	        {
	        	port = defaultPort; //Set port to 5555
	        }
	        catch(Throwable t)
	        {
	        	System.out.println("ERROR - Could not connect!");
	        }
	    	
	        EchoServer sv = new EchoServer(port);
	        
	        try 
	        {
	          sv.listen(); //Start listening for connections
	        } 
	        catch (Exception ex) 
	        {
	          System.out.println("ERROR - Could not listen for clients!");
	        }
	}
	

}
