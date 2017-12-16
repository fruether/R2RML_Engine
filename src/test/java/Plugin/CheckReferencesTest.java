package Plugin;

import Services.FileRetrievementService;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by freddy on 04.09.17.
 */
public class CheckReferencesTest {
	private CheckReferences checkReferences = new CheckReferences();
	
	@Before
	public void setUp() {
		FileRetrievementService.getInstance().setDataPath("src/test/resources/");
	}
	@Test
	public void test_correct_global_path() {
		Node input = NodeFactory.createURI("http://softlang.de/hibernate.cfg.xml");
		Node input2 = NodeFactory.createURI("http://softlang.de/org/openmrs/api/db/hibernate/Allergy.hbm.xml");
		
		Node[] env = new Node[] {input, input2};
		
		boolean result = checkReferences.bodyCall(env, 2, null);
		
		assertTrue(result);
	}
	@Test
	public void test_correct_sub_directory_path() {
		Node input = NodeFactory.createURI("http://softlang.de/hibernate.cfg.xml");
		Node input2 = NodeFactory.createURI("http://softlang.de/hibernate/Allergy.hbm.xml");
		
		Node[] env = new Node[] {input, input2};
		
		boolean result = checkReferences.bodyCall(env, 2, null);
		
		assertTrue(result);
	}
	@Test
	public void test_correct_in_directory_path() {
		Node input = NodeFactory.createURI("http://softlang.de/dir1/subdir1/hibernate.cfg.xml");
		Node input2 = NodeFactory.createURI("http://softlang.de/dir1/subdir1/Allergy.hbm.xml");
		
		Node[] env = new Node[] {input, input2};
		
		boolean result = checkReferences.bodyCall(env, 2, null);
		
		assertTrue(result);
	}
	
	@Test
	public void test_wrong_directory_path() {
		Node input = NodeFactory.createURI("http://softlang.de/hibernate.cfg.xml");
		Node input2 = NodeFactory.createURI("http://softlang.de/hibernate/Allergy2.hbm.xml");
		
		Node[] env = new Node[] {input, input2};
		
		boolean result = checkReferences.bodyCall(env, 2, null);
		
		assertFalse(result);
	}
	
}
