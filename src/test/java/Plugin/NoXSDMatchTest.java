package Plugin;

import Services.FileRetrievementService;
import Services.ValidationService;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by freddy on 25.07.17.
 */
public class NoXSDMatchTest {
	private NoXSDMatch noXSDMatch;
	
	private FileRetrievementService fileRetrievementService;
	private ValidationService validationService;
	
	@Before
	public void setUP() {
		noXSDMatch = new NoXSDMatch();
		fileRetrievementService = FileRetrievementService.getInstance();
		validationService = ValidationService.getInstance();
		fileRetrievementService.setDataPath("src/test/resources/");
		validationService.clean();
		
	}
	
	@Test
	public void getName() throws Exception {
		assertEquals(noXSDMatch.getName(), "NoXSDMatch");
	}
	
	@Test
	public void getArgLength() throws Exception {
		assertEquals(noXSDMatch.getArgLength(), 1);
	}
	
	@Test
	public void test_bodyCall_wrong() throws Exception {
		Node source = NodeFactory.createURI("http://softlang.com/pom.xml");
		
		boolean result = noXSDMatch.bodyCall(new Node[] {source}, 1 , null);
		
		assertTrue(result);
	}
	
	@Test
	public void test_bodyCall_correct() throws Exception {
		Node source = NodeFactory.createURI("http://softlang.com/pom.xml");
		String xsdPath = "src/test/resources/maven-4.0.0.xsd";
		String inPath = "src/test/resources/pom.xml";
		
		ValidationService.getInstance().validateXMLSchema(inPath, xsdPath);
		
		boolean result = noXSDMatch.bodyCall(new Node[] {source}, 1 , null);
		
		assertFalse(result);
	}
	
}
