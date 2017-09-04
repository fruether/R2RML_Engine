package Plugin;

import Services.FileRetrievementService;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by freddy on 03.09.17.
 */
public class DTDCheckPluginTest {
	DTDCheckPlugin dtdCheckPlugin = new DTDCheckPlugin();
	
	@Before
	public void setUp(){
		FileRetrievementService.getInstance().setDataPath("src/test/resources/");
	}
	
	@Test
	public void dtd_check_correct() {
		Node xmlFile = NodeFactory.createURI("http://softlang.de/hibernate.cfg.xml");
		Node dtdFile = NodeFactory.createURI("http://softlang.de/hibernate-configuration-3.0.dtd");
		
		Node[] env = new Node[] {xmlFile, dtdFile};
		
		boolean result = dtdCheckPlugin.bodyCall(env, 2, null);
		assertTrue(result);
	}
	
	@Test
	public void dtd_check_correct2() {
		Node xmlFile = NodeFactory.createURI("http://softlang.de/Allergy.hbm.xml"); //Allergy.hbm.xml
		Node dtdFile = NodeFactory.createURI("http://softlang.de/hibernate-mapping-3.0.dtd");
		
		Node[] env = new Node[] {xmlFile, dtdFile};
		
		boolean result = dtdCheckPlugin.bodyCall(env, 2, null);
		
		assertTrue(result);
	}
	
	@Test
	public void dtd_check_wrong() {
		Node xmlFile = NodeFactory.createURI("http://softlang.de/pom.xml"); //Allergy.hbm.xml
		Node dtdFile = NodeFactory.createURI("http://softlang.de/hibernate-mapping-3.0.dtd");
		
		Node[] env = new Node[] {xmlFile, dtdFile};
		
		boolean result = dtdCheckPlugin.bodyCall(env, 2, null);
		
		assertFalse(result);
	}
}
