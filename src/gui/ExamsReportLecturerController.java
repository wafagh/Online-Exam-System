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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Exam;
import models.ExamsInProgress;
import models.Messages;
import models.MessagesClass;
import models.ReportExam;

public class ExamsReportLecturerController implements Initializable{
	
	@FXML
    private Button OpenReport_examreportForm;
	
	@FXML
    private Button cancel_examreportForm;
	
	@FXML
	Button ShowResults_examreportform;
	
	public static ExamsInProgress exam;
	@FXML
	TableView<ExamsInProgress> examsTbl;
	 
	
	@FXML
	private TableColumn<ExamsInProgress, String> Examid;
	
	@FXML
	private TableColumn<ExamsInProgress, String> Topic;
	
	@FXML
	private TableColumn<ExamsInProgress, String> Course;
	
	@FXML
	private TableColumn<ExamsInProgress, String> ExamTime;
	
	
	ObservableList<ExamsInProgress> data;
	
	@FXML
    void cancel_examreportForm(ActionEvent event) throws Exception{

	
	 System.out.println("Canceled");
	 Stage stage = (Stage) cancel_examreportForm.getScene().getWindow();
		stage.close();
	 FXMLLoader loader = new FXMLLoader();
     Stage primaryStage = new Stage();
	 Pane root = loader.load(getClass().getResource("/gui/LectureHomePage.fxml").openStream());
	 Scene scene = new Scene(root);			
	 primaryStage.setTitle("Lecturer Home Page");
	 primaryStage.setScene(scene);		
	 primaryStage.show();
	 
	 
    }
	
	@FXML
	void OpenReport_examreportForm(ActionEvent event) throws Exception{
		System.out.println("open report.....");
		ExamsInProgress exam = examsTbl.getSelectionModel().getSelectedItem();
		if (exam == null) {
			Alert a = new Alert(AlertType.NONE);
			// set alert type
			a.setAlertType(AlertType.ERROR);
			// set content text
			a.setContentText("Please Chose An Exam");
			// show the dialog
			a.showAndWait();
		} else {
			System.out.println(exam.getId());
			MessagesClass msg = new MessagesClass(Messages.GetExamReport,exam);
			ClientUI.chat.accept(msg);
			
			System.out.println("Opened Report Page");
			FXMLLoader loader = new FXMLLoader();
			 Stage primaryStage = new Stage();
			 ((Node)event.getSource()).getScene().getWindow().hide();// do this 3 lines for all the roles no matter what
			 Pane root = loader.load(getClass().getResource("/gui/StatAnalysisRep.fxml").openStream());
			 Scene scene = new Scene(root);			
			 primaryStage.setTitle("Exams Bank");
			 primaryStage.setScene(scene);		
			 primaryStage.show();
			 
		}
		
	}
	
	
	@FXML
	void ShowResults_examreportform(ActionEvent event) throws Exception{
		 exam = examsTbl.getSelectionModel().getSelectedItem();
		if (exam == null) {
			Alert a = new Alert(AlertType.NONE);
			// set alert type
			a.setAlertType(AlertType.ERROR);
			// set content text
			a.setContentText("Please Choose An Exam");
			// show the dialog
			a.showAndWait();
		} else {
			System.out.println(exam.getId());
			MessagesClass msg = new MessagesClass(Messages.GetExamResults, exam);
			ClientUI.chat.accept(msg);

			System.out.println("Opened Results Page");
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.close();
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/gui/Students_Result.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setTitle("Exams Bank");
			primaryStage.setScene(scene);
			primaryStage.show();

		}
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		javafx.application.Platform.runLater( new Runnable() {
	           @Override
	           public void run() {
	        	   Examid.setCellValueFactory(new PropertyValueFactory<ExamsInProgress, String>("examid"));
	               Topic.setCellValueFactory(new PropertyValueFactory<ExamsInProgress, String>("topic"));
	               Course.setCellValueFactory(new PropertyValueFactory<ExamsInProgress, String>("course"));
	               ExamTime.setCellValueFactory(new PropertyValueFactory<ExamsInProgress, String>("time"));
	               
	               data=FXCollections.observableArrayList(ChatClient.examslist);
	               examsTbl.setItems(data);
	           }
		
	    });
	
	}

}
