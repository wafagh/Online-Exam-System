package gui;

import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Course;
import models.Exam;
import models.Messages;
import models.MessagesClass;
import models.QuestForExam;
import models.Question;
import models.Topic;

public class EditExamController implements Initializable {

	private URL location;
	private ResourceBundle resources;

	@FXML
	Label ExamID_EditExamController;

	@FXML
	private Button confirm_EditExamController;

	@FXML
	private Button cancel_EditExamController;

	@FXML
	private Button examcomments_EditExamController;

	@FXML
	private Button addquestion_EditExamController;

	@FXML
	private Button deletequestion_EditExamController;

	@FXML
	Label Topic_EditExamController;

	@FXML
	private ChoiceBox<String> Course_EditExamController;

	@FXML
	private TextField examtime_EditExamController;

	@FXML
	TableView<Question> questionsTbl;

	@FXML
	private TableColumn<Question, String> idquest;

	@FXML
	private TableColumn<Question, String> questtext;

	ObservableList<Topic> topicdata;

	ObservableList<Course> coursedata;

	ObservableList<Question> data;

	public static ArrayList<QuestForExam> questions = new ArrayList<>();

	public static ArrayList<Question> questionsfortable = new ArrayList<>();

	public static String commentsforstudent;

	public static String commentsforteacher;

