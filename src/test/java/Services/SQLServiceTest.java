package Services;

import org.apache.jena.base.Sys;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
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
	public void mysql_get_tables_correct2() {
		String content = null;
		Set<String> tables = null;
		boolean result = false;
		
		try {
			content = FileRetrievementService.getInstance().getContent("http://softlang.com/SQL/initcaisi.sql").toUpperCase();
			tables = sqlService.mysql_get_tables(content);
			
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
		
		assertEquals(tables.size(), 120);
		assertTrue(tables.contains("functionalCentreAdmission".toUpperCase()));
		assertTrue(tables.contains("ClientLink".toUpperCase()));
		assertTrue(tables.contains("GroupNoteLink".toUpperCase()));
		
		
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
	
	@Test
	public void parse_sql_correct() {
		String content = null;
		boolean result = false;
		
		try {
			content = FileRetrievementService.getInstance().getContent("http://softlang.com/SQL/initcaisi.sql").toUpperCase();
			//content = content.replace("DEFAULT CURRENT_TIMESTAMP", "");
			result = sqlService.parseSQL(content);
			
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
		
		assertTrue(result);
		
	}
	
	@Test
	public void parse_sql_correct_large() {
		String content = null;
		boolean result = false;
		
		try {
			content = FileRetrievementService.getInstance().getContent("http://softlang.com/SQL/drugref.sql");
			content = content.replace("DEFAULT CURRENT_TIMESTAMP", "");
			result = sqlService.parseSQL(content);
			
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
		
		assertTrue(result);
		
	}
	
	@Test
	public void test_get_matches_small_file() {
		String uri = "http://softlang.com/001.sql";
		FileRetrievementService fileRetrievementService = FileRetrievementService.getInstance();
		try {
			String content = fileRetrievementService.getContent(uri);
			
			Map<String, int[]> matchedTable = sqlService.getCreateStmts(content);
			assertEquals(matchedTable.size(), 88);
			assertTrue(matchedTable.containsKey("USER"));
			assertTrue(matchedTable.containsKey("Congregation_AUD".toUpperCase()));
			
		}
		catch (FileRetrievementServiceException exception) {
			assertTrue(false);
		}
		
	}
	@Test
	public void test_get_matches_large_file() {
		String uri = "http://softlang.com/SQL/drugref.sql";
		FileRetrievementService fileRetrievementService = FileRetrievementService.getInstance();
		
		try {
			String content = fileRetrievementService.getContent(uri);
			Map<String, int[]> matchedTable = sqlService.getCreateStmts(content);
			
		}
		catch (FileRetrievementServiceException exception) {
			assertTrue(false);
		}
	}
	@Test
	public void test_get_matches_small_file2() {
		String uri = "http://softlang.com/SQL/patch-2008-12-19.sql";
		FileRetrievementService fileRetrievementService = FileRetrievementService.getInstance();
		
		try {
			String content = fileRetrievementService.getContent(uri);
			boolean matchedTable = sqlService.parseSQL(content);
			
			assertTrue(matchedTable);
			
		}
		catch (FileRetrievementServiceException exception) {
			assertTrue(false);
		}
	}
}
