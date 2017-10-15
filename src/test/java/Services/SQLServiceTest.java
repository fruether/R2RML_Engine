package Services;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by freddy on 15.10.17.
 */
public class SQLServiceTest {
	private SQLService sqlService;
	
	@Before
	public void setUp() {
		sqlService = new SQLService();
		FileRetrievementService.getInstance().setDataPath("src/test/resources/");
	}
	@Test
	public void parseMySQL_correct() {
		String content = null;
		boolean result = false;
		try {
			content = FileRetrievementService.getInstance().getContent("http://softlang.com/001.sql");
			//content = "GRANT ALL ON blub.* TO xay;";
			result = sqlService.parseSQL(content.toUpperCase());
			
			assertTrue(result);
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
		
	}
	
	@Test
	public void parseMySQL_wrong() {
		String content = null;
		
		boolean result = false;
		try {
			content = FileRetrievementService.getInstance().getContent("http://softlang.com/Java/SampleClassDeclarations.java");
			
			result = sqlService.parseSQL(content);
			
			assertFalse(result);
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
		
	}
	@Test
	public void parseMySQL_wrong2() {
		String content = null;
		
		boolean result = false;
		content = "SELEKT X, Y ROM table";
		
		result = sqlService.parseSQL(content);
		
		assertFalse(result);
		
		
	}
	@Test
	public void parseMySQL_wrong3() {
		String content = null;
		boolean result = false;
		
		try {
			content = FileRetrievementService.getInstance().getContent("http://softlang.com/001_wrong.sql");
			result = sqlService.parseSQL(content);
			
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
		
		assertFalse(result);
	}
	@Test
	public void mysql_get_tables_correct() {
		String content = null;
		Set<String> tables = null;
		boolean result = false;
		
		try {
			content = FileRetrievementService.getInstance().getContent("http://softlang.com/001.sql").toUpperCase();
			tables = sqlService.mysql_get_tables(content);
			
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
		
		assertEquals(tables.size(), 88);
		assertTrue(tables.contains("USER"));
		assertTrue(tables.contains("Congregation_AUD".toUpperCase()));
		assertTrue(tables.contains("KingdomHallFeature_AUD".toUpperCase()));
		
	}
	@Test
	public void test_sqliteInput_1() {
		String content = null;
		boolean result = false;
		try {
			content = FileRetrievementService.getInstance().getContent("http://softlang.com/sqlite_test_1.sql");
			result = sqlService.parseSQL(content.toUpperCase());
			
			assertTrue(result);
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
		
	}
}
