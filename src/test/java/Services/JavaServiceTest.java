package Services;

import com.github.jsonldjava.utils.Obj;
import org.junit.Before;
import org.junit.Test;
import util.AnnotationConsumer;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by freddy on 21.10.17.
 */
public class JavaServiceTest {
	private JavaService javaService;
	
	@Before
	public void setUp() {
		javaService = new JavaService();
		FileRetrievementService.getInstance().setDataPath("src/test/resources/");
	}
	
	@Test
	public void getJavaImportetElements_correct()  {
		try {
			String content = FileRetrievementService.getInstance().getContent("http://softlang.com/Java/SampleClass.java");
			List<String> result = javaService.getJavaImportedElements(content); //.stream().map(x ->x.toString());
			
			assertEquals("ALl imports noticed", result.size(), 3);
			assertEquals("Name is matching case 1", result.get(0).toString(), "junit.extensions.ActiveTestSuite");
			assertEquals("Name is matching case 2", result.get(1).toString(), "java.util.List");
			
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
		
	}
	
	@Test
	public void getJavaImportetElements_correct_withPackage()  {
		try {
			String content = FileRetrievementService.getInstance().getContent("http://softlang.com/Java/HibernateEmailDao");
			List<String> result = javaService.getJavaImportedElements(content);
			
			assertEquals("ALl imports noticed", result.size(), 8);
			assertEquals("Name is matching case 1", result.get(0).toString(), "uk.org.rbc1b.roms.db.email");
			assertEquals("Name is matching case 2", result.get(1).toString(), "java.util.List");
			
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
			List<String> result = javaService.getMethodCalls(content, "SampleClassComplex");
			
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
			Set<String> result = javaService.getDeclaredClasses(content, "SampleClassDeclarations");
			
			assertEquals(result.size(), 9);
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
	}
	
	@Test
	public void getAnnotations_correct() {
		String content = null;
		try {
			content = FileRetrievementService.getInstance().getContent("http://softlang.com/Java/SampleClassDeclarations.java");
			Map<String, ? extends Object> result = javaService.getAnnotations(content, "SampleClassDeclarations");
			
			assertEquals(result.size(), 2);
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
	}
	
	@Test
	public void getAnnotations_byName_correct() {
		String content = null;
		try {
			content = FileRetrievementService.getInstance().getContent("http://softlang.com/Java/SampleClassDeclarations.java");
			AnnotationConsumer.AnnotationValue  result = javaService.getAnnotationByName(content, "SampleClassDeclarations", "Table");
			assertNotNull(result);
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
	}
	
	@Test
	public void getAnnotations_byName_wrong() {
		String content = null;
		try {
			content = FileRetrievementService.getInstance().getContent("http://softlang.com/Java/SampleClassDeclarations.java");
			Object result = javaService.getAnnotationByName(content, "SampleClassDeclarations", "TableBlub");
			assertNull(result);
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
	}
	
	@Test
	public void getAnnotations_byName_AndKey_correct() {
		String content = null;
		try {
			content = FileRetrievementService.getInstance().getContent("http://softlang.com/Java/SampleClassDeclarations.java");
			String result = javaService.getAnnotationByNameAndKey(content, "SampleClassDeclarations", "Table", "name");
			
			assertNotNull(result);
			assertEquals(result, "\"Peter\"");
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
	}
	@Test
	public void getAnnotations_byName_correct2() {
		String content = null;
		try {
			content = FileRetrievementService.getInstance().getContent("http://softlang.com/HibernateAnnotation/AFETipoDescuento.java");
			AnnotationConsumer.AnnotationValue  result = javaService.getAnnotationByName(content, "AFETipoDescuento", "Table");
			assertNotNull(result);
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
	}
	
	@Test
	public void test_getJavaClass() {
		String content = null;
		try {
			String uri = "http://softlang.com/Java/QueueDocumentLink.java";
			content = FileRetrievementService.getInstance().getContent(uri);
			String path = FileRetrievementService.getInstance().uriToPath(uri);
			String  result = javaService.getJavaClass(content, path);
			assertEquals(result, "org.oscarehr.common.model.QueueDocumentLink");
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
	}
	@Test
	public void test_getJavaClass_with_ExtendedType() {
		String content = null;
		try {
			String uri = "http://softlang.com/Java/DemographicAccessoryDao.java";
			content = FileRetrievementService.getInstance().getContent(uri);
			String path = FileRetrievementService.getInstance().uriToPath(uri);
			Set<String>  result = javaService.getDeclaredClasses(content, "DemographicAccessoryDao");
			assertTrue(result.contains("DemographicAccessory"));
		}
		catch (FileRetrievementServiceException e) {
			assertNull(e);
		}
	}
}
