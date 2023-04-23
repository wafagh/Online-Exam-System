package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.ExamsInProgress;
import models.Messages;
import models.MessagesClass;
import models.ReportExam;

public class ReportGeniricController implements Initializable {

	@FXML
	Label changerLbl;
	@FXML
	TableView<ReportExam> reportTBL;
	@FXML
	private TableColumn<ReportExam, String> courseT;
	@FXML
	private TableColumn<ReportExam, String> topicT;
	@FXML
	private TableColumn<ReportExam, String> AvgT;
	@FXML
	private TableColumn<ReportExam, String> studentNumT;
	@FXML
	private TableColumn<ReportExam, String> StanDevT;
	@FXML
	private TableColumn<ReportExam, String> dateT;
	@FXML
	private TableColumn<ReportExam, String> medianT;
	@FXML
	private TableColumn<ReportExam, String> highestT;
	@FXML
	private TableColumn<ReportExam, String> lowestT;
	
	ObservableList<ReportExam> data;
	static String s;
	static String strToShow;
	
	@FXML
	void backBtn(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		((Node) event.getSource()).getScene().getWindow().hide();
		Pane root = loader.load(getClass().getResource("/gui/ReportMain.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Report Home Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void load(String s) {

		// changerLbl.setText("");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		switch (s) {
		case "report by student:":

			// take the id
			String id=getTheIdFromThePrinicpal("Student");
			ClientUI.chat.accept(new MessagesClass(Messages.getNameOfTheStudentReport,id));
			String studentName=ChatClient.nameOfTheStudent_report;
			changerLbl.setText(s+"   "+studentName);
			MessagesClass msg = new MessagesClass(Messages.getReportByStudent, id);
			ClientUI.chat.accept(msg);
			if (ChatClient.reportList != null) {
				//System.out.println("Controller :-" + ChatClient.reportList.get(0).getCourse());
				data=FXCollections.observableArrayList(ChatClient.reportList);
				initz();
				
				reportTBL.setItems(data);
			}
			
			
			
			break;
		case "report by Lecture:":

			// take the id
			String idLecturer=getTheIdFromThePrinicpal("Lecturer");
			ClientUI.chat.accept(new MessagesClass(Messages.getNameOfTheStudentReport,idLecturer));
			String LecturerName=ChatClient.nameOfTheStudent_report;
			changerLbl.setText(s+"   "+LecturerName);
			MessagesClass msgLec = new MessagesClass(Messages.getReportByLecturer, idLecturer);
			ClientUI.chat.accept(msgLec);
			if (ChatClient.reportList != null) {
				//System.out.println("Controller :-" + ChatClient.reportList.get(0).getCourse());
				data=FXCollections.observableArrayList(ChatClient.reportList);
				
			}
			initz();
			
			reportTBL.setItems(data);
			
			
			break;
		case "report by Course:":
			// take the course name
						String CourseName=getTheIdFromThePrinicpal("Course");
						changerLbl.setText(s+"   "+CourseName);
						MessagesClass msgCourse = new MessagesClass(Messages.getReportByCourse, CourseName);
						ClientUI.chat.accept(msgCourse);
						if (ChatClient.reportList != null) {
							//System.out.println("Controller :-" + ChatClient.reportList.get(0).getCourse());
							data=FXCollections.observableArrayList(ChatClient.reportList);
						}
						initz();
						
						reportTBL.setItems(data);
						
						
			
			break;
		}

	}
	
	
	

	private void initz() {
		courseT.setCellValueFactory(new PropertyValueFactory<ReportExam, String>("course"));
		topicT.setCellValueFactory(new PropertyValueFactory<ReportExam, String>("topic"));
		AvgT.setCellValueFactory(new PropertyValueFactory<ReportExam, String>("Average"));
		studentNumT.setCellValueFactory(new PropertyValueFactory<ReportExam, String>("numbOfStudents"));
		StanDevT.setCellValueFactory(new PropertyValueFactory<ReportExam, String>("StandDeviation"));
		dateT.setCellValueFactory(new PropertyValueFactory<ReportExam, String>("Date"));
		medianT.setCellValueFactory(new PropertyValueFactory<ReportExam, String>("medianX"));
		highestT.setCellValueFactory(new PropertyValueFactory<ReportExam, String>("HighestGrade"));
		lowestT.setCellValueFactory(new PropertyValueFactory<ReportExam, String>("lowestGrade"));
		
	}

	private String getTheIdFromThePrinicpal(String s) {
		TextInputDialog dialog = new TextInputDialog("");
    	dialog.setTitle("get the report by:"+s);
    	if(s.equals("Student")||s.equals("Lecturer"))
    	{
    		dialog.setHeaderText("Please enter the id of the:"+s);
        	dialog.setContentText("ID:");
    	}
    	else if(s.equals("Course"))
    	{
    		dialog.setHeaderText("Please enter the name of the:"+s);
    		dialog.setContentText("Name:");
    	}
    	// Traditional way to get the response value.
    	Optional<String> result = dialog.showAndWait();
//    	if (result.isPresent() && Integer.parseInt(result.get())>0){
//    		
//    		
//    	}
    	System.out.println(result.get());
    	return result.get();
		
	}
	
	@FXML
	void histogramBtn(ActionEvent event) throws IOException {
		ReportExam exam = reportTBL.getSelectionModel().getSelectedItem();
		System.out.println(exam.getGrades().toString());
		histogramController.courseName=exam.getCourse();
		histogramController.topicName=exam.getTopic();
		histogramController.Date=exam.getDate();
				
		histogramController.grades=exam.getGrades();
		new histogramController().start(new Stage());
	}

}
