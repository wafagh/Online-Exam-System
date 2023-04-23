package gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ReportMainController {

	
	
	private void openWindow(ActionEvent event,String window,String title) throws IOException {
		System.out.println("opneing");
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		((Node) event.getSource()).getScene().getWindow().hide();
		Pane root = loader.load(getClass().getResource(window).openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle(title);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	@FXML
    void studentReportHandler(ActionEvent event) throws Exception{
		System.out.println("report by student");
		ReportGeniricController.s="report by student:";
		openWindow(event,"/gui/ReportGeneric.fxml","Report");
		
	}
	@FXML
    void lectureReportHandler(ActionEvent event) throws Exception{
		ReportGeniricController.s="report by Lecture:";
		
		openWindow(event,"/gui/ReportGeneric.fxml","Report");
		
	
	}
	@FXML
    void CourseReportHandler(ActionEvent event) throws Exception{
		ReportGeniricController.s="report by Course:";
		
		openWindow(event,"/gui/ReportGeneric.fxml","Report");
	
	
	}
	@FXML
    void backBtn(ActionEvent event) throws Exception{
	
		openWindow(event,"/gui/PrincipalHomePage.fxml","Principal Home Page");
	
	}
	@FXML 
	void histogramOpen(ActionEvent event) throws Exception {
		new histogramController().start(new Stage());
	}
}
