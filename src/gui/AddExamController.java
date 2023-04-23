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

public class AddExamController implements Initializable {

	private URL location;
	private ResourceBundle resources;

	@FXML
	private Button confirm_AddExamController;

	@FXML
	private Button cancel_AddExamController;

	@FXML
	private Button addquestion_AddExamController;

	@FXML
	private Button deletequestion_AddExamController;

	@FXML
	private Button examcomments_AddExamController;

	@FXML
	private ChoiceBox<String> Topic_AddExamController;

	@FXML
	private ChoiceBox<String> Course_AddExamController;

	@FXML
	private TextField examtime_AddExamController;

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
	void cancel_AddExamController(ActionEvent event) throws Exception {

		ChatClient.Topic = null;
		ChatClient.Course = null;
		ChatClient.Time = null;
		questions = new ArrayList<>();
		questionsfortable = new ArrayList<>();
		commentsforteacher = null;
		commentsforstudent = null;
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
	void examcomments_AddExamController(ActionEvent event) throws Exception {

		MessagesClass msg = new MessagesClass(Messages.GetTopic, Topic_AddExamController.getValue());
		ClientUI.chat.accept(msg);

		msg = new MessagesClass(Messages.GetCourse, Course_AddExamController.getValue());
		ClientUI.chat.accept(msg);

		msg = new MessagesClass(Messages.GetTime, examtime_AddExamController.getText());
		ClientUI.chat.accept(msg);
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
	void addquestion_AddExamController(ActionEvent event) throws Exception {

		if (questions.size() != 0) {
			if (!(questionsfortable.get(0).getQuesttopic().equals((String) Topic_AddExamController.getValue()))) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText("Error");
				alert.setContentText(
						"Ooops, you can't change the topic because there is questions in the table from the old topic!");

				alert.showAndWait();
			} else {
				MessagesClass msg = new MessagesClass(Messages.GetTopic, Topic_AddExamController.getValue());
				ClientUI.chat.accept(msg);

				msg = new MessagesClass(Messages.GetCourse, Course_AddExamController.getValue());
				ClientUI.chat.accept(msg);

				msg = new MessagesClass(Messages.GetTime, examtime_AddExamController.getText());
				ClientUI.chat.accept(msg);

				System.out.println("add question to new exam");
				FXMLLoader loader = new FXMLLoader();
				Stage primaryStage = new Stage();
				((Node) event.getSource()).getScene().getWindow().hide();// do this 3 lines for all the roles no matter
																			// what
				Pane root = loader.load(getClass().getResource("/gui/AddQuestionToExam.fxml").openStream());
				Scene scene = new Scene(root);
				primaryStage.setTitle("Add Question To New Exam Page");
				primaryStage.setScene(scene);
				primaryStage.show();
			}
		} else {
			MessagesClass msg = new MessagesClass(Messages.GetTopic, Topic_AddExamController.getValue());
			ClientUI.chat.accept(msg);

			msg = new MessagesClass(Messages.GetCourse, Course_AddExamController.getValue());
			ClientUI.chat.accept(msg);

			msg = new MessagesClass(Messages.GetTime, examtime_AddExamController.getText());
			ClientUI.chat.accept(msg);

			System.out.println("add question to new exam");
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.close();
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/gui/AddQuestionToExam.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setTitle("Add Question To New Exam Page");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	@FXML
	void deletequestion_AddExamController(ActionEvent event) throws Exception {

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
	void confirm_AddExamController(ActionEvent event) throws Exception {

		if (questions.size() == 0 || Course_AddExamController.getValue() == null
				|| examtime_AddExamController.getText().equals("")
				|| (!(questionsfortable.get(0).getQuesttopic().equals((String) Topic_AddExamController.getValue())))) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Error");
			alert.setContentText("Ooops, you have missing data!");

			alert.showAndWait();
		} else {

			String examId = null;
			String examnum = null;
			String topicvalue = (String) Topic_AddExamController.getValue();
			String coursevalue = (String) Course_AddExamController.getValue();
			for (Topic a : ChatClient.topiceslist) {
				if (a.getTopic().equals(topicvalue)) {
					for (Course b : ChatClient.courseslist) {
						if (b.getCourse().equals(coursevalue)) {
							if (ChatClient.examsbank.size() == 0) {
								examnum = a.getTopicID() + b.getCourseID() + "01";
							} else if ((Integer.valueOf(
									ChatClient.examsbank.get(ChatClient.examsbank.size() - 1).getExamid()) + 1) < 10) {
								examnum = a.getTopicID() + b.getCourseID() + "0"
										+ (Integer.valueOf(
												ChatClient.examsbank.get(ChatClient.examsbank.size() - 1).getExamid())
												+ 1);
							} else {
								examnum = a.getTopicID() + b.getCourseID()
										+ (ChatClient.examsbank.get(ChatClient.examsbank.size() - 1).getExamid() + 1);
							}
						}
					}
				}
			}

			if (ChatClient.examsbank.size() < 10) {
				examId = "0" + (ChatClient.examsbank.size() + 1);
			} else {
				examId = "" + (ChatClient.examsbank.size() + 1);
			}

			int gradeperquestion =(int) Math.ceil((float)100 / (float)questions.size());
			Exam newexam = new Exam(examId, Topic_AddExamController.getValue(), Course_AddExamController.getValue(),
					"" + gradeperquestion, examtime_AddExamController.getText(), ChatClient.user.getID(),questions.size());
			newexam.setExamnumber(examnum);
			newexam.setCommentforstudent(commentsforstudent);
			newexam.setCommentforteacher(commentsforteacher);


			MessagesClass msg = new MessagesClass(Messages.AddNewExam, newexam);
			ClientUI.chat.accept(msg);

			for (QuestForExam a : questions) {
				a.setExamid(newexam.getExamid());
				msg = new MessagesClass(Messages.AddQuestionToExam, a);
				ClientUI.chat.accept(msg);
			}

			ChatClient.Topic = null;
			ChatClient.Course = null;
			ChatClient.Time = null;
			commentsforteacher = null;
			commentsforstudent = null;
			questions = new ArrayList<>();
			questionsfortable = new ArrayList<>();
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

				topicdata = FXCollections.observableArrayList(ChatClient.topiceslist);
				for (int i = 0; i < topicdata.size(); i++) {
					Topic_AddExamController.getItems().add(topicdata.get(i).getTopic());
				}

				msg = new MessagesClass(Messages.FillCourseChoiceBox, null);
				ClientUI.chat.accept(msg);

				coursedata = FXCollections.observableArrayList(ChatClient.courseslist);
				for (int i = 0; i < coursedata.size(); i++) {
					Course_AddExamController.getItems().add(coursedata.get(i).getCourse());
				}

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
				}
				if (ChatClient.Topic != null) {
					Topic_AddExamController.setValue(ChatClient.Topic);
				}

				if (ChatClient.Course != null) {
					Course_AddExamController.setValue(ChatClient.Course);
				}

				if (ChatClient.Time != null) {
					examtime_AddExamController.setText(ChatClient.Time);
				}

			}
		});
	}
}
