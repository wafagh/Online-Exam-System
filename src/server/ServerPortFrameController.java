package server;

import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import client.ChatClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

//import logic.Exam;
import server.EchoServer;
import server.ServerUI;

public class ServerPortFrameController implements Initializable {
	// private Pane root = new Pane();
	private Label Host = new Label();
	private Label IP = new Label();
	private Label Status = new Label();
	private Label Hosttxt = new Label();
	private Label IPtxt = new Label();
	private Label Statustxt = new Label();
	public static boolean firstTime = false;
	@FXML
	TableView<clientDetails> tableServer;
	@FXML
	private  TableColumn<clientDetails, String> colHostName;
	@FXML
	private  TableColumn<clientDetails, String> colIP;
	@FXML
	private  TableColumn<clientDetails, String> colStatus;
	private static ObservableList<clientDetails> clients=FXCollections.observableArrayList();
	private static ArrayList<clientDetails> clientArrayList = new ArrayList<clientDetails>();

	public void start(Stage primaryStage) throws Exception {
		IP.setText("IP: ");
		Host.setText("Host: ");
		Status.setText("Status: ");
		Hosttxt.setText(" ");
		IPtxt.setText(" ");
		Statustxt.setText(" ");

		/*
		 * root.getChildren().add(Host); Host.setLayoutX(14); Host.setLayoutY(40);
		 * root.getChildren().add(IP); IP.setLayoutX(14); IP.setLayoutY(89);
		 * root.getChildren().add(Status); Status.setLayoutX(14);
		 * Status.setLayoutY(132); root.getChildren().add(Hosttxt);
		 * Hosttxt.setLayoutX(84); Hosttxt.setLayoutY(40);
		 * root.getChildren().add(IPtxt); IPtxt.setLayoutX(84); IPtxt.setLayoutY(89);
		 * root.getChildren().add(Statustxt); Statustxt.setLayoutX(82);
		 * Statustxt.setLayoutY(132);
		 */
		FXMLLoader loader = new FXMLLoader();

		Pane root = loader.load(getClass().getResource("/server/serverFxml.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.show();
		//ServerUI.runServer(ServerUI.DEFAULT_PORT);

//		primaryStage.setTitle("Server");
//		primaryStage.setScene(new Scene(root,360,260));
//		primaryStage.show();
//		ServerUI.runServer(ServerUI.DEFAULT_PORT);
	}

	private URL location;
	private ResourceBundle resources;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.location = location;
		this.resources = resources;
		
	
	
		colHostName.setCellValueFactory(new PropertyValueFactory<clientDetails, String>("hostName"));
		colIP.setCellValueFactory(new PropertyValueFactory<clientDetails, String>("ip"));
		colStatus.setCellValueFactory(new PropertyValueFactory<clientDetails, String>("Status"));
		//clientArrayList.add(new clientDetails("name","ip","status"));
	//	clients.addAll(clientArrayList);
		tableServer.setItems(clients);
		
	}
	public void startServerBtn(ActionEvent event) throws Exception {
		ServerUI.runServer(ServerUI.DEFAULT_PORT);
		DBO.connecttodb();
		DBO.setAllStatusZero();
		
		
		
	}
	public void getExitBtn(ActionEvent event) throws Exception {
		System.out.println("exit Exams Tool");
		System.exit(0);
	}

	public void UpdateClient(InetAddress Host, String IP, String Status) {

	
		javafx.application.Platform.runLater(new Runnable() {
			@Override
			public void run() {
				//clientArrayList.add(new clientDetails(Host.toString(), IP, Status));
				System.out.println("updated:"+Host.toString()+" "+IP+"   "+Status);
				
				//clients.addAll(clientArrayList);new 
				clients.add(new clientDetails(Host.toString(), IP, Status));
				//tableServer.setItems(clients);
				///tableServer.refresh();
				
				}
		});
		
		//clients.add(new clientDetails(Host.toString(), IP, Status));
		
		//initialize(location, resources);
//		System.out.println("ntka3 here");
//		Hosttxt.setText(Host.toString());
//		IPtxt.setText(IP);
//		Statustxt.setText(Status);
//           }
//		});
	
		
	
	
		}
	
}