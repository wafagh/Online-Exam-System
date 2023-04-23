package gui;

import java.net.URL;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.ExamsInProgress;
import models.Messages;
import models.MessagesClass;
import models.Question;

public class QuestionBankController implements Initializable {

	private URL location;
	private ResourceBundle resources;
	@FXML
	private Button search_QuestionBankController;

	@FXML
	private Button editquest_QuestionBankController;

	@FXML
	private Button deletequest_QuestionBankController;

	@FXML
	private Button addquest_QuestionBankController;

	@FXML
	private Button cancel_QuestionBankController;

	@FXML
	private TextField searchtext_QuestionBankController;

	@FXML
	TableView<Question> questionTbl;

	@FXML
	private TableColumn<Question, String> idquest;

	@FXML
	private TableColumn<Question, String> questtext;

	@FXML
	private TableColumn<Question, String> questtopic;

	ObservableList<Question> data;

	@FXML
	void cancel_QuestionBankController(ActionEvent event) throws Exception {

		System.out.println("back");
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/LectureHomePage.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Lecture Home Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void search_QuestionBankController(ActionEvent event) throws Exception {

		String searchtext = searchtext_QuestionBankController.getText();
		MessagesClass msg = new MessagesClass(Messages.SearchQuestion, searchtext);
		ClientUI.chat.accept(msg);
		data = FXCollections.observableArrayList(ChatClient.questionsearchlist);
		questionTbl.setItems(data);
	}

	@FXML
	void addquest_QuestionBankController(ActionEvent event) throws Exception {
		questionTbl.getSelectionModel().clearSelection();
		System.out.println("add question");
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/AddQuestion.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Add Question Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void deletequest_QuestionBankController(ActionEvent event) throws Exception {

		ObservableList<Question> AllQuestions, SingleQuestion;
		AllQuestions = questionTbl.getItems();
		SingleQuestion = questionTbl.getSelectionModel().getSelectedItems();

		if (SingleQuestion.size() == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Error");
			alert.setContentText("Ooops, You must choose an question!");
			alert.showAndWait();
		} else {
			MessagesClass msg = new MessagesClass(Messages.RemoveQuestion, SingleQuestion.get(0));
			ClientUI.chat.accept(msg);
			msg = new MessagesClass(Messages.OpenQuestionBank, null);
			ClientUI.chat.accept(msg);
			initialize(location, resources);
			SingleQuestion.forEach(AllQuestions::remove);
		}

	}

	@FXML
	void editquest_QuestionBankController(ActionEvent event) throws Exception {

		ObservableList<Question> SingleQuestion;
		SingleQuestion = questionTbl.getSelectionModel().getSelectedItems();
		if (SingleQuestion.size() == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Error");
			alert.setContentText("Ooops, You must choose an question!");
			alert.showAndWait();
		} else {
			MessagesClass msg = new MessagesClass(Messages.GetEditQuestionData, SingleQuestion.get(0));
			ClientUI.chat.accept(msg);

			System.out.println("edit question");
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.close();
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/gui/EditQuestion.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setTitle("edit Question Page");
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
				idquest.setCellValueFactory(new PropertyValueFactory<Question, String>("idquest"));
				questtext.setCellValueFactory(new PropertyValueFactory<Question, String>("questtext"));
				questtopic.setCellValueFactory(new PropertyValueFactory<Question, String>("questtopic"));

				data = FXCollections.observableArrayList(ChatClient.questionslist);
				questionTbl.setItems(data);
			}

		});

	}
}
