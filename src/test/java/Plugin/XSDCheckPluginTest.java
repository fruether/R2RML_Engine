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
public class XSDCheckPluginTest {
	private XSDCheckPlugin xsdCheckPlugin = new XSDCheckPlugin();
	
	@Before
	public void setUp() {
		FileRetrievementService.getInstance().setDataPath("src/test/resources/");
	}
	
	
	@Test
	public void bodyCall_wrong_dtd()  {
		Node xml_file = NodeFactory.createURI("http://softlang.com/input/inputHibernate.xml");
		Node xsd_file = NodeFactory.createURI("http://softlang.com/input/noHibernate.xsd");
		Node[] env = new Node[] {xml_file, xsd_file};
		
		boolean isValidXSD = xsdCheckPlugin.bodyCall(env, 2, null);
		assertFalse(isValidXSD);
		
	}
	@Test
	public void bodyCall_correct_xsd()  {
		Node xml_file = NodeFactory.createURI("http://softlang.com/pom.xml");
		Node xsd_file = NodeFactory.createURI("http://softlang.com/pom.xsd");
		Node[] env = new Node[] {xml_file, xsd_file};
		
		boolean isValidXSD = xsdCheckPlugin.bodyCall(env, 2, null);
		assertTrue(isValidXSD);
	}
	@Test
	public void bodyCall_wrong_xsd()  {
		Node xml_file = NodeFactory.createURI("http://softlang.com/pom2.xml");
		Node xsd_file = NodeFactory.createURI("http://softlang.com/pom.xsd");
		Node[] env = new Node[] {xml_file, xsd_file};
		
		boolean isValidXSD = xsdCheckPlugin.bodyCall(env, 2, null);
		assertFalse(isValidXSD);
	}
	
}
