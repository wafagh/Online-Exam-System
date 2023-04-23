package gui;

import java.io.IOException;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Messages;
import models.MessagesClass;
import models.Question;
import models.Topic;

public class AddQuestionController implements Initializable {

	private URL location;
	private ResourceBundle resources;
	@FXML
	Label QuestionNumberLbl_AddQuestionController;

	@FXML
	Label LecturerNameLbl_AddQuestionController;

	@FXML
	private ChoiceBox<String> Topic_AddQuestionController;

	@FXML
	private TextField QuestionText_AddQuestionController;

	@FXML
	private TextField Answer1_AddQuestionController;

	@FXML
	private TextField Answer2_AddQuestionController;

	@FXML
	private TextField Answer3_AddQuestionController;

	@FXML
	private TextField Answer4_AddQuestionController;

	@FXML
	private RadioButton SelectAnswer1_AddQuestionController;

	@FXML
	private RadioButton SelectAnswer2_AddQuestionController;

	@FXML
	private RadioButton SelectAnswer3_AddQuestionController;

	@FXML
	private RadioButton SelectAnswer4_AddQuestionController;

	@FXML
	ToggleGroup group = new ToggleGroup();

	@FXML
	private Button confirm_AddQuestionController;

	@FXML
	private Button cancel_AddQuestionController;

	ObservableList<Topic> data;

	@FXML
	void cancel_AddQuestionController(ActionEvent event) throws Exception {

		System.out.println("back");
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/QuestionBank.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Question Bank Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void confirm_AddQuestionController(ActionEvent event) throws Exception {

		String questID = null;
		String value = (String) Topic_AddQuestionController.getValue();
		String questnumber = "000";

		for (Question a : ChatClient.questionslist) {
			if (Integer.valueOf(a.getQuestnumber()) > Integer.valueOf(questnumber)) {
				questnumber = "" + a.getQuestnumber();
			}
		}
		questnumber = "" + (Integer.valueOf(questnumber) + 1);

		for (Topic a : ChatClient.topiceslist) {
			if (a.getTopic().equals(value)) {
				if (ChatClient.questionslist.size() == 0) {
					questID = a.getTopicID() + questnumber;
				} else if (ChatClient.questionslist.size() < 9) {
					questID = a.getTopicID() + "00" + questnumber;
				} else if (ChatClient.questionslist.size() < 100 && ChatClient.questionslist.size() >= 9) {
					questID = a.getTopicID() + "0" + questnumber;
				} else {
					questID = a.getTopicID() + questnumber;
				}
			}
		}

		SelectAnswer1_AddQuestionController.setToggleGroup(group);
		SelectAnswer1_AddQuestionController.setUserData(1);
		SelectAnswer2_AddQuestionController.setToggleGroup(group);
		SelectAnswer2_AddQuestionController.setUserData(2);
		SelectAnswer3_AddQuestionController.setToggleGroup(group);
		SelectAnswer3_AddQuestionController.setUserData(3);
		SelectAnswer4_AddQuestionController.setToggleGroup(group);
		SelectAnswer4_AddQuestionController.setUserData(4);
		
		
		if (QuestionText_AddQuestionController.getText().equals("") || Topic_AddQuestionController.getValue() == null
				|| Answer1_AddQuestionController.getText().equals("")
				|| Answer2_AddQuestionController.getText().equals("")
				|| Answer3_AddQuestionController.getText().equals("")
				|| Answer4_AddQuestionController.getText().equals("") || group.getSelectedToggle() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Error");
			alert.setContentText("Ooops, there was an missing information!");

			alert.showAndWait();
		} else {
			Question question = new Question(questID, QuestionText_AddQuestionController.getText(),
					Topic_AddQuestionController.getValue(), Answer1_AddQuestionController.getText(),
					Answer2_AddQuestionController.getText(), Answer3_AddQuestionController.getText(),
					Answer4_AddQuestionController.getText(), (int) group.getSelectedToggle().getUserData(),
					ChatClient.user.getFirstName(), Integer.valueOf(questnumber));
			MessagesClass msg = new MessagesClass(Messages.AddQuestion, question);
			ClientUI.chat.accept(msg);
			msg = new MessagesClass(Messages.OpenQuestionBank, null);
			ClientUI.chat.accept(msg);
			initialize(location, resources);

			System.out.println("Question Added");
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.close();
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/gui/QuestionBank.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setTitle("Question Bank Page");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.location = location;
		this.resources = resources;
		javafx.application.Platform.runLater(new Runnable() {
			@Override
			public void run() {

				String questnumber = "0";

				for (Question a : ChatClient.questionslist) {
					if (Integer.valueOf(a.getQuestnumber()) > Integer.valueOf(questnumber)) {
						questnumber = "" + a.getQuestnumber();
					}
				}
				questnumber = "" + (Integer.valueOf(questnumber) + 1);

				MessagesClass msg = new MessagesClass(Messages.FillTopicChoiceBox, null);
				ClientUI.chat.accept(msg);

				data = FXCollections.observableArrayList(ChatClient.topiceslist);
				for (int i = 0; i < data.size(); i++) {
					Topic_AddQuestionController.getItems().add(data.get(i).getTopic());
				}
				QuestionNumberLbl_AddQuestionController.setText(questnumber);
				LecturerNameLbl_AddQuestionController.setText(ChatClient.user.getFirstName());
			}

		});

	}
}
