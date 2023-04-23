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

public class EditQuestionController implements Initializable {

	private URL location;
	private ResourceBundle resources;
	@FXML
	Label QuestionIDLbl_EditQuestionController;

	@FXML
	Label LecturerNameLbl_EditQuestionController;

	@FXML
	Label Topic_EditQuestionController;

	@FXML
	private TextField QuestionText_EditQuestionController;

	@FXML
	private TextField Answer1_EditQuestionController;

	@FXML
	private TextField Answer2_EditQuestionController;

	@FXML
	private TextField Answer3_EditQuestionController;

	@FXML
	private TextField Answer4_EditQuestionController;

	@FXML
	private RadioButton SelectAnswer1_EditQuestionController;

	@FXML
	private RadioButton SelectAnswer2_EditQuestionController;

	@FXML
	private RadioButton SelectAnswer3_EditQuestionController;

	@FXML
	private RadioButton SelectAnswer4_EditQuestionController;

	@FXML
	ToggleGroup group = new ToggleGroup();

	@FXML
	private Button confirm_EditQuestionController;

	@FXML
	private Button cancel_EditQuestionController;

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

		SelectAnswer1_EditQuestionController.setToggleGroup(group);
		SelectAnswer1_EditQuestionController.setUserData(1);
		SelectAnswer2_EditQuestionController.setToggleGroup(group);
		SelectAnswer2_EditQuestionController.setUserData(2);
		SelectAnswer3_EditQuestionController.setToggleGroup(group);
		SelectAnswer3_EditQuestionController.setUserData(3);
		SelectAnswer4_EditQuestionController.setToggleGroup(group);
		SelectAnswer4_EditQuestionController.setUserData(4);

		if (QuestionText_EditQuestionController.getText().equals("")
				|| Answer1_EditQuestionController.getText().equals("")
				|| Answer2_EditQuestionController.getText().equals("")
				|| Answer3_EditQuestionController.getText().equals("")
				|| Answer4_EditQuestionController.getText().equals("") || group.getSelectedToggle() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Error");
			alert.setContentText("Ooops, there was an missing information!");

			alert.showAndWait();
		} else {
			Question question = new Question(ChatClient.questiontoedit.getIdquest(),
					ChatClient.questiontoedit.getQuesttopic(), QuestionText_EditQuestionController.getText(),
					Answer1_EditQuestionController.getText(), Answer2_EditQuestionController.getText(),
					Answer3_EditQuestionController.getText(), Answer4_EditQuestionController.getText(),
					(int) group.getSelectedToggle().getUserData(), ChatClient.questiontoedit.getTeachername(),
					ChatClient.questiontoedit.getQuestnumber());
			MessagesClass msg = new MessagesClass(Messages.EditQuestion, question);
			ClientUI.chat.accept(msg);
			msg = new MessagesClass(Messages.OpenQuestionBank, null);
			ClientUI.chat.accept(msg);
			initialize(location, resources);

			System.out.println("Question Updated");
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

				QuestionIDLbl_EditQuestionController.setText(ChatClient.questiontoedit.getIdquest());
				LecturerNameLbl_EditQuestionController.setText(ChatClient.questiontoedit.getTeachername());
				Topic_EditQuestionController.setText(ChatClient.questiontoedit.getQuesttopic());
				QuestionText_EditQuestionController.setText(ChatClient.questiontoedit.getQuesttext());
				Answer1_EditQuestionController.setText(ChatClient.questiontoedit.getAnswer1());
				Answer2_EditQuestionController.setText(ChatClient.questiontoedit.getAnswer2());
				Answer3_EditQuestionController.setText(ChatClient.questiontoedit.getAnswer3());
				Answer4_EditQuestionController.setText(ChatClient.questiontoedit.getAnswer4());
				if (ChatClient.questiontoedit.getCorrectanswer() == 1) {
					SelectAnswer1_EditQuestionController.setSelected(true);
				} else if (ChatClient.questiontoedit.getCorrectanswer() == 2) {
					SelectAnswer2_EditQuestionController.setSelected(true);
				} else if (ChatClient.questiontoedit.getCorrectanswer() == 3) {
					SelectAnswer3_EditQuestionController.setSelected(true);
				} else {
					SelectAnswer4_EditQuestionController.setSelected(true);
				}
			}

		});

	}
}
