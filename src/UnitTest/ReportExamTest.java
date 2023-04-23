package UnitTest;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import models.ReportExam;
import server.DBO;

class ReportExamTest {
	
	
	ReportExam Report;
	
	@BeforeEach
	void setUp() throws Exception {
		Report = new ReportExam();
		Report.setNumbOfStudents(0);
		Report.addGrade(60.0);
		Report.addGrade(70.0);
		Report.addGrade(80.0);
		
	}

	
	//test : calculate the median and check if it returns 70
	//input : the expected median which is 70.0
	//output : the output should be true
	
	@Test
	void testCalcmedianXCorrect() {
		float expected;
		expected=(float) 70.0;
		Report.calcmedianX();
		assertEquals(Report.getMedianX(),expected);	
	}
	
		//test : calculate the median and check if it returns 80
		//input : wrong expected median which is 80.0
		//output : the output should be false
	
	
	@Test
	void testCalcmedianXWrong() {
		float expected;
		expected=(float) 80.0;
		Report.calcmedianX();
		assertNotEquals(Report.getMedianX(),expected);	
	}
	//test : calculate the Average and check if it returns 70
			//input : the expected average which is 70.0
			//output : the output should be True
	@Test
	void testCalcAverageCorrect() {
		float expected;
		expected =(float) 70.0;
		Report.calcAverage();
		assertEquals(Report.getAverage(),expected);
		
	}
	//test : calculate the Average and check if it returns 80
	//input : the  wrong expected average which is 80.0
	//output : the output should be False
	
	@Test
	void testCalcAverageWrong() {
		float expected;
		expected =(float) 80.0;
		Report.calcAverage();
		assertNotEquals(Report.getAverage(),expected);
		
	}
		//test : calculate the StandDeviation and check if it returns 7.359800721939872
		//input : the   expected StandDeviation which is 7.359800721939872
		//output : the output should be True
	
	@Test
	void testCalcStandDeviationCorrect() {
		double expected;
		Report.addGrade(75.0);
		Report.addGrade(70.0);
		Report.addGrade(80.0);
		expected = (double)7.359800721939872;
		Report.setAverage((float)75.0);
		Report.calcStandDeviation();
		assertEquals((double)Report.getStandDeviation(),expected);
	}
	//test : calculate the StandDeviation and check if it returns 9.16496581
			//input : the  wrong expected average which is  9.16496581
			//output : the output should be False
	@Test
	void testCalcStandDeviationWrong() {
		double expected;
		expected = (double)9.16496581;
		Report.setAverage((float)70.0);
		Report.calcStandDeviation();
		assertNotEquals((double)Report.getStandDeviation(),expected);
		
	}
	
	
	
	
	
	
	
	
	

}
