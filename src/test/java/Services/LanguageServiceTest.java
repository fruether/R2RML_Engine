package Services;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by freddy on 15.09.17.
 */
public class LanguageServiceTest {
	//Qualification.hbm.xml
	private LanguageService languageService;
	
	@Before
	public void setUp() {
		languageService = LanguageService.getInstance();
		FileRetrievementService.getInstance().setDataPath("src/test/resources/");
	}
	
	@Test
	public void getXMLFirstAttribute_correct() {
		try {
			String content = FileRetrievementService.getInstance().getContent("http://softlang.com/Allergy.hbm.xml");
			String result = languageService.getXMLFirstAttribute("class", "name", content);
			assertEquals(result, "org.openmrs.Allergy");
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
		catch (LanguageServiceException e) {
			assertNull(e);
		}
	}
	
	@Test
	public void getXMLFirstAttribute_correct2() {
		try {
			String content = FileRetrievementService.getInstance().getContent("http://softlang.com/Qualification.hbm.xml");
			String result = languageService.getXMLFirstAttribute("hibernate-mapping", "package", content);
			assertEquals(result, "uk.org.rbc1b.roms.db.volunteer.qualification");
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
		catch (LanguageServiceException e) {
			assertNull(e);
		}
	}
	@Test
	public void getXMLFirstAttribute_wrong() {
		try {
			String content = FileRetrievementService.getInstance().getContent("http://softlang.com/Allergy.hbm.xml");
			String result = languageService.getXMLFirstAttribute("name", "class", content);
			assertEquals(result, "");
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
		catch (LanguageServiceException e) {
			assertNull(e);
		}
	}
	
	@Test
	public void getJavaImportetElements_correct()  {
		try {
			String content = FileRetrievementService.getInstance().getContent("http://softlang.com/Java/SampleClass.java");
			List<String> result = languageService.getJavaImportedElements(content);
			
			assertEquals("ALl imports noticed", result.size(), 3);
			assertEquals("Name is matching case 1", result.get(0), "junit.extensions.ActiveTestSuite");
			assertEquals("Name is matching case 2", result.get(1), "java.util.List");
			
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
		
	}
	
	@Test
	public void getJavaImportetElements_correct_withPackage()  {
		try {
			String content = FileRetrievementService.getInstance().getContent("http://softlang.com/Java/HibernateEmailDao");
			List<String> result = languageService.getJavaImportedElements(content);
			
			assertEquals("ALl imports noticed", result.size(), 8);
			assertEquals("Name is matching case 1", result.get(0), "uk.org.rbc1b.roms.db.email");
			assertEquals("Name is matching case 2", result.get(1), "java.util.List");
			
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
		
	}
	
	@Test
	public void getJavaCalledMethods() {
		String content = null;
		try {
			content = FileRetrievementService.getInstance().getContent("http://softlang.com/Java/SampleClassComplex.java");
			List<String> result = languageService.getMethodCalls(content, "SampleClassComplex");
			
			assertEquals(result.size(), 2);
			assertEquals(result.get(0), "println");
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
		
	}
	
	@Test
	public void getDeclaredClasses_correct() {
		String content = null;
		try {
			content = FileRetrievementService.getInstance().getContent("http://softlang.com/Java/SampleClassDeclarations.java");
			Set<String> result = languageService.getDeclaredClasses(content, "SampleClassDeclarations");
			
			assertEquals(result.size(), 9);
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
	}
	
	@Test
	public void parseMySQL_correct() {
		String content = null;
		boolean result = false;
		try {
			content = FileRetrievementService.getInstance().getContent("http://softlang.com/001.sql");
			//content = "GRANT ALL ON blub.* TO xay;";
			result = languageService.parseMySQL(content.toUpperCase());
		
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
			
			result = languageService.parseMySQL(content);
			
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
			
			result = languageService.parseMySQL(content);
			
			assertFalse(result);

		
	}
	@Test
	public void parseMySQL_wrong3() {
		String content = null;
		boolean result = false;
		
		try {
			content = FileRetrievementService.getInstance().getContent("http://softlang.com/001_wrong.sql");
			result = languageService.parseMySQL(content);
			
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
			tables = languageService.mysql_get_tables(content);
			
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
		
		assertEquals(tables.size(), 88);
		assertTrue(tables.contains("USER"));
		assertTrue(tables.contains("Congregation_AUD".toUpperCase()));
		assertTrue(tables.contains("KingdomHallFeature_AUD".toUpperCase()));
		
	}
}
