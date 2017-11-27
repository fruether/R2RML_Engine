package Plugin;

import Plugin.HibernateSpecific.HibernateMappingAnalysis;
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
 * Created by freddy on 08.10.17.
 */
public class HibernateMappingAnalysisTest {
	
	private HibernateMappingAnalysis hibernateMappingAnalysis;
	private BBRuleContext ruleContext;
	
	
	@Before
	public void setUp() {
		hibernateMappingAnalysis = new HibernateMappingAnalysis();
		FileRetrievementService.getInstance().setDataPath("src/test/resources/");
		ruleContext = new BBRuleContext(null);
	}
	
	private Node[] setUpRuleContext(Node source) {
		Node_RuleVariable empty = new Node_RuleVariable("class",1);
		Node[] env = new Node[]{source, empty};
		BindingEnvironment bindingEnvironment = new BindingVector(env);
		ruleContext.setEnv(bindingEnvironment);
		return env;
	}
	
	
	
	@Test
	public void hibernate_mapping_with_package_correct() {
		String expectedClass  = "http://softlang.com/ClassURI/uk.org.rbc1b.roms.db.volunteer.qualification.Qualification";
		Node mapping_file = NodeFactory.createURI("http://softlang.com/Qualification.hbm.xml");
		
		Node[] env = setUpRuleContext(mapping_file);
		
		
		boolean foundMapping = hibernateMappingAnalysis.bodyCall(env, 2, ruleContext);
		Node classNode = ruleContext.getEnv().getGroundVersion(env[1]);
		
		
		String calculatedClassUri = classNode.toString();
		
		assertTrue(foundMapping);
		assertEquals(calculatedClassUri, expectedClass);
	}
	
	@Test
	public void hibernate_mapping_with_package_wrong() {
		String foundClass  = "http://softlang.com/ClassURI/uk.org.rbc1b.roms.db.volunteer.qualification.Peter";
		Node mapping_file = NodeFactory.createURI("http://softlang.com/Qualification.hbm.xml");
		Node[] env = setUpRuleContext(mapping_file);
		
		BindingEnvironment bindingEnvironment = new BindingVector(env);
		ruleContext.setEnv(bindingEnvironment);
		
		boolean foundMapping = hibernateMappingAnalysis.bodyCall(env, 2, ruleContext);
		Node classNode = ruleContext.getEnv().getGroundVersion(env[1]);
		String result = classNode.toString();
		
		assertTrue(foundMapping);
		assertNotEquals(result, foundClass);
		
	}
	
	@Test
	public void hibernate_mapping_without_package_correct() {
		String foundClass  = "http://softlang.com/ClassURI/org.openmrs.Allergy";
		Node mapping_file = NodeFactory.createURI("http://softlang.com/Allergy.hbm.xml");

		Node[] env = setUpRuleContext(mapping_file);
		
		
		boolean foundMapping = hibernateMappingAnalysis.bodyCall(env, 2, ruleContext);
		Node classNode = ruleContext.getEnv().getGroundVersion(env[1]);
		String result = classNode.toString();
		
		assertTrue(foundMapping);
		assertEquals(foundClass, result);
	}
	
	@Test
	public void hibernate_mapping_without_package_wrong() {
		String foundClass  = "http://softlang.com/ClassURI/org.openmrs.Allergy2";
		Node mapping_file = NodeFactory.createURI("http://softlang.com/Allergy.hbm.xml");
		Node srfFile = NodeFactory.createURI(foundClass);
		
		Node[] env = new Node[] {mapping_file, srfFile};
		BindingEnvironment bindingEnvironment = new BindingVector(env);
		ruleContext.setEnv(bindingEnvironment);
		
		boolean foundMapping = hibernateMappingAnalysis.bodyCall(env, 2, ruleContext);
		
		assertFalse(foundMapping);
	}
	
	@Test
	public void hibernate_mapping_without_with_package_correct() {
		String foundClass  = "http://softlang.com/ClassURI/uk.org.rbc1b.roms.db.report.FixedReport";
		Node mapping_file = NodeFactory.createURI("http://softlang.com/FixedReport.hbm.xml");
		
		Node[] env = setUpRuleContext(mapping_file);
		
		
		boolean foundMapping = hibernateMappingAnalysis.bodyCall(env, 2, ruleContext);
		Node classNode = ruleContext.getEnv().getGroundVersion(env[1]);
		String result = classNode.toString();
		
		assertTrue(foundMapping);
		assertEquals(foundClass, result);
	}

}
