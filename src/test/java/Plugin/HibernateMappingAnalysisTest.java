package Plugin;

import Services.FileRetrievementService;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
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
	
	@Test
	public void hibernate_mapping_with_package_correct() {
		String foundClass  = "http://softlang.com/Class/uk.org.rbc1b.roms.db.volunteer.qualification.Qualification";
		Node mapping_file = NodeFactory.createURI("http://softlang.com/Qualification.hbm.xml");
		Node referencing_url = NodeFactory.createURI(foundClass);
		Node[] env = new Node[] {mapping_file, referencing_url};
		
		BindingEnvironment bindingEnvironment = new BindingVector(env);
		ruleContext.setEnv(bindingEnvironment);
		
		boolean foundMapping = hibernateMappingAnalysis.bodyCall(env, 2, null);
		//Node classNode = ruleContext.getEnv().getGroundVersion(env[1]);
		
		
		//String result = classNode.toString();
		
		assertTrue(foundMapping);
	}
	
	@Test
	public void hibernate_mapping_with_package_wrong() {
		String foundClass  = "http://softlang.com/Class/uk.org.rbc1b.roms.db.volunteer.qualification.Peter";
		Node mapping_file = NodeFactory.createURI("http://softlang.com/Qualification.hbm.xml");
		Node referencing_url = NodeFactory.createURI(foundClass);
		Node[] env = new Node[] {mapping_file, referencing_url};
		
		BindingEnvironment bindingEnvironment = new BindingVector(env);
		ruleContext.setEnv(bindingEnvironment);
		
		boolean foundMapping = hibernateMappingAnalysis.bodyCall(env, 2, null);
		assertFalse(foundMapping);
	}
	
	@Test
	public void hibernate_mapping_without_package_correct() {
		String foundClass  = "http://softlang.com/Class/org.openmrs.Allergy";
		Node mapping_file = NodeFactory.createURI("http://softlang.com/Allergy.hbm.xml");
		Node referencing_url = NodeFactory.createURI(foundClass);
		Node[] env = new Node[] {mapping_file, referencing_url};
		
		BindingEnvironment bindingEnvironment = new BindingVector(env);
		ruleContext.setEnv(bindingEnvironment);
		
		boolean foundMapping = hibernateMappingAnalysis.bodyCall(env, 2, null);
		//Node classNode = ruleContext.getEnv().getGroundVersion(env[1]);
		
		
		//String result = classNode.toString();
		
		assertTrue(foundMapping);
	}
	
	@Test
	public void hibernate_mapping_without_package_wrong() {
		String foundClass  = "http://softlang.com/Class/org.openmrs.Allergy2";
		Node mapping_file = NodeFactory.createURI("http://softlang.com/Allergy.hbm.xml");
		Node referencing_url = NodeFactory.createURI(foundClass);
		Node[] env = new Node[] {mapping_file, referencing_url};
		
		BindingEnvironment bindingEnvironment = new BindingVector(env);
		ruleContext.setEnv(bindingEnvironment);
		
		boolean foundMapping = hibernateMappingAnalysis.bodyCall(env, 2, null);
		
		assertFalse(foundMapping);
	}
	
}
