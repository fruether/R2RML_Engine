package Services;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by freddy on 15.09.17.
 */
public class LanguageServiceTest {
	
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
			List<String> result = languageService.getJavaImportetElements(content);
			
			assertEquals("ALl imports noticed", result.size(), 3);
			assertEquals("Name is matching", result.get(0), "junit.extensions.ActiveTestSuite");
			assertEquals("Name is matching2", result.get(1), "java.util.List");
			
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
		
	}
	
	
}