	@FXML
	void examcomments_EditExamController(ActionEvent event) throws Exception {

		System.out.println("back");
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/ExamComments.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Exam Comments Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void cancel_EditExamController(ActionEvent event) throws Exception {

		ChatClient.Course = null;
		ChatClient.Time = null;
		ChatClient.examtoedit = null;
		questions = new ArrayList<>();
		questionsfortable = new ArrayList<>();
		System.out.println("back");
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/ExamBank.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Exam Bank Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void deletequestion_EditExamController(ActionEvent event) throws Exception {

		ObservableList<Question> AllQuestions, SingleQuestion;
		AllQuestions = questionsTbl.getItems();
		SingleQuestion = questionsTbl.getSelectionModel().getSelectedItems();

		if (SingleQuestion.size() == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Error");
			alert.setContentText("Ooops, You must choose an question!");
			alert.showAndWait();
		} else {
			if (AllQuestions.size() > 1) {
				for (int i = 0; i < questions.size(); i++) {
					if (questions.get(i).getQuestid().equals(SingleQuestion.get(0).getIdquest())) {
						questions.remove(questions.get(i));
					}
				}
				for (int i = 0; i < questionsfortable.size(); i++) {
					if (questionsfortable.get(i).getIdquest().equals(SingleQuestion.get(0).getIdquest())) {
						questionsfortable.remove(questionsfortable.get(i));
					}
				}
				initialize(location, resources);
				SingleQuestion.forEach(AllQuestions::remove);
			} else {
				questions = new ArrayList<>();
				questionsfortable = new ArrayList<>();
				data = FXCollections.observableArrayList(questionsfortable);
				questionsTbl.setItems(data);
			}
		}
	}

	@FXML
	void addquestion_EditExamController(ActionEvent event) throws Exception {

		MessagesClass msg = new MessagesClass(Messages.GetTopic, Topic_EditExamController.getText());
		ClientUI.chat.accept(msg);

		msg = new MessagesClass(Messages.GetCourse, Course_EditExamController.getValue());
		ClientUI.chat.accept(msg);

		msg = new MessagesClass(Messages.GetTime, examtime_EditExamController.getText());
		ClientUI.chat.accept(msg);

		System.out.println("add question to edit exam");
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/AddQuestionToExam.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setTitle("Add Question To Edit Exam Page");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@FXML
	void confirm_EditExamController(ActionEvent event) throws Exception {

		if (questions.size() == 0 || Course_EditExamController.getValue() == null
				|| examtime_EditExamController.getText().equals("")) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Error");
			alert.setContentText("Ooops, you have missing data!");

			alert.showAndWait();
		} else {

			String examnum = null;
			String topicvalue = (String) Topic_EditExamController.getText();
			String coursevalue = (String) Course_EditExamController.getValue();
			for (Topic a : ChatClient.topiceslist) {
				if (a.getTopic().equals(topicvalue)) {
					for (Course b : ChatClient.courseslist) {
						if (b.getCourse().equals(coursevalue)) {
							examnum = a.getTopicID() + b.getCourseID() + (ChatClient.examtoedit.getExamid());
						}
					}
				}
			}

			int gradeperquestion = (int) Math.ceil((float) 100 / (float) questions.size());
			Exam newexam = new Exam(ChatClient.examtoedit.getExamid(), ChatClient.examtoedit.getTopic(),
					Course_EditExamController.getValue(), "" + gradeperquestion, examtime_EditExamController.getText(),
					ChatClient.examtoedit.getLecture(), questions.size());
			newexam.setExamnumber(examnum);
			newexam.setCommentforstudent(commentsforstudent);
			newexam.setCommentforteacher(commentsforteacher);

			MessagesClass msg = new MessagesClass(Messages.EditExam, newexam);
			ClientUI.chat.accept(msg);

			msg = new MessagesClass(Messages.RemoveExamQuestions, ChatClient.examtoedit.getExamid());
			ClientUI.chat.accept(msg);

			for (QuestForExam a : questions) {
				a.setExamid(ChatClient.examtoedit.getExamid());
				msg = new MessagesClass(Messages.AddQuestionToExam, a);
				ClientUI.chat.accept(msg);
			}

			ChatClient.Topic = null;
			ChatClient.Course = null;
			ChatClient.Time = null;
			ChatClient.examtoedit = null;

			questions = new ArrayList<>();
			questionsfortable = new ArrayList<>();
			msg = new MessagesClass(Messages.OpenExamBank, null);
			ClientUI.chat.accept(msg);
			System.out.println("confirm");
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.close();
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/gui/ExamBank.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setTitle("Exam Bank Page");
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

				questionsfortable = new ArrayList<>();
				MessagesClass msg = new MessagesClass(Messages.FillTopicChoiceBox, null);
				ClientUI.chat.accept(msg);
				msg = new MessagesClass(Messages.FillCourseChoiceBox, null);
				ClientUI.chat.accept(msg);

				coursedata = FXCollections.observableArrayList(ChatClient.courseslist);
				for (int i = 0; i < coursedata.size(); i++) {
					Course_EditExamController.getItems().add(coursedata.get(i).getCourse());
				}

				if (ChatClient.Course != null) {
					Course_EditExamController.setValue(ChatClient.Course);
				}

				if (ChatClient.Time != null) {
					examtime_EditExamController.setText(ChatClient.Time);
				}

				ExamID_EditExamController.setText(ChatClient.examtoedit.getExamnumber());
				Topic_EditExamController.setText(ChatClient.examtoedit.getTopic());
				examtime_EditExamController.setText(ChatClient.examtoedit.getTime());
				Course_EditExamController.setValue(ChatClient.examtoedit.getCourse());

				idquest.setCellValueFactory(new PropertyValueFactory<Question, String>("idquest"));
				questtext.setCellValueFactory(new PropertyValueFactory<Question, String>("questtext"));

				if (questions.size() != 0) {
					for (int i = 0; i < questions.size(); i++) {
						for (int j = 0; j < ChatClient.questionslist.size(); j++) {
							if (questions.get(i).getQuestid().equals(ChatClient.questionslist.get(j).getIdquest())) {
								questionsfortable.add(ChatClient.questionslist.get(j));
							}
						}
					}
					data = FXCollections.observableArrayList(questionsfortable);
					questionsTbl.setItems(data);
				} else {

					for (int j = 0; j < ChatClient.questionslist.size(); j++) {
						questionsfortable.add(ChatClient.questionslist.get(j));

						QuestForExam questforexam = new QuestForExam(ChatClient.questionslist.get(j).getIdquest(),
								ChatClient.examtoedit.getExamid(), ChatClient.user.getID());
						questions.add(questforexam);
					}
					data = FXCollections.observableArrayList(questionsfortable);
					questionsTbl.setItems(data);
				}
			}
		});
	}

}
