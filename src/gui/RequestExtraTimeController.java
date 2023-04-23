package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.ExamsInProgress;
import models.Messages;
import models.MessagesClass;

public class RequestExtraTimeController implements Initializable {

	private URL location;
	private ResourceBundle resources;

	@FXML
	private Button reqtimechange_reqextratimeForm;

	@FXML
	private Button cancel_reqextratimeForm;

	@FXML
	Button lock_unlock;

	@FXML
	TableView<ExamsInProgress> examsTbl;

	@FXML
	private TableColumn<ExamsInProgress, String> Examid;

	@FXML
	private TableColumn<ExamsInProgress, String> Topic;

	@FXML
	private TableColumn<ExamsInProgress, String> Course;

	@FXML
	private TableColumn<ExamsInProgress, String> Preduration;

	@FXML
	private TableColumn<ExamsInProgress, String> Reduration;

	@FXML
	private TableColumn<ExamsInProgress, String> Status;

	ObservableList<ExamsInProgress> data;
	public static ExamsInProgress exam;

	@FXML
	void cancel_reqextratimeForm(ActionEvent event) throws Exception {

		System.out.println("Canceled");
		Stage stage = (Stage)cancel_reqextratimeForm.getScene().getWindow();
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
	void reqtimechange_reqextratimeForm(ActionEvent event) throws Exception {
		exam = examsTbl.getSelectionModel().getSelectedItem();
		if (exam == null) {
			Alert a = new Alert(AlertType.NONE);
			// set alert type
			a.setAlertType(AlertType.ERROR);
			// set content text
			a.setContentText("Please Chose An Exam");
			// show the dialog
			a.showAndWait();
		} else {
			if (exam.getStatus().equals("Done") || exam.getStatus().equals("Locked")) {
				System.out.println("cant request for this exam");
				
				Alert a = new Alert(AlertType.NONE);
				// set alert type
				a.setAlertType(AlertType.ERROR);
				// set content text
				a.setContentText("Cant Request For This Exam");
				// show the dialog
				a.showAndWait();
			} else {
				System.out.println("Opened request form");
				Stage stage = (Stage)cancel_reqextratimeForm.getScene().getWindow();
				stage.close();
				FXMLLoader loader = new FXMLLoader();
				Stage primaryStage = new Stage();
				Pane root = loader.load(getClass().getResource("/gui/REQ.fxml").openStream());
				Scene scene = new Scene(root);
				primaryStage.setTitle("Request Extra Time");
				primaryStage.setScene(scene);
				primaryStage.show();
			}

		}

	}

	@FXML
	void lock_unlock(ActionEvent event) throws Exception {
		exam = examsTbl.getSelectionModel().getSelectedItem();
		if (exam == null) {
			Alert a = new Alert(AlertType.NONE);
			// set alert type
			a.setAlertType(AlertType.ERROR);
			// set content text
			a.setContentText("Please Chose An Exam");
			// show the dialog
			a.showAndWait();
		} else {
			if (!(RequestExtraTimeController.exam.getStatus().equals("Done"))) {
				MessagesClass msg = new MessagesClass(Messages.LockUnlock, exam);
				ClientUI.chat.accept(msg);
				msg = new MessagesClass(Messages.OpenedExamInProgressForm, ChatClient.user);
				ClientUI.chat.accept(msg);
				initialize(location, resources);
			} else {
				Alert a = new Alert(AlertType.NONE);
				// set alert type
				a.setAlertType(AlertType.ERROR);
				// set content text
				a.setContentText("Cant Lock-UnLock this exam");
				// show the dialog
				a.showAndWait();
			}
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.location = location;
		this.resources = resources;
		javafx.application.Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Examid.setCellValueFactory(new PropertyValueFactory<ExamsInProgress, String>("id"));
				Topic.setCellValueFactory(new PropertyValueFactory<ExamsInProgress, String>("topic"));
				Course.setCellValueFactory(new PropertyValueFactory<ExamsInProgress, String>("course"));
				Preduration.setCellValueFactory(new PropertyValueFactory<ExamsInProgress, String>("time"));
				Reduration.setCellValueFactory(new PropertyValueFactory<ExamsInProgress, String>("reduration"));
				Status.setCellValueFactory(new PropertyValueFactory<ExamsInProgress, String>("status"));
				data = FXCollections.observableArrayList(ChatClient.examslist);
				examsTbl.setItems(data);
			}

		});

	}

}
