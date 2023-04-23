// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import client.*;
import common.ChatIF;
import common.MyFile;
import models.Exam;
import models.ExamDone;
import models.ExamInProgressStudent;
import models.ExamsInProgress;
import models.Question;
import models.ReportExam;
import models.RequestsForTime;
import models.Messages;
import models.MessagesClass;
import models.Student;
import models.StudentsResults;
import models.Topic;
import models.User;
import models.AnswersReview;
import models.Course;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient {

	// Instance variables **********************************************
	public static User user;
	public static Student student;
	public static Exam examforstudent;
	public static ExamInProgressStudent examInProgressStudent;
	public static ArrayList<RequestsForTime> requestsList;
	public static ArrayList<ExamsInProgress> examslist;
	public static ArrayList<Question> questionslist;
	public static ArrayList<ExamDone> gradesListStudent;
	public static ArrayList<Topic> topiceslist;
	public static Question questiontoedit;
	public static int code;
	public static RequestsForTime request;
	public static ArrayList<Exam> examsbank;
	public static ArrayList<Course> courseslist;
	public static String Topic;
	public static String Course;
	public static String Time;
	public static Exam examtoedit;
	public static ReportExam ReportForExam;
	public static ArrayList<ReportExam> reportList;
	public static String nameOfTheStudent_report;
	public static ExamDone copyofExam;
	public static ArrayList<AnswersReview> toCompareAnsList;
	public static String FileExamOffline;
	public static ExamsInProgress examinprogress;
	public static ArrayList<Question> questionsearchlist;
	public static ArrayList<StudentsResults> examresultlist;
	public static ExamsInProgress studentexamconfirm;
	public static ArrayList<AnswersReview> studentanswerslist;
	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF clientUI;
	// public static ArrayList<Exam> exams;
	public static boolean awaitResponse = false;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

	public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		openConnection();

	}

	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	@SuppressWarnings("unchecked")
	public void handleMessageFromServer(Object msg) {
		awaitResponse = false;

		System.out.println("--> handleMessageFromServer");

		MessagesClass message = (MessagesClass) msg;

		switch (message.getMsgType()) {

		case loginSucceeded:
			user = (User) message.getMsgData();
//			
			break;
		case loginerror:
			// case loginstudentsuccess :
			break;
		case loginstudent:

			student = (Student) message.getMsgData();
			System.out.println(student.getFirstName());/////
			break;
/////////////////////////////////////////// START OF PRINCIPAL///////////////////////////////////
		case theListRequestsToPrinc:

			requestsList = (ArrayList<RequestsForTime>) message.getMsgData();
			break;
		case getReportByStudent:
			reportList = (ArrayList<ReportExam>) message.getMsgData();
			break;
		case getReportByLecturer:
			reportList = (ArrayList<ReportExam>) message.getMsgData();
			break;
		case getReportByCourse:
			reportList = (ArrayList<ReportExam>) message.getMsgData();
			break;
		case getNameOfTheStudentReport:// get the name of the students * Or * the lecturer to print it in the window of
										// the report
			if ((String) message.getMsgData() == null)
				nameOfTheStudent_report = "not found";
			else
				nameOfTheStudent_report = (String) message.getMsgData();

			break;
/////////////////////////// END OF PRINCIPAL ////////////////////////////////////////////////////
////////////////////START OF STUDENT//////////////////////////////////////////////////////////////
		case ExamCodeExist:
			if ((Exam) message.getMsgData() != null) {
				examforstudent = (Exam) message.getMsgData();
				examforstudent.setStatus((String) message.getMsgData1());
			} else {
				examforstudent = null;
			}
			break;

		case ExamInProgressData:
			if ((ExamInProgressStudent) message.getMsgData() != null) {
				examInProgressStudent = (ExamInProgressStudent) message.getMsgData();

			} else {
				examInProgressStudent = null;
			}
			break;
		case OpenGradesList:
			gradesListStudent = (ArrayList<ExamDone>) message.getMsgData();
			break;
		case FinishedExamUpdate:
			if ((boolean) message.getMsgData())
				System.out.println("db updated");
			else
				System.out.println("didnt update db");
			break;
		case FinishedExamUpdateOffline:
			System.out.println("db updated");
			break;
		case ReviewExamAnswers:
			toCompareAnsList = (ArrayList<AnswersReview>) message.getMsgData();
			break;
		case downloadingExamFile:
			String infoPath = "C:\\Users\\samer\\git\\CEMS_G16\\examsFiles\\Offline Exam.docx";
			MyFile file = new MyFile("Offline Exam.docx");
			String LocalfilePath = infoPath;
			System.out.println(infoPath);
			System.out.println(file.getFileName());
			try {
				File newFile = new File(LocalfilePath);
				byte[] mybytearray = new byte[(int) newFile.length()];
				FileInputStream fis = new FileInputStream(newFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				file.initArray(mybytearray.length);
				file.setSize(mybytearray.length);
				bis.read(file.getMybytearray(), 0, mybytearray.length);
				MessagesClass msgfile = new MessagesClass(Messages.downloadingExamFile, file);
				sendToServer(msgfile);
			} catch (Exception e) {
				System.out.println("Error send (Files)msg) to Server");
			}
			break;
		case FileSaved:
			FileExamOffline = (String) message.getMsgData();
			break;

//////////////////////END OF STUDENT///////////////////////////////////////////////////////////////////		
////START OF TEACHER///////////////////////////////////////////////////////////////////////////////////
		case OpenedExamInProgressForm:
			examslist = (ArrayList<ExamsInProgress>) message.getMsgData();

			break;

		case OpenExamBank:
			examsbank = (ArrayList<Exam>) message.getMsgData();

			break;

		case OpenExamreportlecturer:
			examslist = (ArrayList<ExamsInProgress>) message.getMsgData();
			
			break;
		case CouldntAddRequest:
			System.out.println("Couldnt add the request!!");
			break;
		case LockUnlock:
			if (examInProgressStudent != null) {
				examinprogress =(ExamsInProgress) message.getMsgData();
				if (user.getRole().equals("2") && examInProgressStudent.getExaminprogressid().equals(examinprogress.getId()) ) {
					if(examInProgressStudent.getStatus().equals("Locked")) {
						examInProgressStudent.setStatus("UnLocked");
					}else {
						examInProgressStudent.setStatus("Locked");
					}
				}
			}
			break;
		case AddedTheRequest:
			request = (RequestsForTime) message.getMsgData();
			break;

		case SearchExam:
			examsbank = null;
			examsbank = (ArrayList<Exam>) message.getMsgData();

			break;

		case OpenQuestionBank:
			questionslist = (ArrayList<Question>) message.getMsgData();

			break;

		case RemoveQuestion:

			break;

		case QuestionDeletedSuccesfully:

			break;

		case QuestionDeletedUnsuccesfully:

			break;

		case AddQuestion:
			questionslist = (ArrayList<Question>) message.getMsgData();

			break;

		case SearchQuestion:

			questionsearchlist = (ArrayList<Question>) message.getMsgData();

			break;

		case FillTopicChoiceBox:
			topiceslist = (ArrayList<Topic>) message.getMsgData();

		case QuestionEditSuccesfully:

			break;

		case QuestionEditUnsuccesfully:

			break;

		case GetEditQuestionData:
			questiontoedit = (Question) message.getMsgData();
			break;
		case loggedout:

			break;
		case AddExamInProgress:
			code = (int) message.getMsgData();
			break;

		case GetTopic:
			Topic = (String) message.getMsgData();

			break;

		case GetCourse:
			Course = (String) message.getMsgData();

			break;

		case GetTime:
			Time = (String) message.getMsgData();

			break;

		case GetQuestionswithtopic:
			questionslist = (ArrayList<Question>) message.getMsgData();

			break;
		case ExamDeletedSuccesfully:

			break;

		case ExamDeletedUnsuccesfully:

			break;

		case RemoveExamQuestions:

			break;

		case FillCourseChoiceBox:
			courseslist = (ArrayList<Course>) message.getMsgData();

			break;

		case GetEditExamData:
			examtoedit = (Exam) message.getMsgData();
			break;

		case GetEditExamQuestions:
			questionslist = (ArrayList<Question>) message.getMsgData();

			break;
		case AddNewExam:
			examsbank = (ArrayList<Exam>) message.getMsgData();

			break;

		case AddQuestionToExam:

			break;

		case EditExam:

			break;

		case ExamIsDone:
			if (examInProgressStudent != null) {
				examinprogress =(ExamsInProgress) message.getMsgData();
				if (user.getRole().equals("2") && examInProgressStudent.getExaminprogressid().equals(examinprogress.getId()) ) {
					examInProgressStudent.setStatus("Done");
				}
			}
			break;
		case GetExamReport:

			ReportForExam = (ReportExam) message.getMsgData();

			break;
		case confirmTheRequest:// that message received per lecutrer that request extra time
			//

			if (user.getID().equals(((RequestsForTime) message.getMsgData()).getRequesterId())) {

				System.out.println("i got here 23");
				System.out.println(examslist.size());
				for (ExamsInProgress a : examslist) {// &&
														// !(examWaitingToConfirm.getStatus().equals("Locked"))
					if(((RequestsForTime) message.getMsgData()).getExam_id().equals(a.getId()))
					{
						System.out.println(a.getStatus());
						if ((!(a.getStatus().equals("Done")))) {
							System.out.println(a.getDateTime());

							String[] str = a.getDateTime().split(" ");

							DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
							LocalDateTime now = LocalDateTime.now();
							System.out.println("1");
							System.out.println(str[1]);

							String[] timeStr = str[1].split(":");
							System.out.println(timeStr[0] + timeStr[1]);
							str = dtf.format(now).split(" ");
							System.out.println(str[1] + str[0]);
							String[] nowStr = str[1].split(":");
							System.out.println(nowStr[0] + nowStr[1]);
							int[][] timeMatrix = new int[2][2];// save the dates as int to calacaulte it
							System.out.println("2");
							timeMatrix[0][0] = (Integer.parseInt(timeStr[0]) * 60);
							timeMatrix[0][1] = Integer.parseInt(timeStr[1]);
							timeMatrix[1][0] = (Integer.parseInt(nowStr[0]) * 60);
							timeMatrix[1][1] = Integer.parseInt(nowStr[1]);
							System.out.println("2.5");

							examslist.get(examslist.indexOf(a))
									.setTemptime(timeMatrix[1][0] - timeMatrix[0][0] + timeMatrix[1][1] - timeMatrix[0][1]);
							System.out.println("3");
							examslist.get(examslist.indexOf(a))
									.setReduration(((RequestsForTime) message.getMsgData()).getReduration());
							examslist.get(examslist.indexOf(a))
									.setTotalsec(examslist.get(examslist.indexOf(a)).getTotalsec()
											- examslist.get(examslist.indexOf(a)).getTemptime()
											+ Integer.parseInt(examslist.get(examslist.indexOf(a)).getReduration())*60);
											//+(Integer.parseInt(examslist.get(examslist.indexOf(a)).getTime())
							examslist.get(examslist.indexOf(a)).setTime(String.valueOf(((Integer.parseInt(examslist.get(examslist.indexOf(a)).getTime()))+(Integer.parseInt(examslist.get(examslist.indexOf(a)).getReduration())))));
						
							System.out.println(examslist.get(examslist.indexOf(a)).getTemptime());
							System.out.println(examslist.get(examslist.indexOf(a)).getTotalsec());
							System.out.println(examslist.get(examslist.indexOf(a)).getReduration());
							System.out.println(examslist.get(examslist.indexOf(a)).getTime());
							System.out.println(a.getReduration());
							System.out.println("reduration updated");
//							
							try {// send message to send msg to all clients
								sendToServer(new MessagesClass(Messages.UpdateStudentsExam, examslist.get(examslist.indexOf(a))));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					
				}

			}
			// update status
			break;
		case GetExamResults:
			examresultlist = (ArrayList<StudentsResults>) message.getMsgData();
			break;

		case GetStudentResult:
			studentexamconfirm = (ExamsInProgress) message.getMsgData();
			break;

		case GetStudentanswers:
			studentanswerslist = (ArrayList<AnswersReview>) message.getMsgData();
			System.out.println(studentanswerslist.get(0).getStudentAnswer());
			System.out.println(studentanswerslist.get(1).getStudentAnswer());
			break;

		case UpdateStudentResultSuccesfully:

			break;

		case UpdateStudentResultUnsuccesfully:

			break;
		case UpdateStudentsExam:
			ExamsInProgress tempexam = (ExamsInProgress) message.getMsgData();;
			if(examinprogress.getId().equals(tempexam.getId())){
				if(!examInProgressStudent.getStatus().equals("Done")) {
					examInProgressStudent.setStatus("Confirmed");
					examInProgressStudent.setTotalSecounds(tempexam.getTotalsec());
				}
			}
			break;
	//////////END OF TEACHER////////////////////////////////////////////////////////////////////////////////	
		}


	}


	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */

	public void handleMessageFromClientUI(Object message) {
		// openConnection(); //in order to send more than one message

		awaitResponse = true;
		try {
			sendToServer(message);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// wait for response
		while (awaitResponse) {
			try {

				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			sendToServer(new MessagesClass(Messages.Disconnected, user));
			
			closeConnection();

		} catch (IOException e) {
			
		}
		
		//System.exit(0);
	}
}
//End of ChatClient class
