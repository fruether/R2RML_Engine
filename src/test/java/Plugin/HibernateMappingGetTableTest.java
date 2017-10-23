package Plugin;

import Plugin.HibernateSpecific.HibernateMappingGetTable;
import Services.FileRetrievementService;
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
 * Created by freddy on 15.10.17.
 */
public class HibernateMappingGetTableTest {
	private HibernateMappingGetTable hibernateMappingGetTable;
	private BBRuleContext ruleContext;
	
	
	@Before
	public void setUp() {
		hibernateMappingGetTable = new HibernateMappingGetTable();
		FileRetrievementService.getInstance().setDataPath("src/test/resources/");
		ruleContext = new BBRuleContext(null);
	}
	
	private Node[] setUpRuleContext(Node source) {
		Node_RuleVariable empty = new Node_RuleVariable("table",1);
		Node[] env = new Node[]{source, empty};
		BindingEnvironment bindingEnvironment = new BindingVector(env);
		ruleContext.setEnv(bindingEnvironment);
		return env;
	}
	
	@Test
	public void table_name_correct_1() {
		String expectedTable = "http://softlang.com/Table/ALLERGY";
		Node inputFile = NodeFactory.createURI("http://softlang.com/Allergy.hbm.xml");
		Node[] env = setUpRuleContext(inputFile);
		
		boolean result = hibernateMappingGetTable.bodyCall(env, 2, ruleContext);
		
		assertTrue(result);
		
		String retrievedTable = ruleContext.getEnv().getGroundVersion(env[1]).getURI();
		assertEquals(expectedTable, retrievedTable);
	}
	
	@Test
	public void table_name_correct_2() {
		String expectedTable = "http://softlang.com/Table/FIXEDREPORT";
		Node inputFile = NodeFactory.createURI("http://softlang.com/FixedReport.hbm.xml");
		Node[] env = setUpRuleContext(inputFile);
		
		boolean result = hibernateMappingGetTable.bodyCall(env, 2, ruleContext);
		
		assertTrue(result);
		
		String retrievedTable = ruleContext.getEnv().getGroundVersion(env[1]).getURI();
		assertEquals(expectedTable, retrievedTable);
	}
	
	@Test
	public void table_name_wrong() {
		String expectedTable = "http://softlang.com/Table/FIXEDREPORT";
		Node inputFile = NodeFactory.createURI("http://softlang.com/pom.xml");
		Node[] env = setUpRuleContext(inputFile);
		
		boolean result = hibernateMappingGetTable.bodyCall(env, 2, ruleContext);
		
		assertFalse(result);
	}
	@Test
	public void table_name_correct_default() {
		String expectedTable = "http://softlang.com/Table/PROJECTAVAILABILITY";
		Node inputFile = NodeFactory.createURI("http://softlang.com/ProjectAvailability.hbm.xml");
		Node[] env = setUpRuleContext(inputFile);
		
		boolean result = hibernateMappingGetTable.bodyCall(env, 2, ruleContext);
		
		assertTrue(result);
		
		String retrievedTable = ruleContext.getEnv().getGroundVersion(env[1]).getURI();
		assertEquals(expectedTable, retrievedTable);
	}
}
