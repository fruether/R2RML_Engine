package Plugin;

import Services.FileRetrievementService;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;

import static org.junit.Assert.*;

/**
 * Created by freddy on 25.07.17.
 */
public class LiquidBaseDependencyPluginTest {
	private LiquidBaseDependencyPlugin liquidBaseDependencyPlugin;
	
	@Before
	public void setUp() throws ParserConfigurationException {
		liquidBaseDependencyPlugin = new LiquidBaseDependencyPlugin();
		FileRetrievementService.getInstance().setDataPath("src/test/resources/");
	}
	@Test
	public void getName() throws Exception {
		assertEquals(liquidBaseDependencyPlugin.getName(), "liquidBaseDependencyCheck");
		
	}
	
	@Test
	public void getArgLength() throws Exception {
		assertEquals(liquidBaseDependencyPlugin.getArgLength(), 1);
	}
	
	@Test
	public void test_bodyCall_correct() throws Exception {
		Node input = NodeFactory.createURI("http://softlang.com/pom.xml");
		
		boolean result = liquidBaseDependencyPlugin.bodyCall(new Node[]{input}, 1, null);
		
		assertTrue(result);
		
	}
	
	@Test
	public void test_bodyCall_wrong() throws Exception {
		Node input = NodeFactory.createURI("http://softlang.com/pom2.xml");
		
		boolean result = liquidBaseDependencyPlugin.bodyCall(new Node[]{input}, 1, null);
		
		assertFalse(result);
		
	}
}
