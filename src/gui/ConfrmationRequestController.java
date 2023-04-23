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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Messages;
import models.MessagesClass;
import models.RequestsForTime;


public class ConfrmationRequestController implements Initializable{

	
	@FXML
	TableView<RequestsForTime> requestsTable =new TableView<RequestsForTime>();

	
	
	@FXML
	TableColumn<?,?> ReqList_ExamId;
	@FXML
	TableColumn<?,?> ReqList_reqid;
	@FXML
	TableColumn<?,?> ReqList_reqName;
	@FXML
	TableColumn<?,?> ReqList_PreDuartion;
	@FXML
	TableColumn<?,?> ReqList_reDuration;
	@FXML
	TableColumn<?,?> ReqList_topic;
	@FXML
	TableColumn<?,?> ReqList_course;
	
	 public static ObservableList<RequestsForTime> List =FXCollections.observableArrayList(); 
	
	@FXML
    void backBtn(ActionEvent event) throws Exception{
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		((Node) event.getSource()).getScene().getWindow().hide();
		Pane root = loader.load(getClass().getResource("/gui/PrincipalHomePage.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Principal Home Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	
	}
	@FXML 
	void confirmTheRequest() {
		System.out.println("confirmm");
		RequestsForTime exam = requestsTable.getSelectionModel().getSelectedItem();
		if(exam!=null) {
			System.out.println(exam.getExam_id());
			MessagesClass msg = new MessagesClass(Messages.confirmTheRequest,exam);
			ClientUI.chat.accept(msg);
			
			
			
		}
		
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		
		///////////////////////////////////////update pre re duation
		List.clear();
		ReqList_ExamId.setCellValueFactory(new PropertyValueFactory<>("exam_id"));
		ReqList_reqid.setCellValueFactory(new PropertyValueFactory<>("requesterId"));
		ReqList_reqName.setCellValueFactory(new PropertyValueFactory<>("requesterName"));
		ReqList_PreDuartion.setCellValueFactory(new PropertyValueFactory<>("preduration"));
		ReqList_reDuration.setCellValueFactory(new PropertyValueFactory<>("reduration"));
		ReqList_topic.setCellValueFactory(new PropertyValueFactory<>("topic"));
		ReqList_course.setCellValueFactory(new PropertyValueFactory<>("course"));
		
	   System.out.println("getting into inizle");
    	//ObservableList<ConfirmationRequestsInfo> theListToAdd = requestsTable.getItems();
	  
	   MessagesClass msg = new MessagesClass(Messages.getRequestsToPrincipal,null);
		ClientUI.chat.accept(msg);
		if (ChatClient.requestsList != null)
		{
			System.out.println("Controller :-" + ChatClient.requestsList.toString());
			
		}
		List.addAll(ChatClient.requestsList);
		
    	//theListToAdd.addAll(ChatClient.requestsList);
    	//requestsTable.setItems(theListToAdd);
    	requestsTable.getItems().addAll(List);
		//requestsTable.setEditable(true);
		
	}
	
	
	
}
