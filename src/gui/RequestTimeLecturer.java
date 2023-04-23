package gui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;

import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.ExamsInProgress;
import models.Messages;
import models.MessagesClass;
import models.RequestsForTime;

public class RequestTimeLecturer implements Initializable {

	@FXML
	Label Examid;
	@FXML
	Label course;
	@FXML
	Label subject;
	@FXML
	Label time;
	@FXML
	TextField newduration;
	@FXML
	TextArea reason;
	@FXML
	Button send_reqform;
	@FXML
	Button cancel_reqform;

	@FXML
	void cancel_reqform(ActionEvent event) throws Exception {

		System.out.println("Canceled");
		Stage stage = (Stage) cancel_reqform.getScene().getWindow();

		stage.close();
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/gui/ExamsInProgressTime.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Lecturer Home Page");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@FXML
	void send_reqform(ActionEvent event) throws Exception {
		Timer timer=null;
		if (reason.getText().equals("") || newduration.getText().equals("")) {
			Alert a = new Alert(AlertType.NONE);

			// set alert type
			a.setAlertType(AlertType.ERROR);
			// set content text
			a.setContentText("Missing Information");
			// show the dialog
			a.showAndWait();

		} else {
			if (RequestExtraTimeController.exam.getStatus().equals("Done")
					|| RequestExtraTimeController.exam.getStatus().equals("Locked")) {
				System.out.println("cant request for this exam");
			} else {
				RequestExtraTimeController.exam.setStatus("Requested");
				RequestExtraTimeController.exam.setReduration(newduration.getText());
				RequestsForTime req = new RequestsForTime(0, null, null, null, null, null, null, null, null);
				req.setRequesterId(ChatClient.user.getID());
				req.setExam_id(RequestExtraTimeController.exam.getId());
				req.setRequesterName(ChatClient.user.getFirstName());
				req.setCourse(RequestExtraTimeController.exam.getCourse());
				req.setPreduration(RequestExtraTimeController.exam.getTime());
				req.setReduration(RequestExtraTimeController.exam.getReduration());
				req.setTopic(RequestExtraTimeController.exam.getTopic());
				req.setReason(reason.getText());
				for(ExamsInProgress a : ChatClient.examslist)
					if(a.getId().equals(RequestExtraTimeController.exam.getId()))
						 timer= a.getTimer();
				
				MessagesClass msg = new MessagesClass(Messages.SentTimeRequest, req,timer);
				ClientUI.chat.accept(msg);
				RequestExtraTimeController.exam.setStatus("Requested");

				System.out.println("sent");
				Stage stage = (Stage) send_reqform.getScene().getWindow();
				stage.close();
				Stage primaryStage = new Stage();
				FXMLLoader loader = new FXMLLoader();
				Pane root = loader.load(getClass().getResource("/gui/ExamsInProgressTime.fxml").openStream());
				Scene scene = new Scene(root);
				primaryStage.setTitle("Request More Time");
				primaryStage.setScene(scene);
				primaryStage.show();
			}

		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("getting into inizle");

		javafx.application.Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (RequestExtraTimeController.exam != null) {
					Examid.setText(RequestExtraTimeController.exam.getId());
					course.setText(RequestExtraTimeController.exam.getCourse());
					subject.setText(RequestExtraTimeController.exam.getTopic());
					time.setText(RequestExtraTimeController.exam.getTime());

				}
			}
		});

	}

}