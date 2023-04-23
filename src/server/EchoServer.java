// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package server;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import common.MyFile;
import gui.ExamInProgressController;
import models.AnswersReview;
import models.Course;
import models.Exam;
import models.ExamDone;
import models.ExamInProgressStudent;
import models.ExamsInProgress;
import models.Question;
import models.ReportExam;
import models.RequestsForTime;
import models.StudentsResults;
import models.Topic;
import models.Messages;
import models.MessagesClass;
import models.QuestForExam;
import models.User;
import models.clientsRequest;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */

public class EchoServer extends AbstractServer {
	private ArrayList<MessagesClass> messagerequests = new ArrayList<MessagesClass>();
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	// final public static int DEFAULT_PORT = 5555;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 * 
	 */

	public EchoServer(int port) {
		super(port);
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 * @param
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {

		System.out.println("Message received: " + ((MessagesClass) msg).getMsgType() + " from " + client);
		MessagesClass message = (MessagesClass) msg;
		ArrayList<ExamsInProgress> examslist = new ArrayList<>();
		ArrayList<clientsRequest> requestsClients = new ArrayList<clientsRequest>();
		User user = null;

		try {

			switch (message.getMsgType()) {
			//// genegral//////
			case Login:

				user = (User) message.getMsgData();
				System.out.println(user.getUserName() + " 0");////////////////////////////////////////////
				if (DBO.authLogIn(user) != null) {
					System.out.println(user.getUserName() + " 1");////////////////////////////////////////////
					message = new MessagesClass(Messages.loginSucceeded, DBO.authLogIn(user));
					DBO.updateClientStatus(user, 1);
					client.sendToClient(message);
					// UpdateClient(client.getInetAddress().getLocalHost(),
					// client.getInetAddress().getHostName(), "Connected");

					// DBO.user = null;// make it null again WE DONT NEED TO USE it so free it

				} else {
					message = new MessagesClass(Messages.loginerror, null);
					client.sendToClient(message);
				}

				break;
			case Disconnected:
				System.out.println("do i am getting here");

				clientDisconnected(client);

				break;
			case updateStatusOfLogIn:
				// update the client status to 0 -> that mean he log out
				if ((User) message.getMsgData() != null)
					DBO.updateClientStatus((User) message.getMsgData(), 0);
				client.sendToClient(new MessagesClass(Messages.LogedOut, null));

				break;
			////////////// principal Start///////////////
			case getRequestsToPrincipal:
				DBO.getTheRequestList();
				if (DBO.requestsList != null) {

					client.sendToClient(new MessagesClass(Messages.theListRequestsToPrinc, DBO.requestsList));
//				System.out.println(DBO.requestsList.toString());

				} else {
					// if there no list we also should sent message
					client.sendToClient(new MessagesClass(Messages.noListHere, null));
				}

				break;
			case getReportByStudent:
				ArrayList<ReportExam> reportsByStudent = new ArrayList<>();
				reportsByStudent = DBO.getReportByStudent((String) message.getMsgData());
				if (reportsByStudent != null) {
					for (ReportExam R : reportsByStudent)// calc the avearge + median + StandDeviation
					{
						R.calcAverage();
						R.calcmedianX();
						R.calcStandDeviation();
						System.out.println(R.getAverage() + " " + R.getMedianX() + " " + R.getStandDeviation());
					}
					client.sendToClient(new MessagesClass(Messages.getReportByStudent, reportsByStudent));

				} else
					client.sendToClient(new MessagesClass(Messages.getReportByStudent, null));
				break;
			case getReportByLecturer:
				ArrayList<ReportExam> reportsByLecturer = new ArrayList<>();
				reportsByLecturer = DBO.getReportByLecturer((String) message.getMsgData());
				if (reportsByLecturer != null) {
					for (ReportExam R : reportsByLecturer)// calc the avearge + median + StandDeviation
					{
						R.calcAverage();
						R.calcmedianX();
						R.calcStandDeviation();
						System.out.println("is you writing this" + R.getAverage() + " " + R.getMedianX() + " "
								+ R.getStandDeviation());
					}
					client.sendToClient(new MessagesClass(Messages.getReportByLecturer, reportsByLecturer));

				} else
					client.sendToClient(new MessagesClass(Messages.getReportByLecturer, null));
				break;
			case getNameOfTheStudentReport:
				String s = DBO.getUserFullName((String) message.getMsgData());
				if (s != null)
					client.sendToClient(new MessagesClass(Messages.getNameOfTheStudentReport, s));
				else {
					client.sendToClient(new MessagesClass(Messages.getNameOfTheStudentReport, null));
				}
				break;
			case getReportByCourse:
				ArrayList<ReportExam> reportsByCourse = new ArrayList<>();
				reportsByCourse = DBO.getReportByCourse((String) message.getMsgData());
				if (reportsByCourse != null) {
					for (ReportExam R : reportsByCourse)// calc the avearge + median + StandDeviation
					{
						R.calcAverage();
						R.calcmedianX();
						R.calcStandDeviation();
						System.out.println(R.getAverage() + " " + R.getMedianX() + " " + R.getStandDeviation());
					}
					client.sendToClient(new MessagesClass(Messages.getReportByCourse, reportsByCourse));

				} else
					client.sendToClient(new MessagesClass(Messages.getReportByCourse, null));
				break;
			case confirmTheRequest:
				for (MessagesClass a : messagerequests) {
					if (((RequestsForTime) a.getMsgData()).getExam_id()
							.equals(((RequestsForTime) message.getMsgData()).getExam_id())) {
						message = new MessagesClass(Messages.confirmTheRequest, a.getMsgData(), a.getMsgData1());
					}
				}
				RequestsForTime confirmation = (RequestsForTime) message.getMsgData();

				sendToAllClients(message);
				System.out.println("i got here");
				client.sendToClient(new MessagesClass(null, null));// to free the princpial controller - > chatClient
				break;
////////////////////Principal End////////////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////Lecturer Start //////////////////////////////////////////////////////////////////////////////////////////////////////
			case OpenExamBank:
				System.out.println("Message received: " + msg + " from " + client);

				ArrayList<Exam> examlist = new ArrayList<>();

				if (DBO.getAllExams() != null) {
					examlist = DBO.getAllExams();
					message = new MessagesClass(Messages.OpenExamBank, examlist);
				} else {
					message = new MessagesClass(Messages.OpenExamBank, null);
				}

				try {

					client.sendToClient(message);
				} catch (IOException e) {

					e.printStackTrace();
				}
				break;
			case OpenedExamInProgressForm:

				System.out.println("Message received: " + msg + " from " + client);

				examslist = DBO.getAllExamsInProgress((User) message.getMsgData());
				if (examslist != null) {
					message = new MessagesClass(Messages.OpenedExamInProgressForm, examslist);
				} else {
					message = new MessagesClass(Messages.OpenedExamInProgressForm, null);
				}

				client.sendToClient(message);

				break;
			case SentTimeRequest:
				RequestsForTime req;
				req = (RequestsForTime) message.getMsgData();
				// requestsClients.add(new clientsRequest(client, req));
				messagerequests.add(message);
				if (DBO.addrequest(req) == false) {
					System.out.println("Couldnt insert the request");
					// message = new MessagesClass(Messages., null);
					message = new MessagesClass(Messages.CouldntAddRequest, null);
				} else {
					System.out.println("added the request!");
					message = new MessagesClass(Messages.AddedTheRequest, req);
				}
				client.sendToClient(message);
				break;
			case UpdateStudentsExam:
				sendToAllClients(message);
				break;
			case LockUnlock:

				System.out.println("locking/Unlocking exam");

				ExamsInProgress exam = (ExamsInProgress) message.getMsgData();
				System.out.println(exam.getStatus());
				if (exam.getStatus().equals("Locked")) {
					if (DBO.UnlockExam(exam) == true) {
						System.out.println("Exam has been Locked");

					} else
						System.out.println("Exam has been Locked-error");

				} else {
					if (DBO.LockExam(exam) == true) {
						System.out.println("Exam has been unLocked");
					}

					else
						System.out.println("Exam has been unLocked-error");
				}
				message = new MessagesClass(Messages.LockUnlock, exam);
				sendToAllClients(message);
				break;

			case OpenExamreportlecturer:
				ArrayList<ExamsInProgress> examslist1;
				examslist1 = new ArrayList<>();
				examslist1 = DBO.getAllExamsInProgressByTeacher((User) message.getMsgData());

				if (examslist1 != null) {

					message = new MessagesClass(Messages.OpenExamreportlecturer, examslist1);
				} else {
					message = new MessagesClass(Messages.OpenExamreportlecturer, null);
				}

				client.sendToClient(message);

				break;

			case SearchExam:
				System.out.println("Message received: " + msg + " from " + client);

				ArrayList<Exam> examsearchlist = new ArrayList<>();
				String searchexamtext = (String) message.getMsgData();
				if (searchexamtext.equals("")) {
					examsearchlist = DBO.getAllExams();
					message = new MessagesClass(Messages.SearchExam, examsearchlist);
				} else {
					if (DBO.SearchExam(searchexamtext) != null) {
						examsearchlist = DBO.SearchExam(searchexamtext);
						message = new MessagesClass(Messages.SearchExam, examsearchlist);
					} else {
						message = new MessagesClass(Messages.SearchExam, null);
					}
				}
				try {

					client.sendToClient(message);
				} catch (IOException e) {

					e.printStackTrace();
				}
				break;
			case RemoveExam:
				System.out.println("Message received: " + msg + " from " + client);

				Exam examtoremove = (Exam) message.getMsgData();
				if (DBO.RemoveExam(examtoremove) != false) {
					message = new MessagesClass(Messages.ExamDeletedSuccesfully, null);
				} else {
					message = new MessagesClass(Messages.ExamDeletedUnsuccesfully, null);
				}

				try {
					client.sendToClient(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			case RemoveExamQuestions:
				System.out.println("Message received: " + msg + " from " + client);

				String examID = (String) message.getMsgData();
				if (DBO.RemoveExamQuestions(examID) != false) {
					message = new MessagesClass(Messages.RemoveExamQuestions, null);
				} else {
					message = new MessagesClass(Messages.RemoveExamQuestions, null);
				}

				try {
					client.sendToClient(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case GetTopic:
				System.out.println("Message received: " + msg + " from " + client);

				message = new MessagesClass(Messages.GetTopic, (String) message.getMsgData());
				try {
					client.sendToClient(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			case GetCourse:
				System.out.println("Message received: " + msg + " from " + client);

				message = new MessagesClass(Messages.GetCourse, (String) message.getMsgData());
				try {
					client.sendToClient(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			case GetTime:
				System.out.println("Message received: " + msg + " from " + client);

				message = new MessagesClass(Messages.GetTime, (String) message.getMsgData());
				try {
					client.sendToClient(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			case GetQuestionswithtopic:
				System.out.println("Message received: " + msg + " from " + client);

				ArrayList<Question> questions = new ArrayList<>();
				String topicforquestion = (String) message.getMsgData();
				if (DBO.GetQuestionswithtopic(topicforquestion) != null) {
					questions = DBO.GetQuestionswithtopic(topicforquestion);
					message = new MessagesClass(Messages.GetQuestionswithtopic, questions);
				} else {
					message = new MessagesClass(Messages.GetQuestionswithtopic, null);
				}

				try {
					client.sendToClient(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			case FillCourseChoiceBox:
				System.out.println("Message received: " + msg + " from " + client);

				ArrayList<Course> courseslist = new ArrayList<>();

				if (DBO.getAllCourses() != null) {
					courseslist = DBO.getAllCourses();
					message = new MessagesClass(Messages.FillCourseChoiceBox, courseslist);
				} else {
					message = new MessagesClass(Messages.FillCourseChoiceBox, null);
				}

				try {

					client.sendToClient(message);
				} catch (IOException e) {

					e.printStackTrace();
				}
				break;

			case AddNewExam:
				System.out.println("Message received: " + msg + " from " + client);

				Exam newexam = (Exam) message.getMsgData();
				if (DBO.AddExam(newexam) != null) {
					message = new MessagesClass(Messages.AddNewExam, DBO.getAllExams());
				} else {
					message = new MessagesClass(Messages.AddNewExam, DBO.getAllExams());
				}
				try {

					client.sendToClient(message);
				} catch (IOException e) {

					e.printStackTrace();
				}
				break;

			case AddQuestionToExam:
				System.out.println("Message received: " + msg + " from " + client);

				QuestForExam newquestforexam = (QuestForExam) message.getMsgData();
				if (DBO.AddQuestionToExam(newquestforexam) != false) {
					message = new MessagesClass(Messages.AddQuestionToExam, null);
				} else {
					message = new MessagesClass(Messages.AddQuestionToExam, null);
				}
				try {

					client.sendToClient(message);
				} catch (IOException e) {

					e.printStackTrace();
				}
				break;

			case GetEditExamData:
				System.out.println("Message received: " + msg + " from " + client);

				Exam examtoedit = (Exam) message.getMsgData();
				message = new MessagesClass(Messages.GetEditExamData, examtoedit);

				try {
					client.sendToClient(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			case GetEditExamQuestions:
				System.out.println("Message received: " + msg + " from " + client);

				String exameditID = (String) message.getMsgData();
				ArrayList<Question> exameditquestionlist = new ArrayList<>();
				if (DBO.GetEditExamQuestions(exameditID) != null) {
					exameditquestionlist = DBO.GetEditExamQuestions(exameditID);
					message = new MessagesClass(Messages.GetEditExamQuestions, exameditquestionlist);
				} else {
					message = new MessagesClass(Messages.GetEditExamQuestions, exameditquestionlist);
				}

				try {
					client.sendToClient(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			case EditExam:
				System.out.println("Message received: " + msg + " from " + client);

				Exam editexam = (Exam) message.getMsgData();
				if (DBO.EditExam(editexam) != false) {
					message = new MessagesClass(Messages.EditExam, null);
				} else {
					message = new MessagesClass(Messages.EditExam, null);
				}

				try {
					client.sendToClient(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			case OpenQuestionBank:
				System.out.println("Message received: " + msg + " from " + client);

				ArrayList<Question> questionlist = new ArrayList<>();

				if (DBO.getAllQuestions() != null) {
					questionlist = DBO.getAllQuestions();
					message = new MessagesClass(Messages.OpenQuestionBank, questionlist);
				} else {
					message = new MessagesClass(Messages.OpenQuestionBank, null);
				}

				try {

					client.sendToClient(message);
				} catch (IOException e) {

					e.printStackTrace();
				}
				break;

			case RemoveQuestion:
				System.out.println("Message received: " + msg + " from " + client);

				Question quest = (Question) message.getMsgData();
				if (DBO.DeleteQuestion(quest) != false) {
					message = new MessagesClass(Messages.QuestionDeletedSuccesfully, null);
				} else {
					message = new MessagesClass(Messages.QuestionDeletedUnsuccesfully, null);
				}

				try {
					client.sendToClient(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			case SearchQuestion:
				System.out.println("Message received: " + msg + " from " + client);

				ArrayList<Question> questsearchlist = new ArrayList<>();
				String searchquesttext = (String) message.getMsgData();
				if (searchquesttext.equals("")) {
					questsearchlist = DBO.getAllQuestions();
					message = new MessagesClass(Messages.SearchQuestion, questsearchlist);
				} else {
					if (DBO.SearchQuestion(searchquesttext) != null) {
						questsearchlist = DBO.SearchQuestion(searchquesttext);
						message = new MessagesClass(Messages.SearchQuestion, questsearchlist);
					} else {
						message = new MessagesClass(Messages.SearchQuestion, null);
					}
				}
				try {

					client.sendToClient(message);
				} catch (IOException e) {

					e.printStackTrace();
				}
				break;

			case AddQuestion:
				System.out.println("Message received: " + msg + " from " + client);

				Question newquest = (Question) message.getMsgData();
				if (DBO.AddQuestion(newquest) != null) {
					message = new MessagesClass(Messages.AddQuestion, DBO.getAllQuestions());
				} else {
					message = new MessagesClass(Messages.AddQuestion, DBO.getAllQuestions());
				}
				try {

					client.sendToClient(message);
				} catch (IOException e) {

					e.printStackTrace();
				}
				break;

			case FillTopicChoiceBox:
				System.out.println("Message received: " + msg + " from " + client);

				ArrayList<Topic> topiceslist = new ArrayList<>();

				if (DBO.getAllTopices() != null) {
					topiceslist = DBO.getAllTopices();
					message = new MessagesClass(Messages.FillTopicChoiceBox, topiceslist);
				} else {
					message = new MessagesClass(Messages.FillTopicChoiceBox, null);
				}

				try {

					client.sendToClient(message);
				} catch (IOException e) {

					e.printStackTrace();
				}
				break;

			case GetEditQuestionData:
				System.out.println("Message received: " + msg + " from " + client);

				Question questtoedit = (Question) message.getMsgData();
				message = new MessagesClass(Messages.GetEditQuestionData, questtoedit);

				try {
					client.sendToClient(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			case EditQuestion:
				System.out.println("Message received: " + msg + " from " + client);

				Question editquest = (Question) message.getMsgData();
				if (DBO.EditQuestion(editquest) != false) {
					message = new MessagesClass(Messages.QuestionEditSuccesfully, editquest);
				} else {
					message = new MessagesClass(Messages.QuestionEditUnsuccesfully, null);
				}

				try {
					client.sendToClient(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case AddExamInProgress:
				if (DBO.InsertNewExamInProgress((Exam) message.getMsgData())) {
					System.out.println("added new exam in progress");
					client.sendToClient(new MessagesClass(Messages.AddExamInProgress, DBO.code));
				}

				else
					client.sendToClient(new MessagesClass(Messages.GenralError, null));
				break;
			case SaveDateAndTime:

				break;
			case UpdateStatus:
				System.out.println("Updating Status For Exam Done");
				ExamsInProgress exam2 = DBO.UpdateExamStatus((ExamsInProgress) message.getMsgData());
				if (exam2 != null) {
					System.out.println("Updated the status to done");
					sendToAllClients(new MessagesClass(Messages.ExamIsDone, exam2));
				} else {
					System.out.println("Couldnt update the status!");
					client.sendToClient(new MessagesClass(Messages.GenralError, null));
				}

				break;
			case GetExamReport:

				ReportExam report;
				report = DBO.GetExamReport((ExamsInProgress) message.getMsgData());
				try {

					if (report == null) {

						client.sendToClient(new MessagesClass(Messages.GenralError, null));
					} else {
						client.sendToClient(new MessagesClass(Messages.GetExamReport, report));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case GetExamResults:

				ArrayList<StudentsResults> examresultlist = new ArrayList<>();
				examresultlist = DBO.getExamresult((ExamsInProgress) message.getMsgData());
				try {
					if (examresultlist != null) {
						System.out.println(examresultlist.get(0).getExamid());
						client.sendToClient(new MessagesClass(Messages.GetExamResults, examresultlist));
					} else {

						client.sendToClient(new MessagesClass(Messages.GetExamResults, null));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			case GetStudentResult:
				System.out.println("im here");
				try {
					client.sendToClient(
							new MessagesClass(Messages.GetStudentResult, (ExamsInProgress) message.getMsgData()));

				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			case GetStudentanswers:
				ArrayList<AnswersReview> answerslist = new ArrayList<>();
				answerslist = DBO.GetStudentanswers((ExamsInProgress) message.getMsgData());
			
				try {
					if (answerslist != null) {
						client.sendToClient(new MessagesClass(Messages.GetStudentanswers, answerslist));
						System.out.println(answerslist.size());
					} else {
						client.sendToClient(new MessagesClass(Messages.GetStudentanswers, null));
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			case UpdateStudentResult:
				try {
					if (DBO.UpdateStudentResult((StudentsResults) message.getMsgData()) != false) {
						client.sendToClient(new MessagesClass(Messages.UpdateStudentResultSuccesfully, null));
					} else {
						client.sendToClient(new MessagesClass(Messages.UpdateStudentResultUnsuccesfully, null));
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			////// Lecturer End////
////////////////////////Student Start//////////////////////////////////////////////////////////////////////////
			case StudentCheckExamCode:
				System.out.println("Message received: " + msg + " from " + client);
				String code = (String) message.getMsgData();
				String tempstatus1 = DBO.checkExamCode(code);
				if (null != tempstatus1) {
					message = new MessagesClass(Messages.ExamCodeExist, DBO.getExambyCode(code), tempstatus1);
				} else {
					message = new MessagesClass(Messages.ExamCodeExist, null);
				}
				try {
					client.sendToClient(message);
				} catch (IOException e) {

					e.printStackTrace();
				}
				break;
			case getExamQuestionsbycode:
				System.out.println("Message received: " + msg + " from " + client);
				String code1 = (String) message.getMsgData();
				if (null != DBO.checkExamCode(code1)) {
					Exam tempexam = DBO.getExambyCode(code1);
					ArrayList<Question> tempquestions = DBO.getexamquestions(code1);

					String tempstatus = DBO.getExamInProgressStatus(code1);
					tempexam.setFirstenterflag(
							DBO.setexamfirstenterflag(tempexam.getExaminprogressid(), (String) message.getMsgData1()));
					ExamInProgressStudent examinprogress = new ExamInProgressStudent(tempexam, tempquestions, code1,
							tempstatus);
					examinprogress.firstenterflag = (tempexam.getFirstenterflag());

					message = new MessagesClass(Messages.ExamInProgressData, examinprogress);
				} else {
					message = new MessagesClass(Messages.ExamInProgressData, null);
				}
				try {
					client.sendToClient(message);
				} catch (IOException e) {

					e.printStackTrace();
				}
				break;
			case OpenGradesList:
				System.out.println("Message received: " + msg + " from " + client);
				ArrayList<ExamDone> GradesList = new ArrayList<>();
				GradesList = DBO.getStudentGradeslist(DBO.user.getID());
				if (GradesList != null) {
					System.out.println("has informations");
					message = new MessagesClass(Messages.OpenGradesList, GradesList);
				} else {
					System.out.println("Empty list");
					message = new MessagesClass(Messages.OpenGradesList, null);
				}
				try {
					client.sendToClient(message);
				} catch (IOException e) {

					e.printStackTrace();
				}
				break;
			case FinishedExam:
				boolean answer;
				ExamInProgressStudent finishedexamdetails;
				finishedexamdetails = (ExamInProgressStudent) message.getMsgData(); // get all the finished exam details
				System.out.println("Message received: " + msg + " from " + client);
				answer = DBO.ExamFinishedUpdate(finishedexamdetails); // send the finished exam details to the database
				message = new MessagesClass(Messages.FinishedExamUpdate, answer);
				try {
					client.sendToClient(message);
				} catch (IOException e) {

					e.printStackTrace();
				}
				break;
			case FinishedExamOffline:
				ExamInProgressStudent finishedexamdetails1;
				finishedexamdetails1 = (ExamInProgressStudent) message.getMsgData(); // get all the finished exam
																						// details
				System.out.println("Message received: " + msg + " from " + client);
				DBO.ExamFinishedUpdateOffline(finishedexamdetails1); // send the finished exam details to the database
				message = new MessagesClass(Messages.FinishedExamUpdateOffline, null);
				try {
					client.sendToClient(message);
				} catch (IOException e) {

					e.printStackTrace();
				}
				break;
			case CheckExam:
				String examid;
				examid = (String) message.getMsgData();
				System.out.println(examid);
				System.out.println("Message received: " + msg + " from " + client);
				DBO.getexamdonedata(examid);
				
				break;
			case readFormFile:
				System.out.println("server to chient send msg!");
				message = new MessagesClass(Messages.downloadingExamFile, null);
				client.sendToClient(message);
				break;
			case downloadingExamFile:
				MyFile file = (MyFile) message.getMsgData();
				System.out.println(file.getFileName());
				String localPath = "D:\\CEMS_Exam\\" + file.getFileName();
				try {
					File newOne = new File(localPath);
					FileOutputStream fos = new FileOutputStream(newOne);
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					bos.write(file.getMybytearray(), 0, file.getSize());
					bos.flush();
					fos.flush();
					bos.close();
					fos.close();
					message = new MessagesClass(Messages.FileSaved, localPath);
					client.sendToClient(message);
				} catch (Exception e) {
					System.out.println("Error moving data");
					e.printStackTrace();
				}
				break;
////////////////////////Student End////////////////////////////////////////////////////////////////////////////
			default:
				client.sendToClient(new MessagesClass(Messages.GenralError, null));

				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

//////end of switch loop /////////////////////////////////////////////////////////////////////////////

	public void clientConnected(ConnectionToClient client) {
		System.out.println("->Client Connected");
		try {
			// DBO.connecttodb();// start it one time per client "connect to the database"
			System.out.println("Data base connected");

			UpdateClient(client.getInetAddress().getLocalHost(), client.getInetAddress().getHostName(), "Connected");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clientDisconnected(ConnectionToClient client) {
		System.out.println("->Client DisConnected");
		try {
			UpdateClient(client.getInetAddress().getLocalHost(), client.getInetAddress().getHostName(), "Disconnected");
		} catch (java.net.UnknownHostException e) {
			e.printStackTrace();
		}
	}

	private void UpdateClient(InetAddress HostName, String IP, String Status) {

//		System.out.println("Host: " + HostName + " IP: " + IP + " Status: " + Status);

		ServerUI.aFrame.UpdateClient(HostName, IP, Status);

		System.out.println("had returend");

	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());

	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}
}
//End of EchoServer class
