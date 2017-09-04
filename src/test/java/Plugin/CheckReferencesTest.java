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
	public void test_correct1() {
		Node input = NodeFactory.createURI("http://softlang.de/hibernate.cfg.xml");
		Node input2 = NodeFactory.createBlankNode();
		
		Node[] env = new Node[] {input, input2};
		
		boolean result = checkReferences.bodyCall(env, 2, null);
		
	}
}
