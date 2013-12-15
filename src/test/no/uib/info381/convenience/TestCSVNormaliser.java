package test.no.uib.info381.convenience;

import static org.junit.Assert.*;

import java.io.File;

import no.uib.info381.knn.convenience.CSVNormaliser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestCSVNormaliser {
	CSVNormaliser norm = null;

	@Before
	public void setUp() throws Exception {
		norm = new CSVNormaliser(new File("testdata/csv/missingdata.csv"), 1);
	}

	@After
	public void tearDown() throws Exception {
		norm = null;
	}

	@Test
	public void testFillBlanks() {
		norm.fillBlanks();
		String expected = "19,Eple,\n" +
						  "21,Appelsin,\n" +
						  "20.0,Eple,\n" +
						  "20,Eple,\n" +
						  "20,Appelsin,\n";
		String actual = norm.toString();
		assertEquals("Incorrect result", expected, actual);
	}

	@Test
	public void testNormalize() {
		norm.fillBlanks().normalize();
		String expected = "0.0,Eple,\n" +
						  "1.0,Appelsin,\n" +
						  "0.5,Eple,\n" +
						  "0.5,Eple,\n" +
						  "0.5,Appelsin,\n";
		String actual = norm.toString();
		assertEquals("Incorrect result", expected, actual);
	}


}
