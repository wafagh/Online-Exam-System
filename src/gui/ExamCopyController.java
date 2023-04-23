package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.ChatClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.AnswersReview;
import models.ExamDone;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
////change
public class ExamCopyController implements Initializable {
	ObservableList<AnswersReview> tableData;
	@FXML
    private TableView<AnswersReview> reviewAnswerTbl;

    @FXML
    private Button Cancel_ECBtn;

    @FXML
    private Label ExamCopy_courseID;

    @FXML
    private Label ExamCopy_courseName;

    @FXML
    private Label ExamCopy_grade;

    @FXML
    private Label ExamComment_lb;
    
    @FXML
    private Label ExamCopy_time;
   
    @FXML
    private TableColumn<AnswersReview,Integer>question_num ;

    @FXML
    private TableColumn<AnswersReview, String> questionContent;

    @FXML
    private TableColumn<AnswersReview, String> IsCorrectcal;

/*
  * action: click on button Exit
  * result: return back to home page
  */
    @FXML
    void cancel_btnEc(ActionEvent event) throws IOException {
    	System.out.println("closed page");
    	FXMLLoader loader = new FXMLLoader();
        Stage primaryStage = new Stage();
        ((Node)event.getSource()).getScene().getWindow().hide();// do this 3 lines for all the roles no matter what
   	 	Pane root = loader.load(getClass().getResource("/gui/Gradeslist.fxml").openStream());
   	 	Scene scene = new Scene(root);			
   	 	primaryStage.setTitle("Student Home Page");
   	 	primaryStage.setScene(scene);		
   	 	primaryStage.show();

    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		javafx.application.Platform.runLater( new Runnable() {
	           @Override
	           public void run() {
	        	   if(ChatClient.copyofExam!=null) {
	        		   
	        		   ExamCopy_courseID.setText(ChatClient.copyofExam.getExam_id());
	        		   ExamCopy_courseName.setText(ChatClient.copyofExam.getExam_course());
	        		   ExamCopy_grade.setText(ChatClient.copyofExam.getGrade() + "/ 100");
	        		   ExamCopy_time.setText(ChatClient.copyofExam.getFinishTime()+" minutes");
	        		   /*define table*/
	        		   question_num.setCellValueFactory(new PropertyValueFactory<AnswersReview, Integer>("question_serial"));
	        		   questionContent.setCellValueFactory(new PropertyValueFactory<AnswersReview, String>("questiontext"));
	        		   IsCorrectcal.setCellValueFactory(new PropertyValueFactory<AnswersReview, String>("isCorrect"));
	        		   tableData=FXCollections.observableArrayList(ChatClient.toCompareAnsList);
	        		   reviewAnswerTbl.setItems(tableData);
	        		   ExamComment_lb.setText(ChatClient.copyofExam.getComment());
	        		   // to color isCorrcet cal according to student's answer
	        		   IsCorrectcal.setCellFactory(column -> {
		        		    return new TableCell<AnswersReview, String>() {
		        		        @Override
		        		        protected void updateItem(String item, boolean empty) {
		        		            super.updateItem(item, empty);

		        		            if (item == null || empty) {
		        		                setText(null);
		        		                setStyle("");
		        		            } 
		        		            else {
		        		                // Format date.
		        		                setText(item.toString());
		        		                //if the status of the answer is correct change text's color to green
		        		                if (item.equals("Correct")) {
		        		                	 setTextFill(Color.GREENYELLOW);
		        		                    setStyle("-fx-alignment: CENTER ");
		        		                }
		        		                else {
		        		                	   //if the status of the answer is correct change text's color to red
		        		                	if (item.equals("InCorrect")) {
		        		                		 setTextFill(Color.RED);
			        		                    setStyle("-fx-alignment: CENTER ");
		        		                	}
		        		                }
		        		            }
		        		        }
		        		    };
		        		});
	        		   
	        	   }
	        	   else {
	        		   System.out.println("null object");
	        	   }
	        	   
	           }
		});
		
	}

}
