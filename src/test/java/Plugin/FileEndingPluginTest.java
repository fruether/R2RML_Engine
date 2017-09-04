package Plugin;

import org.apache.jena.graph.NodeFactory;
import org.junit.Test;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;

import static org.junit.Assert.*;

/**
 * Created by freddy on 04.09.17.
 */
public class FileEndingPluginTest {
	private FileEndingPlugin fileEndingPlugin = new FileEndingPlugin();
	
	@Test
	public void getName() throws Exception {
	}
	
	@Test
	public void getArgLength() throws Exception {
		assertEquals(fileEndingPlugin.getArgLength(), 2);
	}
	
	@Test
	public void test_plugin_correct() {
		Node uri = NodeFactory.createURI("http://softlang.org/plugins/plugin/description.dtd");
		Node suffix = NodeFactory.createLiteral(".dtd");
		Node[] env = new Node[] {uri, suffix};
		
		boolean result = fileEndingPlugin.bodyCall(env, 2, null);
		
		assertTrue(result);
	}
	
	@Test
	public void test_plugin_wrong() {
		Node uri = NodeFactory.createURI("http://softlang.org/plugins/plugin/description.xml");
		Node suffix = NodeFactory.createLiteral(".dtd");
		Node[] env = new Node[] {uri, suffix};
		
		boolean result = fileEndingPlugin.bodyCall(env, 2, null);
		
		assertFalse(result);
	}
	
}
