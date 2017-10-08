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
			
			assertEquals(result.size(), 8);
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
	}
	
	
}
