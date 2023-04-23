package gui;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.text.TableView.TableRow;

import client.ChatClient;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.ExamDone;
import models.ExamsInProgress;
import models.Messages;
import models.MessagesClass;
///change
public class StudentGradesLstController implements Initializable {
	URL location;
//	Service service = new ProcessService();
    @FXML
    private Label Label_gradeslist;

    @FXML
    private Button ExamCopy_stbtn;

    @FXML
    private Button close_stbtn;

    @FXML
    private TableView<ExamDone> studentGrade_table;

    @FXML
    private TableColumn<ExamDone, String> Examid_gradestbl;

    @FXML
    private TableColumn<ExamDone, String> course_gradestbl;

    @FXML
    private TableColumn<ExamDone, String> grade_gradestbl;

    @FXML
    private TableColumn<ExamDone, String> status_gradestbl;

    @FXML
    private Label stuName_labGL;

    @FXML
    private Label stuID_labGL;
    @FXML
    private TextArea notice_GL;

	ObservableList<ExamDone> Table_Data;//save student's exam answers
    @FXML
    /*
     * action:click on close
     * result back to home page
     */
    void Close_btnGL(ActionEvent event) throws IOException {
    	System.out.println("closed page");
    	FXMLLoader loader = new FXMLLoader();
        Stage primaryStage = new Stage();
        ((Node)event.getSource()).getScene().getWindow().hide();// do this 3 lines for all the roles no matter what
   	 	Pane root = loader.load(getClass().getResource("/gui/StudentHomePage.fxml").openStream());
   	 	Scene scene = new Scene(root);			
   	 	primaryStage.setTitle("Student Home Page");
   	 	primaryStage.setScene(scene);		
   	 	primaryStage.show();
   	 
    }

    @FXML
    /*
     * action : click on Exam copy
     * result open new window that has review answer of the student and information about the exam
     */
    void ExamCopy_btnGL(ActionEvent event) throws IOException {
    	ChatClient.copyofExam=studentGrade_table.getSelectionModel().getSelectedItem();//save information about the selected row
    	if(ChatClient.copyofExam==null)
    	{
    		System.out.println("is null");
    		notice_GL.setVisible(true);
    	}
    	else {
    		MessagesClass message=new MessagesClass(Messages.ReviewExamAnswers,ChatClient.copyofExam);//create message to server
    		ClientUI.chat.accept(message);
    		//System.out.println("DownLoading a copy... wait please\n");
    		FXMLLoader loader = new FXMLLoader();
    		Stage primaryStage = new Stage();
    		((Node)event.getSource()).getScene().getWindow().hide();// do this 3 lines for all the roles no matter what
   	 		Pane root = loader.load(getClass().getResource("/gui/ExamCopyStudent.fxml").openStream());
   	 		Scene scene = new Scene(root);			
   	 		primaryStage.setTitle("Exam Copy Page");
   	 		primaryStage.setScene(scene);		
   	 		primaryStage.show();
    	}
    	
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.location=location;
		notice_GL.setVisible(false);
		javafx.application.Platform.runLater( new Runnable() {
	           @Override
	           public void run() {
	        	   
	        	   stuName_labGL.setText(ChatClient.user.getFirstName()+ " " +ChatClient.user.getLastName());
	        	   stuID_labGL.setText(ChatClient.user.getID());
	        	   /*define table*/
	        	   Examid_gradestbl.setCellValueFactory(new PropertyValueFactory<ExamDone, String>("Exam_id"));
	        	   course_gradestbl.setCellValueFactory(new PropertyValueFactory<ExamDone, String>("Exam_course"));
	        	   grade_gradestbl.setCellValueFactory(new PropertyValueFactory<ExamDone, String>("grade"));
	        	   status_gradestbl.setCellValueFactory(new PropertyValueFactory<ExamDone, String>("exam_status"));
	        	   // updating table row with right information
	        	   Table_Data=FXCollections.observableArrayList(ChatClient.gradesListStudent);
	        	   studentGrade_table.setItems(Table_Data);
	        	   studentGrade_table.setStyle("-fx-alignment: CENTER ");
	        	   
	        	   // to color status cal according to student's status exam
	        	   status_gradestbl.setCellFactory(column -> {
	        		    return new TableCell<ExamDone, String>() {
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
	        		                if (item.equals("Failed")) {
	        		                	 setTextFill(Color.BLACK);
	        		                    setStyle("-fx-background-color: #ff6666;");
	        		                }
	        		                else {
	        		                	if (item.equals("Suspected Copying")) {
	        		                		 setTextFill(Color.BLACK);
		        		                    setStyle("-fx-background-color: #ffff99; ");
	        		                	}
		        		                else
		        		                 {
		        		                	setTextFill(Color.BLACK);
		        		                	setStyle(null);
		        		                 }
	        		                }
	        		            }
	        		        }
	        		    };
	        		});
	           }//run    
	           
			});
	
		}
	
}
