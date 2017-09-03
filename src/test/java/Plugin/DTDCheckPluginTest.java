package Plugin;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by freddy on 03.09.17.
 */
public class DTDCheckPluginTest {
	DTDCheckPlugin dtdCheckPlugin = new DTDCheckPlugin();
	
	@Test
	public void bodyCall() throws Exception {
		Node xmlFile = NodeFactory.createURI("http://softlang.de/a.xml");
		Node xsdFile = NodeFactory.createURI("http://softlang.de/a.xsd");
		
		Node[] env = new Node[] {xmlFile, xsdFile};
		
		dtdCheckPlugin.bodyCall(env, 2, null);
	}
	
}
