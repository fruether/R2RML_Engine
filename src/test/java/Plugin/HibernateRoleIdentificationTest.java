package Plugin;

import Plugin.HibernateSpecific.HibernateRoleIdentification;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.Node_RuleVariable;
import org.apache.jena.reasoner.rulesys.impl.BBRuleContext;
import org.apache.jena.reasoner.rulesys.impl.BindingVector;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by freddy on 14.09.17.
 */
public class HibernateRoleIdentificationTest {
	private HibernateRoleIdentification hibernateRoleIdentification = new HibernateRoleIdentification();
	private BBRuleContext ruleContext;
	
	@Before
	public void setUp() {
		ruleContext = new BBRuleContext(null);
	}
	private Node[] setUpRuleContext(Node source) {
		Node_RuleVariable empty = new Node_RuleVariable("role",1);
		Node[] env = new Node[]{source, empty};
		BindingEnvironment bindingEnvironment = new BindingVector(env);
		ruleContext.setEnv(bindingEnvironment);
		return env;
	}
	
	@Test
	public void bodyCall_correct_role_assignment_mapping1() throws Exception {
		Node artefact = NodeFactory.createURI(" http://softlang.com/plugins/hibernate/hibernate-mapping-3.0.dtd");
		
		String resultingRole = "http://softlang.com/Plugins/Hibernate/HibernateMapping";
		Node[] env = setUpRuleContext(artefact);
		
		boolean result = hibernateRoleIdentification.bodyCall(env,2 , ruleContext);
		Node languageNode = ruleContext.getEnv().getGroundVersion(env[1]);
		
		assertTrue(result);
		assertEquals(languageNode.getURI(), resultingRole);
	}
	@Test
	public void bodyCall_correct_role_assignment_mapping2() throws Exception {
		Node artefact = NodeFactory.createURI("http://softlang.com/plugins/hibernate/hibernate-mapping-4.0.xsd");
		
		String resultingRole = "http://softlang.com/Plugins/Hibernate/HibernateMapping";
		Node[] env = setUpRuleContext(artefact);
		
		boolean result = hibernateRoleIdentification.bodyCall(env,2 , ruleContext);
		Node languageNode = ruleContext.getEnv().getGroundVersion(env[1]);
		
		assertTrue(result);
		assertEquals(languageNode.getURI(), resultingRole);
	}
	@Test
	public void bodyCall_correct_role_assignment_conf1() throws Exception {
		Node artefact = NodeFactory.createURI("http://softlang.com/plugins/hibernate/hibernate-configuration-3.0.dtd");
		
		String resultingRole = "http://softlang.com/Plugins/Hibernate/HibernateConfiguration";
		Node[] env = setUpRuleContext(artefact);
		
		boolean result = hibernateRoleIdentification.bodyCall(env,2 , ruleContext);
		Node languageNode = ruleContext.getEnv().getGroundVersion(env[1]);
		
		assertTrue(result);
		assertEquals(languageNode.getURI(), resultingRole);
	}
	@Test
	public void bodyCall_correct_role_assignment_wrong() throws Exception {
		Node artefact = NodeFactory.createURI("http://softlang.com/plugins/hibernate/hibernate-configuraadasf.xml");
		
		String resultingRole = "http://softlang.com/Plugins/Hibernate/HibernateConfiguration";
		Node[] env = setUpRuleContext(artefact);
		
		boolean result = hibernateRoleIdentification.bodyCall(env,2 , ruleContext);
		Node languageNode = ruleContext.getEnv().getGroundVersion(env[1]);
		
		assertFalse(result);
	}
}
