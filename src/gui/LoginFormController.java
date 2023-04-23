package gui;

import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Messages;
import models.MessagesClass;
import models.User;

public class LoginFormController {
	ActionEvent loginevent;
	@FXML
	private TextField EnterUserName_LoginForm;

	@FXML
	private TextField EnterPassword_LoginForm;

	@FXML
	private Button Loginbtn_LoginForm;

	@FXML
	private Button Exitbtn_LoginForm;

	@FXML
	void Exitbtn_LoginForm(ActionEvent event) {
		ClientUI.chat.client.quit();
		System.out.println("Login Exit");
		System.exit(0);
	}

	@FXML
	void LoginBtn_LoginForm(ActionEvent event) throws Exception {
		//MessagesClass msg = new MessagesClass(Messages.Login,null);
		
		ChatClient.user=null;
				if (EnterUserName_LoginForm.getText().equals("")) {
					if (EnterPassword_LoginForm.getText().equals("")) {
						loginDetailsError();
					}
				} else {
					
					User user = new User(null,null);
					user.setUserName((String)EnterUserName_LoginForm.getText());
					user.setPassword((String)EnterPassword_LoginForm.getText());
					MessagesClass msg = new MessagesClass(Messages.Login,user);
					ClientUI.chat.accept(msg);
					
					if (ChatClient.user != null) {
						System.out.println("logged in");
						FXMLLoader loader = new FXMLLoader();
						Stage primaryStage = new Stage();
						((Node)event.getSource()).getScene().getWindow().hide();
						
						switch (ChatClient.user.getRole()) {// open the window depend on the role student/lecture/principal

						case "1":
							Pane root1 = loader.load(getClass().getResource("/gui/LectureHomePage.fxml").openStream());
							Scene scene1 = new Scene(root1);
							primaryStage.setTitle("Lecture Home Page");
							primaryStage.setScene(scene1);
							primaryStage.show();
							break;

						case "2":
							Pane root2 = loader.load(getClass().getResource("/gui/StudentHomePage.fxml").openStream());
							Scene scene2 = new Scene(root2);
							primaryStage.setTitle("Student Home Page");
							primaryStage.setScene(scene2);
							primaryStage.show();
							break;

						case "3":												
							Pane root3 = loader.load(getClass().getResource("/gui/PrincipalHomePage.fxml").openStream());
							Scene scene3 = new Scene(root3);
							primaryStage.setTitle("Principal Home Page");
							primaryStage.setScene(scene3);
							primaryStage.show();
							break;
						}
					}
				}
	}

	private void loginDetailsError() {
		System.out.println("Wrong input try again");

	}

	public void start(Stage primaryStage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("/gui/LoginForm.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Login");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
