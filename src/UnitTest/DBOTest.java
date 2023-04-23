package UnitTest;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import models.User;
import server.DBO;

class DBOTest {

	
	
	DBO dbo;	
	User user;
	
	@BeforeEach
	void setUp() throws Exception {
		
		dbo=DBO.GetInstance();
		dbo.connectToDB("root","Aa123456");
	}

	
	
	
	@Test
	void testAuthLogInUserExist()throws SQLException {
		try {
		user = new User("principal","123");
		User expected=new User("principal","123","3","principal","3","14");
		User result=dbo.authLogIn(user);
		assertEquals(result.getUserName(),expected.getUserName());
		assertEquals(result.getPassword(),expected.getPassword());
		assertEquals(result.getRole(),expected.getRole());
		assertEquals(result.getFirstName(),expected.getFirstName());
		assertEquals(result.getLastName(),expected.getLastName());
		assertEquals(result.getID(),expected.getID());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Error");
			e.printStackTrace();
		}
	}
	
	@Test
	void testAuthLogInUserWrongField()throws SQLException {
		try {
		user = new User("principal","123");
		User expected=new User("principal","12345","3","principal","3","14");
		User result=dbo.authLogIn(user);
		assertEquals(result.getUserName(),expected.getUserName());
		assertNotEquals(result.getPassword(),expected.getPassword());
		assertEquals(result.getRole(),expected.getRole());
		assertEquals(result.getFirstName(),expected.getFirstName());
		assertEquals(result.getLastName(),expected.getLastName());
		assertEquals(result.getID(),expected.getID());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Error");
			e.printStackTrace();
		}
	
	}
	@Test
	void testAuthLogInUserDoesntExist()throws SQLException {
		try {
		user = new User("principal","1234");
		User expected=null;
		User result;
		
			result = dbo.authLogIn(user);
			assertEquals(expected,result);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Error");
			e.printStackTrace();
		}
		
	}

}
