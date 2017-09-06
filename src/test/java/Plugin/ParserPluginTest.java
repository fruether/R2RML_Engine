package Plugin;

import Services.FileRetrievementService;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.reasoner.TriplePattern;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.Node_RuleVariable;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.impl.BBRuleContext;
import org.apache.jena.reasoner.rulesys.impl.BFRuleContext;
import org.apache.jena.reasoner.rulesys.impl.BindingVector;
import org.junit.Before;
import org.junit.Test;

import javax.naming.Binding;

import static org.junit.Assert.*;

/**
 * Created by freddy on 10.07.17.
 */
public class ParserPluginTest {
	
	private FileRetrievementService fileRetrievementService;
	private ParserPlugin parserPlugin;
	private BBRuleContext ruleContext;
	
	@Before
	public void setUp() throws Exception {
		fileRetrievementService = FileRetrievementService.getInstance();
		fileRetrievementService.setDataPath("src/test/resources/");
		parserPlugin = new ParserPlugin();
		ruleContext = new BBRuleContext(null);
	}
	
	private Node[] setUpRuleContext(Node source) {
		Node_RuleVariable empty = new Node_RuleVariable("language",1);
		Node[] env = new Node[]{source, empty};
		BindingEnvironment bindingEnvironment = new BindingVector(env);
		ruleContext.setEnv(bindingEnvironment);
		return env;
	}
	@Test
	public void bodyCall_correct_xml() throws Exception {
		Node source = NodeFactory.createURI("http://softlang.com/pom.xml");
		String resultingLanguage = "http://softlang.com/XML";
		Node[] env = setUpRuleContext(source);
		
		boolean result = parserPlugin.bodyCall(env,2 , ruleContext);
		Node languageNode = ruleContext.getEnv().getGroundVersion(env[1]);
		
		assertTrue(result);
		assertEquals(languageNode.getURI(), resultingLanguage);
	}
	@Test
	public void bodyCall_correct_xsd() throws Exception {
		Node source = NodeFactory.createURI("http://softlang.com/maven-4.0.0.xsd");
		String resultingLanguage = "http://softlang.com/XSD";
		Node[] env = setUpRuleContext(source);
		
		
		boolean result = parserPlugin.bodyCall(env,2 , ruleContext);
		assertTrue(result);
		Node languageNode = ruleContext.getEnv().getGroundVersion(env[1]);
		
		assertTrue(result);
		assertEquals(languageNode.getURI(), resultingLanguage);
	}
	@Test
	public void bodyCall_correct_java() throws Exception {
		Node source = NodeFactory.createURI("http://softlang.com/Parser/Correct.java");
		String resultingLanguage = "http://softlang.com/Java";
		Node[] env = setUpRuleContext(source);
		
		
		boolean result = parserPlugin.bodyCall(env,2 , ruleContext);
		Node languageNode = ruleContext.getEnv().getGroundVersion(env[1]);
		
		assertTrue(result);
		assertEquals(languageNode.getURI(), resultingLanguage);
	}
	@Test
	public void bodyCall_incorrect_java() throws Exception {
		Node source = NodeFactory.createURI("http://softlang.com/Parser/Incorrect.java");
		String resultingLanguage = "http://softlang.com/Java";
		Node[] env = setUpRuleContext(source);
		
		boolean result = parserPlugin.bodyCall(env,2 , ruleContext);
		
		assertFalse(result);
	}
	
	@Test
	public void bodyCall_incorrect_xml() throws Exception {
		Node source = NodeFactory.createURI("http://softlang.com/pom_wrong_syntax.xml");
		String resultingLanguage = "http://softlang.com/XML";
		Node[] env = setUpRuleContext(source);
		
		boolean result = parserPlugin.bodyCall(env,2 , ruleContext);
		
		assertFalse(result);
	}
	
	@Test
	public void bodyCall_incorrect_xsd() throws Exception {
		Node source = NodeFactory.createURI("http://softlang.com/pom_withXML.xsd");
		Node[] env = setUpRuleContext(source);
		
		boolean result = parserPlugin.bodyCall(env,2 , ruleContext);
		
		assertFalse(result);
	}
}
