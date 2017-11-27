package Plugin.JavaSpecific;

import Services.FileRetrievementService;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by freddy on 26.11.17.
 */
public class CheckExtensionTest {
	private CheckExtension checkExtension;
	private FileRetrievementService fileRetrievementService;
	
	@Before
	public void setUp() {
		checkExtension = new CheckExtension();
		fileRetrievementService = FileRetrievementService.getInstance();
		fileRetrievementService.setDataPath("src/test/resources/");
	}
	@Test
	public void check_existing_extension_package_correct() {
		String javaFile = "http://softlang.com/Java/MdsZLBDao.java";
		String classUri = "http://softlang.com/Class/org.oscarehr.common.dao.AbstractDao";
		
		Node javaFileNode = NodeFactory.createURI(javaFile);
		Node classUriNode = NodeFactory.createURI(classUri);
		Node[] env = new Node[] {javaFileNode, classUriNode};
		
		boolean result = checkExtension.bodyCall(env, 2, null);
		assertTrue(result);
		
	}
	@Test
	public void check_existing_extension_wrong() {
		String javaFile = "http://softlang.com/Java/MdsZLBDao.java";
		String classUri = "http://softlang.com/Class/org.oscarehr.common.dao.MdsZLB";
		
		Node javaFileNode = NodeFactory.createURI(javaFile);
		Node classUriNode = NodeFactory.createURI(classUri);
		Node[] env = new Node[] {javaFileNode, classUriNode};
		
		boolean result = checkExtension.bodyCall(env, 2, null);
		assertFalse(result);
		
	}
	
	@Test
	public void check_existing_extension_correct() {
		String javaFile = "http://softlang.com/Java/MeasurementTypeDao.java";
		String classUri = "http://softlang.com/Class/org.oscarehr.common.model.AbstractDao";
		
		Node javaFileNode = NodeFactory.createURI(javaFile);
		Node classUriNode = NodeFactory.createURI(classUri);
		Node[] env = new Node[] {javaFileNode, classUriNode};
		
		boolean result = checkExtension.bodyCall(env, 2, null);
		assertTrue(result);
		
	}
	
	
}
