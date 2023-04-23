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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Exam;
import models.Messages;
import models.MessagesClass;
import models.QuestForExam;
import models.Question;

public class AddQuestionToExamController implements Initializable {

	@FXML
	private Button search_AddQuestionToExamController;

	@FXML
	private Button cancel_AddQuestionToExamController;

	@FXML
	private Button addquestion_AddQuestionToExamController;

	@FXML
	private TextField searchtext_AddQuestionToExamController;

	@FXML
	Label Topic_AddQuestionToExamController;

	@FXML
	TableView<Question> questionsTbl;

	@FXML
	private TableColumn<Question, String> idquest;

	@FXML
	private TableColumn<Question, String> questtext;

	@FXML
	private TableColumn<Question, String> teachername;

	ObservableList<Question> data;

	@FXML
	void cancel_AddQuestionToExamController(ActionEvent event) throws Exception {

		if (ChatClient.examtoedit == null) {
			System.out.println("back");
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.close();
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/gui/AddExam.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setTitle("Add Exam Page");
			primaryStage.setScene(scene);
			primaryStage.show();
		} else {
			System.out.println("back");
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.close();
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/gui/EditExam.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setTitle("Edit Exam Page");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	@FXML
	void search_AddQuestionToExamController(ActionEvent event) throws Exception {

		String searchtext = searchtext_AddQuestionToExamController.getText();
		MessagesClass msg = new MessagesClass(Messages.SearchQuestion, searchtext);
		ClientUI.chat.accept(msg);
		data = FXCollections.observableArrayList(ChatClient.questionsearchlist);
		questionsTbl.setItems(data);
	}

	@FXML
	void addquestion_AddQuestionToExamController(ActionEvent event) throws Exception {

		boolean flag = true;
		ObservableList<Question> SingleQuestion;
		SingleQuestion = questionsTbl.getSelectionModel().getSelectedItems();

		if (SingleQuestion.size() == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Error");
			alert.setContentText("Ooops, You must choose an question!");
			alert.showAndWait();
		} else {
			if (ChatClient.examtoedit == null) {
				for (QuestForExam a : AddExamController.questions) {
					if (SingleQuestion.get(0).getIdquest().equals(a.getQuestid())) {
						flag = false;
					}
				}
			} else {
				for (QuestForExam a : EditExamController.questions) {
					if (SingleQuestion.get(0).getIdquest().equals(a.getQuestid())) {
						flag = false;
					}
				}
			}

			if (flag == true) {
				QuestForExam questforexam = new QuestForExam(SingleQuestion.get(0).getIdquest(), null,
						ChatClient.user.getID());
				if (ChatClient.examtoedit == null) {
					AddExamController.questions.add(questforexam);
					System.out.println("back");
					Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					stage.close();
					FXMLLoader loader = new FXMLLoader();
					Stage primaryStage = new Stage();
					Pane root = loader.load(getClass().getResource("/gui/AddExam.fxml").openStream());
					Scene scene = new Scene(root);
					primaryStage.setTitle("Add Exam Page");
					primaryStage.setScene(scene);
					primaryStage.show();
				} else {
					EditExamController.questions.add(questforexam);
					System.out.println("back");
					Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					stage.close();
					FXMLLoader loader = new FXMLLoader();
					Stage primaryStage = new Stage();
					Pane root = loader.load(getClass().getResource("/gui/EditExam.fxml").openStream());
					Scene scene = new Scene(root);
					primaryStage.setTitle("Edit Exam Page");
					primaryStage.setScene(scene);
					primaryStage.show();
				}
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText("Error");
				alert.setContentText("Ooops, these question already exists!");

				alert.showAndWait();
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		javafx.application.Platform.runLater(new Runnable() {
			@Override
			public void run() {
				idquest.setCellValueFactory(new PropertyValueFactory<Question, String>("idquest"));
				questtext.setCellValueFactory(new PropertyValueFactory<Question, String>("questtext"));
				teachername.setCellValueFactory(new PropertyValueFactory<Question, String>("teachername"));

				MessagesClass msg = new MessagesClass(Messages.GetQuestionswithtopic, ChatClient.Topic);
				ClientUI.chat.accept(msg);
				data = FXCollections.observableArrayList(ChatClient.questionslist);
				questionsTbl.setItems(data);

				Topic_AddQuestionToExamController.setText(ChatClient.Topic);
			}

		});
	}
}
