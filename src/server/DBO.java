package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.print.attribute.standard.NumberOfDocuments;

import client.ChatClient;
import models.AnswersReview;
import models.CheckExam;
import models.Course;
import models.Exam;
import models.ExamDone;
import models.ExamInProgressStudent;
import models.ExamsInProgress;
import models.QuestForExam;
import models.Question;
import models.ReportExam;
import models.RequestsForTime;
import models.Student;
import models.StudentAnswers;
import models.StudentsResults;
import models.Topic;
import models.User;

public class DBO {
	static Connection conn;
	public static User user;
	public static Student student;
	public static Exam exam;
	public static CheckExam checkexam;
	public static ArrayList<RequestsForTime> requestsList;
	public static ArrayList<Question> questionlist;
	public static StudentAnswers studentanswers;
	public static int code;
	public static ExamsInProgress examinprogress;
	public static DBO _instance = null;

	public DBO()
	{
		
	}
	public static DBO GetInstance() {
		if (_instance == null) {
			_instance = new DBO();
		}

		return _instance;
	}
	
	public void connectToDB(String dbUsername, String dbPassword) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/test?serverTimezone=IST", dbUsername,
					dbPassword);

		} catch (Exception ex) {
			System.out.println("cant enter");
		}
	}
	public static void connecttodb() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/test?serverTimezone=IST", "root", "Aa123456");

			System.out.println("SQL connection succeed");

		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	static void updateDurationdb(String examId, String time) {
		Statement stmt;
		try {
			System.out.println("getting here");
			PreparedStatement ps = conn.prepareStatement("UPDATE Test SET time= ? WHERE examid = ?");
			System.out.println("the examid is :" + examId + " and the time is :" + time);
			ps.setString(1, time);
			ps.setString(2, examId);

			if (ps.executeUpdate() == 0) {
				System.out.println("Table Update Error!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static User authLogIn(User username) throws SQLException{// check if the password and the user name is correct , if yes then send the
		// right message

		Statement stmt;
		int status;
		boolean valid = false;
		String usernameStatus;
		System.out.println("i am getting into login query");
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM users where username='" + username.getUserName()
					+ "' and password='" + username.getPassword() + "'");
			if (rs.next() == true) {
				user = new User(username.getUserName(), username.getPassword(), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(7));
				if (rs.getString(3).equals("2")) {
					user.setFaculty(rs.getString(6));
				}
				
				usernameStatus = username.getUserName();
				status = rs.getInt("userStatus");
				if (rs.getInt("userStatus") != 1)
					valid = true;

			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
// if(rs.getInt("userStatus")!=1) {

// updateClientStatus(user,1);

// }
		if (valid == true) {

			return user;
		}
		return null;

	}

	public static void updateClientStatus(User user, int status) {
		System.out.println("user status updated ...." + user.getUserName());
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement("UPDATE users SET userStatus= ? WHERE username = ?");
			ps.setInt(1, status);
			ps.setString(2, user.getUserName());

			if (ps.executeUpdate() == 0) {
				System.out.println("Table Update Error!");
			} else {
				System.out.println("update " + user.getUserName() + " status to:" + status);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void setAllStatusZero() {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement("UPDATE users SET userStatus=0");
			if (ps.executeUpdate() == 0) {
				System.out.println("Error on updating or its all zero before");
			} else
				System.out.println("updated all to zero");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

/////start of Pricnipal//////
	static boolean getTheRequestList() {// get the list of the requests that lecture asked , we need this in principal
		// gui
		requestsList = new ArrayList<RequestsForTime>();
		try {
			Statement ps = conn.createStatement();
			ResultSet RS = ps.executeQuery("SELECT * from change_time_requests");
			RequestsForTime request;
			while (RS.next()) {

				request = new RequestsForTime(RS.getInt(1), RS.getString(2), RS.getString(3), RS.getString(4),
						RS.getString(5), RS.getString(6), RS.getString(7), RS.getString(8), RS.getString(9));

				requestsList.add(request);

			}

			return true;

		} catch (SQLException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public static ArrayList<ReportExam> getReportByStudent(String id) {
		ArrayList<ReportExam> RE = new ArrayList<>();
		ReportExam examTemp = new ReportExam(null);// just intiliaze
		PreparedStatement ps1;
		PreparedStatement ps2;
		PreparedStatement examInfo;
		try {

			ps1 = conn.prepareStatement("SELECT * FROM examdone E, users u where E.studentid=u.id and E.studentid=?");
			ps1.setString(1, id);
			ps2 = conn.prepareStatement(
					"SELECT * FROM  examdone E,examinprogress eip where E.examinprogressid=? AND E.examtype <> 'Offline' AND E.submitintime <> 'No'");
			examInfo = conn.prepareStatement(
					"SELECT course,topic from exams E,examinprogress eip where eip.examinprogressid=? and E.id=eip.examid ");
			ResultSet rst_examPerStudent = ps1.executeQuery();
			ResultSet rst_ExamsForAllStudent;
			ResultSet examInfoRS;
			rst_examPerStudent.last();
			int size = rst_examPerStudent.getRow(), i = 1;
			rst_examPerStudent.beforeFirst();
			System.out.println(
					"number of the exams this student are did is :" + size + "\n" + rst_examPerStudent.toString());
			while (rst_examPerStudent.next()) {
				examInfo.setString(1, rst_examPerStudent.getString(1));// set the course id to get the info of the exam
				// like exam id and topic
				examInfoRS = examInfo.executeQuery();
				if (examInfoRS.next()) {
					examTemp = new ReportExam(rst_examPerStudent.getString(1), examInfoRS.getString("course"),
							examInfoRS.getString("topic"), rst_examPerStudent.getString("date"));

					System.out.println(examTemp.getCourse() + " " + examTemp.getDate());
				}
				ps2.setString(1, rst_examPerStudent.getString(1));
				rst_ExamsForAllStudent = ps2.executeQuery();

				i = 0;
				while (rst_ExamsForAllStudent.next()) {
					System.out.println(i++);
					System.out.println("by student query: " + rst_ExamsForAllStudent.getString(3));
					examTemp.addGrade(rst_ExamsForAllStudent.getDouble("grade"));

				}

				RE.add(examTemp);
			}

		} catch (SQLException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return RE;
	}

	public static ArrayList<ReportExam> getReportByLecturer(String id) {
		ArrayList<ReportExam> RE = new ArrayList<>();
		PreparedStatement holdTheUserName;
		PreparedStatement holdtheExamIds;
		PreparedStatement holdTheExamDetialsTemporary;
		PreparedStatement holdCourseTopic;
		try {
			holdTheUserName = conn.prepareStatement("SELECT username FROM  users where id=?");
			holdTheUserName.setString(1, id);// untill here we have the username of the lecturer we want to make report
			// to him/her
			holdtheExamIds = conn.prepareStatement(
					"select examinprogressid from examinprogress where usernameS=? and status='Done' ");
			holdTheExamDetialsTemporary = conn.prepareStatement("select * from examdone where examinprogressid = ? AND examtype <> 'Offline' AND submitintime <> 'No'");// get
			// the
			// exam
			// grades
			// and
			// details
			// +
			// date
			// if
			// i
			// want
			holdCourseTopic = conn.prepareStatement(
					"select course,topic from exams E, examinprogress EXA where E.id=EXA.examid and EXA.examinprogressid=? ");
			ResultSet usernameSet = holdTheUserName.executeQuery();// the user name
			ResultSet idsOfExamsByLecutrer;
			ResultSet tempExams;
			ResultSet courseTopic;
			if (usernameSet.next()) {
				System.out.println("user name is ->" + usernameSet.getString(1));
				holdtheExamIds.setString(1, usernameSet.getString(1));// set the username to the query to get all the
				// ids of texam this lecutrer started
				idsOfExamsByLecutrer = holdtheExamIds.executeQuery();// in this varaible will be all the ids
				int i = 0;
				boolean added = false;
				while (idsOfExamsByLecutrer.next())//
				{
					System.out.println("loading exam....");
					holdTheExamDetialsTemporary.setString(1, idsOfExamsByLecutrer.getString(1));// put the exam id in
					// progress -> to get
					// all the student in
					// this exam
					tempExams = holdTheExamDetialsTemporary.executeQuery();
					holdCourseTopic.setString(1, idsOfExamsByLecutrer.getString(1));
					courseTopic = holdCourseTopic.executeQuery();
					courseTopic.next();
					ReportExam tExam = new ReportExam(idsOfExamsByLecutrer.getString(1),
							courseTopic.getString("course"), courseTopic.getString("topic"), "");
					i = 0;
					while (tempExams.next()) {
						if (i == 0) {
							tExam.setDate(tempExams.getString("date"));
							i++;
						}
						added = true;
						tExam.addGrade(tempExams.getDouble("grade"));

					}
					if (added == true)
						RE.add(tExam);

					added = false;
				}

				return RE;
			}

		} catch (SQLException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static ArrayList<ReportExam> getReportByCourse(String CourseName) {
		ArrayList<ReportExam> RE = new ArrayList<>();
		ReportExam temporarySt;
		PreparedStatement stm_exams;
		PreparedStatement stm_examsinPorgress;
		PreparedStatement stm_students_grades;
		try {
			ResultSet examsIds;
			ResultSet examIdProgress;
			ResultSet tempRresultStudents;
			stm_exams = conn.prepareStatement("select id,topic from exams where course=?");
			stm_examsinPorgress = conn.prepareStatement(
					"select examinprogressid from examinprogress where examid=? and status='Done'  ");
			stm_students_grades = conn.prepareStatement("select grade,date from examdone where examinprogressid=? AND examtype <> 'Offline' AND submitintime <> 'No'");
			stm_exams.setString(1, CourseName);
			examsIds = stm_exams.executeQuery();
			while (examsIds.next()) {
				stm_examsinPorgress.setString(1, examsIds.getString("id"));
				examIdProgress = stm_examsinPorgress.executeQuery();
				while (examIdProgress.next()) {
					stm_students_grades.setString(1, examIdProgress.getString("examinprogressid"));
					tempRresultStudents = stm_students_grades.executeQuery();

					if (tempRresultStudents.next()) {
						temporarySt = new ReportExam(examIdProgress.getString("examinprogressid"), CourseName,
								examsIds.getString("topic"), tempRresultStudents.getString("date"));
						do {// i used do while because i want to take the values to use them before the loop
// start , so it kind of trick to take them beofre only-> like exams Date
// because it is in the examDone Query
							System.out.println("the grade is:" + tempRresultStudents.getString("grade"));
							temporarySt.addGrade(tempRresultStudents.getDouble("grade"));
						} while (tempRresultStudents.next());
						RE.add(temporarySt);
					}
				}
			}
			System.out.println("will return now");
			return RE;

		} catch (SQLException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public static String getUserFullName(String id) {
		try {
			PreparedStatement ps1 = conn.prepareStatement("SELECT firstname,lastname from users where id=?");
			ps1.setString(1, id);
			ResultSet rs = ps1.executeQuery();
			if (rs.next()) {
				System.out.println("also here" + rs.getString("firstname") + " " + rs.getString("lastname"));
				return (rs.getString("firstname") + " " + rs.getString("lastname"));
			} else
				return null;
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
	}
/////end of Pricnipal//////

// Start of Lecturer///////////////////////////////////////////////////////////////////////////////////////////////////////

	public static ArrayList<ExamsInProgress> getAllExamsInProgress(User username) {

		ArrayList<ExamsInProgress> examslist = new ArrayList<>();
		System.out.println(username.getUserName());
		try {
			PreparedStatement ps1 = conn.prepareStatement(
					"SELECT E.examinprogressid,EX.topic,EX.course,EX.time,E.reduration,E.status,E.code,E.date FROM examinprogress E ,exams EX where E.examid=EX.id AND E.usernameS=?");
			ps1.setString(1, username.getUserName());
			ResultSet rst = ps1.executeQuery();
			while (rst.next()) {
				ExamsInProgress exam = new ExamsInProgress(rst.getString(1), rst.getString(2), rst.getString(3),
						rst.getString(4), rst.getString(5), rst.getString(6), rst.getInt(7));
				exam.setDateTime(rst.getString(8));

				examslist.add(exam);
				
			}

			
			rst.next();
			
			return examslist;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	static boolean addrequest(RequestsForTime request) {
		System.out.println("adding a request");
		try {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO change_time_requests (exam_id,requesterId,requesterName,pre_duration,re_duration,topic,course,reason) VALUES (?,?,?,?,?,?,?,?)");

			ps.setString(1, request.getExam_id());
			ps.setString(2, request.getRequesterId());
			ps.setString(3, request.getRequesterName());
			ps.setString(4, request.getPreduration());
			ps.setString(5, request.getReduration());
			ps.setString(6, request.getTopic());
			ps.setString(7, request.getCourse());
			ps.setString(8, request.getReason());
			if (ps.executeUpdate() == 0) {
				System.out.println("Table Insert Error!");
				return false;
			} else {
				ps = conn.prepareStatement("UPDATE examinprogress SET status = ? WHERE examinprogressid = '"
						+ request.getExam_id() + "' ");
				ps.setString(1, "Requested");
				if (ps.executeUpdate() == 0) {
					System.out.println("Table Update Error!");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	static boolean LockExam(ExamsInProgress exam) {
		try {
			PreparedStatement ps = conn.prepareStatement(
					"UPDATE examinprogress SET status = ? WHERE examinprogressid = '" + exam.getId() + "' ");
			ps.setString(1, "Locked");
			if (ps.executeUpdate() == 0) {
				System.out.println("Table Update Error!");
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	static boolean UnlockExam(ExamsInProgress exam) {
		try {
			PreparedStatement ps = conn.prepareStatement(
					"UPDATE examinprogress SET status = ? WHERE examinprogressid = '" + exam.getId() + "' ");
			ps.setString(1, "UnLocked");
			if (ps.executeUpdate() == 0) {
				System.out.println("Table Update Error!");
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static ArrayList<Question> getAllQuestions() {

		Statement stm;
		System.out.println("I am getting questions list");
		ArrayList<Question> questionslist = new ArrayList<>();
		try {
			stm = conn.createStatement();
			String sql = "SELECT * FROM questions ";
			ResultSet rst;
			rst = stm.executeQuery(sql);
			while (rst.next()) {

				Question quest = new Question(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4),
						rst.getString(5), rst.getString(6), rst.getString(7), rst.getInt(8), rst.getString(9),
						rst.getInt(10));
				questionslist.add(quest);
			}
			return questionslist;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Boolean DeleteQuestion(Question quest) {

		Statement stm;
		System.out.println("I am deleting question ");
		try {
			stm = conn.createStatement();
			String sql = "DELETE FROM questions WHERE idquest='" + quest.getIdquest() + "' ";
			int rst;
			rst = stm.executeUpdate(sql);
			System.out.println("Rows affected: " + rst);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static ArrayList<Question> SearchQuestion(String searchtext) {

		Statement stm;
		System.out.println("I am updating question bank table ");
		ArrayList<Question> questionslist = new ArrayList<>();
		try {
			stm = conn.createStatement();
			String sql = "SELECT * FROM questions WHERE idquest='" + searchtext + "' OR topic='" + searchtext + "' ";
			ResultSet rst;
			rst = stm.executeQuery(sql);
			while (rst.next()) {

				Question quest = new Question(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4),
						rst.getString(5), rst.getString(6), rst.getString(7), rst.getInt(8), rst.getString(9),
						rst.getInt(10));
				questionslist.add(quest);
			}
			return questionslist;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Boolean AddQuestion(Question quest) {

		Statement stm;
		System.out.println("I am adding question ");
		try {
			stm = conn.createStatement();
			String sql = "INSERT INTO questions VALUES ('" + quest.getIdquest() + "', '" + quest.getQuesttext() + "', '"
					+ quest.getQuesttopic() + "', '" + quest.getAnswer1() + "', " + "'" + quest.getAnswer2() + "', '"
					+ quest.getAnswer3() + "', '" + quest.getAnswer4() + "', '" + quest.getCorrectanswer() + "', '"
					+ quest.getTeachername() + "', '" + quest.getQuestnumber() + "' )";

			int rst = stm.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static ArrayList<Topic> getAllTopices() {

		Statement stm;
		System.out.println("I am getting topices list");
		ArrayList<Topic> topiceslist = new ArrayList<>();
		try {
			stm = conn.createStatement();
			String sql = "SELECT * FROM topics ";
			ResultSet rst;
			rst = stm.executeQuery(sql);
			while (rst.next()) {

				Topic topic = new Topic(rst.getString(1), rst.getString(2));
				topiceslist.add(topic);
			}
			return topiceslist;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Boolean EditQuestion(Question quest) {

		Statement stm;
		System.out.println("I am editing question");
		try {
			stm = conn.createStatement();
			String sql = "UPDATE questions SET questtext='" + quest.getQuesttext() + "', topic='"
					+ quest.getQuesttopic() + "', answer1='" + quest.getAnswer1() + "', answer2='" + quest.getAnswer2()
					+ "'," + " answer3='" + quest.getAnswer3() + "', answer4='" + quest.getAnswer4()
					+ "', correctanswer='" + quest.getCorrectanswer() + "' WHERE idquest='" + quest.getIdquest() + "' ";
			int rst;
			rst = stm.executeUpdate(sql);
			System.out.println("Rows affected: " + rst);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean InsertNewExamInProgress(Exam exam) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		System.out.println(dtf.format(now));

		try {

			ExamsInProgress exam1 = new ExamsInProgress(null, null, null, null, null, null, 0);
			exam1.setExamid(exam.getExamid());
			exam1.setTime(exam.getTime());

			PreparedStatement ps = conn.prepareStatement("INSERT INTO codes VALUES ()");
			if (ps.executeUpdate() == 0) {
				System.out.println("Couldnt create code!");
				return false;
			}

			Statement stm = conn.createStatement();
			String sql = "SELECT * FROM codes ";
			ResultSet rst = stm.executeQuery(sql);
			while (rst.next()) {
				if (rst.isLast())
					exam1.setCode(rst.getInt(1) + 1000);
			}

			ps = conn.prepareStatement(
					"INSERT INTO examinprogress(examid,time,reduration,status,code,usernameS,date) VALUES (?,?,?,?,?,?,?)");
			ps.setString(1, exam.getExamid());
			ps.setString(2, exam.getTime());
			ps.setString(3, "0");
			ps.setString(4, "No Request");
			ps.setInt(5, exam1.getCode());
			ps.setString(6, exam.getTempLecutrer());
			ps.setString(7, dtf.format(now));
			code = exam1.getCode();
			if (ps.executeUpdate() == 0) {
				System.out.println("Table Update Error!");
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean SaveDateAndTime(String date) {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO examdone VALUES ()");
			if (ps.executeUpdate() == 0) {
				System.out.println("Couldnt create code!");
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}

	public static ExamsInProgress UpdateExamStatus(ExamsInProgress exam) {

		String str;
		try {
			System.out.println(exam.getReduration());
			PreparedStatement ps1 = conn.prepareStatement(
					"UPDATE  examinprogress SET status = ?, time = ?, reduration = ? where code= '" + code + "'");
			ps1.setString(1, "Done");
			ps1.setString(2, exam.getTime());
			ps1.setString(3, exam.getReduration());
			if (ps1.executeUpdate() == 0) {
				System.out.println("Couldnt update the status");

			}
			Statement stm = conn.createStatement();

			str = "SELECT E.examinprogressid,EX.topic,EX.course,EX.time,E.reduration,E.status,E.code,E.date FROM examinprogress E ,exams EX where E.examinprogressid='"
					+ exam.getId() + "' ";

			ResultSet rst = stm.executeQuery(str);
			if (rst.next() != false)
				;
			examinprogress = new ExamsInProgress(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4),
					rst.getString(5), rst.getString(6), rst.getInt(7));
			examinprogress.setDateTime(exam.getDateTime());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return examinprogress;
	}

	public static ArrayList<Exam> getAllExams() {

		Statement stm;
		System.out.println("I am getting exams list");
		ArrayList<Exam> examslist = new ArrayList<>();
		try {
			stm = conn.createStatement();
			String sql = "SELECT * FROM exams ";
			ResultSet rst;
			rst = stm.executeQuery(sql);
			while (rst.next()) {

				Exam exam = new Exam(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4),
						rst.getString(5), rst.getString(6), rst.getInt(7));
				exam.setExamnumber(rst.getString(8));
				exam.setCommentforteacher(rst.getString(9));
				exam.setCommentforstudent(rst.getString(10));

				examslist.add(exam);
			}
			return examslist;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<Exam> SearchExam(String searchtext) {

		Statement stm;
		System.out.println("I am updating exams bank table ");
		ArrayList<Exam> examslist = new ArrayList<>();
		try {
			stm = conn.createStatement();
			String sql = "SELECT * FROM exams WHERE id='" + searchtext + "' OR topic='" + searchtext + "' OR course='"
					+ searchtext + "' ";
			ResultSet rst;
			rst = stm.executeQuery(sql);
			while (rst.next()) {

				Exam exam = new Exam(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4),
						rst.getString(5), rst.getString(6), rst.getInt(7));
				exam.setExamnumber(rst.getString(8));
				exam.setCommentforteacher(rst.getString(9));
				exam.setCommentforstudent(rst.getString(10));

				examslist.add(exam);
			}
			return examslist;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Boolean RemoveExam(Exam exam) {

		Statement stm;
		System.out.println("I am deleting exam ");
		try {
			stm = conn.createStatement();
			String sql = "DELETE FROM exams WHERE id='" + exam.getExamid() + "' ";
			int rst;
			rst = stm.executeUpdate(sql);
			System.out.println("Rows affected: " + rst);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Boolean RemoveExamQuestions(String examID) {

		Statement stm;
		System.out.println("I am deleting exam questions ");
		try {
			stm = conn.createStatement();
			String sql = "DELETE FROM questforexam WHERE examid='" + examID + "' ";
			int rst;
			rst = stm.executeUpdate(sql);
			System.out.println("Rows affected: " + rst);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static ArrayList<Question> GetQuestionswithtopic(String searchtext) {

		Statement stm;
		System.out.println("I am geting questions table");
		ArrayList<Question> questionslist = new ArrayList<>();
		try {
			stm = conn.createStatement();
			String sql = "SELECT * FROM questions WHERE topic='" + searchtext + "' ";
			ResultSet rst;
			rst = stm.executeQuery(sql);
			while (rst.next()) {

				Question quest = new Question(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4),
						rst.getString(5), rst.getString(6), rst.getString(7), rst.getInt(8), rst.getString(9),
						rst.getInt(10));
				questionslist.add(quest);
			}
			return questionslist;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<Course> getAllCourses() {

		Statement stm;
		System.out.println("I am getting topices list");
		ArrayList<Course> courseslist = new ArrayList<>();
		try {
			stm = conn.createStatement();
			String sql = "SELECT * FROM courses ";
			ResultSet rst;
			rst = stm.executeQuery(sql);
			while (rst.next()) {

				Course course = new Course(rst.getString(1), rst.getString(2));
				courseslist.add(course);
			}
			return courseslist;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Boolean AddQuestionToExam(QuestForExam quest) {

		Statement stm;
		System.out.println("I am adding question to new exam ");
		try {
			stm = conn.createStatement();
			String sql = "INSERT INTO questforexam VALUES ('" + quest.getQuestid() + "', '" + quest.getExamid() + "', '"
					+ quest.getLecturerid() + "')";

			int rst = stm.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Boolean AddExam(Exam exam) {

		Statement stm;
		System.out.println("I am adding question ");
		try {
			stm = conn.createStatement();
			String sql = "INSERT INTO exams VALUES ('" + exam.getExamid() + "', '" + exam.getTopic() + "', '"
					+ exam.getCourse() + "', '" + exam.getGradeperquestion() + "', " + "'" + exam.getTime() + "', '"
					+ exam.getLecture() + "', '" + exam.getNumofquests() + "', '" + exam.getExamnumber() + "'" + ", '"
					+ exam.getCommentforteacher() + "', '" + exam.getCommentforstudent() + "')";

			int rst = stm.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static ArrayList<Question> GetEditExamQuestions(String exameditID) {

		int i = 0;
		Statement stm;
		System.out.println("I am geting the exam to edit questions ");
		ArrayList<QuestForExam> exameditquestionlist = new ArrayList<>();
		ArrayList<Question> questionslist = new ArrayList<>();

		try {
			stm = conn.createStatement();
			String sql = "SELECT * FROM questforexam WHERE examid='" + exameditID + "' ";
			ResultSet rst;
			rst = stm.executeQuery(sql);
			while (rst.next()) {

				QuestForExam questforexam = new QuestForExam(rst.getString(1), rst.getString(2), rst.getString(3));
				exameditquestionlist.add(questforexam);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (i = 0; i < exameditquestionlist.size(); i++) {
			try {
				stm = conn.createStatement();
				String sql1 = "SELECT * FROM questions WHERE idquest='" + exameditquestionlist.get(i).getQuestid()
						+ "' ";
				ResultSet rst1;
				rst1 = stm.executeQuery(sql1);
				while (rst1.next()) {
					Question quest = new Question(rst1.getString(1), rst1.getString(2), rst1.getString(3),
							rst1.getString(4), rst1.getString(5), rst1.getString(6), rst1.getString(7), rst1.getInt(8),
							rst1.getString(9), rst1.getInt(10));
					questionslist.add(quest);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (questionslist.size() != 0)
			return questionslist;

		return null;
	}

	public static Boolean EditExam(Exam exam) {

		Statement stm;
		System.out.println("I am editing exam");
		try {
			stm = conn.createStatement();
			String sql = "UPDATE exams SET id='" + exam.getExamid() + "', topic='" + exam.getTopic() + "', course='"
					+ exam.getCourse() + "', grade='" + exam.getGradeperquestion() + "'," + " time='" + exam.getTime()
					+ "', Lecturer_ID='" + exam.getLecture() + "', numofquest='" + exam.getNumofquests() + "', examID='"
					+ exam.getExamnumber() + "', commentforteacher='" + exam.getCommentforteacher()
					+ "', commentsforstudent='" + exam.getCommentforstudent() + "' WHERE id='" + exam.getExamid()
					+ "' ";
			int rst;
			rst = stm.executeUpdate(sql);
			System.out.println("Rows affected: " + rst);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static ArrayList<ExamsInProgress> getAllExamsInProgressByTeacher(User username) {

		ArrayList<ExamsInProgress> examslist = new ArrayList<>();
		System.out.println(username.getUserName());
		try {
			PreparedStatement ps1 = conn.prepareStatement(
					"SELECT E.examinprogressid,EX.topic,EX.course,EX.time,E.reduration,E.status,E.code,E.examid,E.date FROM examinprogress E ,exams EX where E.examid=EX.id AND EX.Lecturer_ID=? AND E.status= 'Done'");
			ps1.setString(1, username.getID());
			ResultSet rst = ps1.executeQuery();
			while (rst.next()) {
				ExamsInProgress exam = new ExamsInProgress(rst.getString(1), rst.getString(2), rst.getString(3),
						rst.getString(4), rst.getString(5), rst.getString(6), rst.getInt(7));
				exam.setExamid(rst.getString(8));
				exam.setDateTime(rst.getString("date"));
				examslist.add(exam);
			}
			return examslist;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static ReportExam GetExamReport(ExamsInProgress exam) {
		ReportExam exam1 = new ReportExam(null, null, null, 0, 0, 0, null, 0, 0, 0);
		try {
			System.out.println("getting the information");
			PreparedStatement ps1 = conn.prepareStatement(
					"SELECT * FROM examdone WHERE examinprogressid= ? AND examtype <> 'Offline' AND submitintime <>'No'");
			ps1.setString(1, exam.getId());

			ResultSet rst = ps1.executeQuery();

			exam1.setExaminprogID(exam.getExamid());

			exam1.setCourse(exam.getCourse());

			exam1.setDate(exam.getDateTime().split(" ")[0]);

			while (rst.next()) {
				System.out.println("getting the grades");
				exam1.addGrade(rst.getDouble(4));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		exam1.calcAverage();

		exam1.calcmedianX();
		return exam1;

	}

	public static ArrayList<StudentsResults> getExamresult(ExamsInProgress examinprog) {

		ArrayList<StudentsResults> examresultlist = new ArrayList<StudentsResults>();
		try {
			PreparedStatement ps1 = conn.prepareStatement(
					"SELECT E.id,E.course,ED.grade,ED.comments,ED.IsConfirmed,ED.studentid,U.firstname,U.lastname,EP.examinprogressid\r\n"
							+ "FROM examdone ED, users U, exams E, examinprogress EP\r\n"
							+ "where EP.examinprogressid= '" + examinprog.getId()
							+ "' AND EP.examid=E.id AND ED.studentid=U.id AND ED.examtype <> 'Offline' AND ED.submitintime <> 'No';");
			ResultSet rst = ps1.executeQuery();
			while (rst.next()) {
				StudentsResults examresults = new StudentsResults(rst.getString(1), rst.getString(2), rst.getString(3),
						rst.getString(4), rst.getString(5), rst.getString(6), rst.getString(7), rst.getString(8),rst.getString("examinprogressid"));
				examresultlist.add(examresults);

			}
			return examresultlist;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static ArrayList<AnswersReview> GetStudentanswers(ExamsInProgress exam) {

		ArrayList<AnswersReview> answerslist = new ArrayList<>();
		String studentID = null;
		try {
			PreparedStatement ps1 = conn.prepareStatement("SELECT U.id\r\n"
					+ "		FROM examdone ED, users U, examinprogress EP\r\n" + "		WHERE ED.examinprogressid='"
					+ exam.getId() + "' AND\r\n" + "         ED.studentid=U.id;");
			
			ResultSet rst = ps1.executeQuery();
			rst.next();
			studentID = rst.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			PreparedStatement ps2 = conn
					.prepareStatement("SELECT DISTINCT QE.questid,Q.questtext,A.student_answer,Q.correctanswer\r\n"
							+ "		FROM questions Q, answersstudentforexam A, questforexam QE\r\n"
							+ "		WHERE A.s_exam_id='" + exam.getId() + "' AND\r\n"
							+ "        A.quest_id=Q.idquest AND Q.idquest=QE.questid AND A.student_id='" + studentID
							+ "';");
			ResultSet rst = ps2.executeQuery();
			while (rst.next()) {
				System.out.println("x");
				AnswersReview questanswer = new AnswersReview(rst.getInt(1), rst.getString(2), rst.getInt(3),
						rst.getInt(4));
				if (questanswer.getCorrectAnswer() == questanswer.getStudentAnswer())
					questanswer.setIsCorrect("Correct");
				else {
					questanswer.setIsCorrect("InCorrect");
				}
				questanswer.setStudentID(studentID);
				answerslist.add(questanswer);
			}
			return answerslist;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Boolean UpdateStudentResult(StudentsResults result) {

		Statement stm;
		String examID = null;
		try {
			PreparedStatement ps1 = conn.prepareStatement("SELECT EP.examinprogressid\r\n"
					+ "		FROM examdone ED, users U, examinprogress EP WHERE ED.examinprogressid=EP.examinprogressid AND EP.examid='"
					+ result.getExamid() + "' AND ED.studentid=U.id;");
			ResultSet rst = ps1.executeQuery();
			rst.next();
			examID = rst.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("I am editing student exam result");
		try {
			stm = conn.createStatement();
			String sql = "UPDATE examdone SET grade='" + result.getGrade() + "', comments='" + result.getComments()
					+ "', IsConfirmed='" + 1 + "' WHERE examinprogressid='" + examID + "' AND studentid='"
					+ result.getStudentID() + "' ";
			int rst;
			rst = stm.executeUpdate(sql);
			System.out.println("Rows affected: " + rst);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
/////// End of Lecturer///////////////////////////////////////////////////////////////////////////////////////////////////////

///////start of student //////////////////////////////////////////////////////////////////////////////////////////////////////

	public static String checkExamCode(String code) {
		Statement stm;
		System.out.println("check if exam in progress");
		try {
			stm = conn.createStatement();
			String sql = "SELECT test.examinprogress.code,test.examinprogress.status\r\n"
					+ " FROM test.examinprogress\r\n" + "WHERE test.examinprogress.code = " + code + ";";
			ResultSet rst;
			rst = stm.executeQuery(sql);
			if (rst.next()) {
				return rst.getString(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static ArrayList<Question> getexamquestions(String code) {
		Statement stm;
		System.out.println("I am getting questions list");
		ArrayList<Question> questionslist = new ArrayList<>();
		int i = 1;
		try {
			stm = conn.createStatement();
			String sql = "SELECT test.questions.*\r\n"
					+ "FROM test.questions , test.questforexam ,test.exams,test.examinprogress\r\n"
					+ "WHERE test.examinprogress.code = " + code
					+ " AND test.examinprogress.examid = test.exams.id AND test.exams.id = test.questforexam.examid AND test.questforexam.questid = test.questions.idquest;";
			ResultSet rst;
			rst = stm.executeQuery(sql);
			while (rst.next()) {
				Question question = new Question(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4),
						rst.getString(5), rst.getString(6), rst.getString(7), Integer.parseInt(rst.getString(8)),
						rst.getString(9), i++);
				questionslist.add(question);
			}
			return questionslist;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Exam getExambyCode(String code) {
		Statement stm;
		System.out.println("I am getting exam details by exam code");

		try {
			stm = conn.createStatement();
			String sql = "SELECT test.exams.* ,test.examinprogress.examinprogressid,test.examinprogress.date\r\n"
					+ "FROM test.exams ,test.examinprogress\r\n" + "WHERE test.examinprogress.code = " + code
					+ " AND test.exams.id = test.examinprogress.examid;";
			ResultSet rst;
			rst = stm.executeQuery(sql);
			while (rst.next()) {
				exam = new Exam(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4),
						rst.getString(5), rst.getString(6), rst.getInt(7));
				exam.setExaminprogressid(rst.getString(11));
				String[] tempString = rst.getString(12).split("\\s");

				exam.setDate(tempString[0]);
				exam.setStarttime(tempString[1]);

				return exam;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean setexamfirstenterflag(String examinprogressid, String studentid) {
		Statement stm1;
		try {
			System.out.println(examinprogressid);
			System.out.println(studentid);
			stm1 = conn.createStatement();
			String sql1 = "SELECT *\r\n" + "FROM test.answersstudentforexam\r\n"
					+ "WHERE test.answersstudentforexam.s_exam_id = " + examinprogressid
					+ " AND test.answersstudentforexam.student_id = " + studentid + ";";
			ResultSet rst;
			rst = stm1.executeQuery(sql1);
			while (rst.next()) {
				System.out.println("Test for enter exam!!!!!!!!" + studentid + examinprogressid);
				return false;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String getExamInProgressStatus(String code) {
		Statement stm;
		String examinprogressid;
		String studentid;
		System.out.println("I'm setting first enter exam falg !!!");
		try {
			stm = conn.createStatement();
			String sql = "SELECT test.examinprogress.status\r\n" + " FROM test.examinprogress\r\n"
					+ " WHERE test.examinprogress.code = " + code + ";";
			ResultSet rst;
			rst = stm.executeQuery(sql);
			rst.next();
			return rst.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<ExamDone> getStudentGradeslist(String id) {
		Statement stat;
		ExamDone draftObj;
		byte flag;
		System.out.println("gettin info");
		ArrayList<ExamDone> studentGradeslist = new ArrayList<>();
		String qu = "select distinct test.exams.id,test.exams.course,test.examdone.* \r\n"
				+ "				from test.examdone ,test.examinprogress,test.exams,test.users\r\n"
				+ "				where test.examdone.examinprogressid=test.examinprogress.examinprogressid and\r\n"
				+ "				examinprogress.examid=test.exams.id and test.examdone.studentid=" + id + ";";
		ResultSet resulset;
		String.format("Getting %s Grades' List ", user.getFirstName());
		try {
			stat = conn.createStatement();
			resulset = stat.executeQuery(qu);
			while (resulset.next()) {
				draftObj = new ExamDone(resulset.getString(1), resulset.getString(2), resulset.getString(6),
						resulset.getString(8), resulset.getString(7), resulset.getString(5), resulset.getString(3));

				if (resulset.getByte(12) == 1) {
					draftObj.setSuspectedCopying(true);
					draftObj.setExam_status("Suspected Copying");
				}
				if (resulset.getByte(11) == 1) {
					draftObj.setIsConfirmed(true);
				}
				System.out.println("" + draftObj.isSuspectedCopying());
				System.out.println(draftObj.toString());
				if (draftObj.isSuspectedCopying())
					draftObj.setGrade("0");
				if (draftObj.isIsConfirmed()) {
					studentGradeslist.add(draftObj);
				}
			}
			return studentGradeslist;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public static boolean ExamFinishedUpdate(ExamInProgressStudent finishedexamdetails) {
		PreparedStatement ps;
		System.out.println("updating answerstudentforexam");
		try {// updating table exam done and answersstudentforexam
			String sql = "INSERT INTO test.examdone (examinprogressid,studentid,timefinished,submitintime,date,examtype)\r\n"
					+ "VALUES (?,?,?,?,?,?);";

			ps = conn.prepareStatement(sql);
			ps.setString(1, finishedexamdetails.getExaminprogressid());
			ps.setString(2, finishedexamdetails.getStudentid());
			ps.setString(3, finishedexamdetails.getTimefinished());
			ps.setString(4, finishedexamdetails.getSubmitintime());
			ps.setString(5, String.valueOf(finishedexamdetails.getLocalDate()));
			ps.setString(6, "Online");
			ps.executeUpdate();
			sql = "INSERT INTO test.answersstudentforexam\r\n" + "VALUES (?,?,?,?);";
			ps = conn.prepareStatement(sql);
			int i;
			for (i = 0; i < finishedexamdetails.getAnswers().length; i++) {
				ps.setString(1, finishedexamdetails.getExaminprogressid());
				ps.setString(2, finishedexamdetails.getStudentid());
				ps.setString(3, finishedexamdetails.getQuestions().get(i).getIdquest());
				ps.setString(4, String.valueOf(finishedexamdetails.getAnswers()[i]));
				ps.executeUpdate();
			}

			sql = "INSERT INTO test.examprogressforstudent\r\n" + "VALUES (?,?,?);";
			ps = conn.prepareStatement(sql);
			ps.setString(1, finishedexamdetails.getExaminprogressid());
			ps.setString(2, finishedexamdetails.getStudentid());
			ps.setString(3, finishedexamdetails.getTimestarted());
			ps.executeUpdate();
			if (i > 0)
				return true;
			else
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void ExamFinishedUpdateOffline(ExamInProgressStudent finishedexamoffline) {

		PreparedStatement ps;
		System.out.println("updating answerstudentforexam");
		try {// updating table exam done and answersstudentforexam
			String sql = "INSERT INTO test.examdone (examinprogressid,studentid,timefinished,submitintime,date,examtype,uploadfile)\r\n"
					+ "VALUES (?,?,?,?,?,?,?);";

			ps = conn.prepareStatement(sql);
			ps.setString(1, finishedexamoffline.getExaminprogressid());
			ps.setString(2, finishedexamoffline.getStudentid());
			ps.setString(3, finishedexamoffline.getTimefinished());
			ps.setString(4, finishedexamoffline.getSubmitintime());
			ps.setString(5, String.valueOf(finishedexamoffline.getLocalDate()));
			ps.setString(6, "Offline");
			ps.setString(7, finishedexamoffline.getAnswersfile());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updateExamInProgressStatus(String status) {
		PreparedStatement ps;
		System.out.println("updating examinprogress status");
		try {
			String sql = " ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, status);
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void getexamdonedata(String examid) {
		Statement stm;
		PreparedStatement ps;
		System.out.println(examid);
		System.out.println("gettin the data from db to check exam");
		try {
			stm = conn.createStatement();
			String sql = "SELECT test.exams.* ,test.examinprogress.examinprogressid \r\n"
					+ "FROM test.exams ,test.examinprogress\r\n" + "WHERE  test.exams.id = " + examid
					+ " AND test.exams.id = test.examinprogress.examid ;";
			ResultSet rst;
			rst = stm.executeQuery(sql);
rst.next();
				checkexam = new CheckExam(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4),
						rst.getString(6), rst.getString(7));
				checkexam.setExaminprogressid(rst.getString(11));
				
			sql = "SELECT test.questions.*\r\n"
					+ "FROM test.questions , test.questforexam ,	test.exams	,	test.examinprogress\r\n"
					+ "WHERE test.examinprogress.examid = " + examid
					+ " AND test.examinprogress.examid = test.exams.id AND test.exams.id = test.questforexam.examid AND test.questforexam.questid = test.questions.idquest;";
			rst = stm.executeQuery(sql);
			while (rst.next()) {
				checkexam.addquestion(rst.getString(1), rst.getString(8));
				checkexam.addquestionidtolist(rst.getString(1));
			}
			sql = "SELECT test.examdone.studentid\r\n" + " FROM test.examdone\r\n"
					+ " WHERE test.examdone.examinprogressid = " + checkexam.getExaminprogressid()
					+ " AND test.examdone.examtype <> 'Offline' AND test.examdone.submitintime='No';";
			rst = stm.executeQuery(sql);
			ArrayList<String> studentidlist = new ArrayList<String>();

			while (rst.next()) {
				studentidlist.add(rst.getString(1));
			}
			checkexam.setStudentidlist(studentidlist);
			sql = "SELECT test.answersstudentforexam.*\r\n" + "FROM test.answersstudentforexam\r\n"
					+ "WHERE test.answersstudentforexam.s_exam_id = " + checkexam.getExaminprogressid() + ";";
			rst = stm.executeQuery(sql);
			int i = 0;
			int numberorquestions = checkexam.getNumofquestions();

			while (rst.next()) {
				if (i == 0) {
					studentanswers = new StudentAnswers(rst.getString(2), checkexam.getNumofquestions());

					studentanswers.addquestionanswer(rst.getString(3), rst.getString(4));
					i++;
				} else {
					studentanswers.addquestionanswer(rst.getString(3), rst.getString(4));
					if (++i == numberorquestions) {
						i = 0;
						checkexam.addStudentAnswers(studentanswers.getStudentid(), studentanswers);
					}
				}

			}
/*
			checkexam.startCheckExam();
			System.out.println("updating the data to db after check exam");
			for (i = 0; i < studentidlist.size(); i++) {
				sql = "UPDATE test.examdone\r\n" + "SET test.examdone.grade = "
						+ checkexam.getStudentAnswer(studentidlist.get(i)).getFinalgrade() + " \r\n"
						+ "WHERE test.examdone.studentid = " + studentidlist.get(i)
						+ " AND test.examdone.examinprogressid = " + checkexam.getExaminprogressid() + ";";
			}*/

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static ArrayList<AnswersReview> getAnswersList(String studentid, ExamDone exam) {
		Statement stat;
		AnswersReview comp;
		int serialnum = 1;
		ArrayList<AnswersReview> answerslist = new ArrayList<>();
		String que = "select distinct test.questforexam.questid,test.questions.questtext,test.questions.correctanswer,test.answersstudentforexam.student_answer\r\n"
				+ "		from test.questions,test.answersstudentforexam,test.examdone,test.questforexam\r\n"
				+ "		where test.examdone.examinprogressid=test.answersstudentforexam.s_exam_id and\r\n"
				+ "        test.answersstudentforexam.quest_id=test.questions.idquest and test.questions.idquest=test.questforexam.questid\r\n"
				+ "        and test.answersstudentforexam.student_id=" + studentid
				+ " and test.answersstudentforexam.s_exam_id=" + exam.getExamInporgID() + ";";
		ResultSet rSet;
		try {
			stat = conn.createStatement();
			rSet = stat.executeQuery(que);
			while (rSet.next()) {
				comp = new AnswersReview(serialnum, rSet.getString(2), rSet.getInt(4), rSet.getInt(3));
				if (comp.getCorrectAnswer() == comp.getStudentAnswer())
					comp.setIsCorrect("Correct");
				else {
					comp.setIsCorrect("InCorrect");
				}
				System.out.println(comp.toString());
				answerslist.add(comp);
				serialnum++;
			}
			return answerslist;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}
//////////End of student ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

}