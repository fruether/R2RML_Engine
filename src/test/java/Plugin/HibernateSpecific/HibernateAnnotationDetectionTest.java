package Plugin.HibernateSpecific;

import Services.FileRetrievementService;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.LPBackwardRuleInfGraph;
import org.apache.jena.reasoner.rulesys.impl.BBRuleContext;
import org.apache.jena.reasoner.rulesys.impl.BindingVector;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by freddy on 23.10.17.
 */
public class HibernateAnnotationDetectionTest {
	private HibernateAnnotationDetection hibernateAnnotationDetection;
	private BBRuleContext ruleContext;
	
	
	@Before
	public void setUp() {
		hibernateAnnotationDetection = new HibernateAnnotationDetection();
		FileRetrievementService.getInstance().setDataPath("src/test/resources/");
		ruleContext = new BBRuleContext(new LPBackwardRuleInfGraph(null, null, null, new LPBackwardRuleInfGraph(null, null, null, null)));
		
	}
	
	@Test
	public void get_annoation_with_name() {
		Node javaFile = NodeFactory.createURI("http://softlang.com/HibernateAnnotation/Libro.java");
		Node[] env = new Node[] {javaFile};
		BindingEnvironment bindingEnvironment = new BindingVector(env);
		ruleContext.setEnv(bindingEnvironment);
		
		//hibernateAnnotationDetection.headAction(env, 1, ruleContext);
		
		//Triple x = new Triple(javaFile, NodeFactory.createURI("http://softlang.de/refersTo"), NodeFactory.createURI("http://softlang.de/Table/cont_libro"));
	}
	
	@Test
	public void get_annoation_without_name() {
		Node javaFile = NodeFactory.createURI("http://softlang.com/HibernateAnnotation/AFETipoDescuento.java");
		Node[] env = new Node[] {javaFile};
		BindingEnvironment bindingEnvironment = new BindingVector(env);
		ruleContext.setEnv(bindingEnvironment);
		
		//hibernateAnnotationDetection.headAction(env, 1, ruleContext);
		
		//Triple x = new Triple(javaFile, NodeFactory.createURI("http://softlang.de/refersTo"), NodeFactory.createURI("http://softlang.de/Table/AFETipoDescuento"));
	}
	
	
	@Test
	public void get_annoation_with_only_entity() {
		Node javaFile = NodeFactory.createURI("http://softlang.com/HibernateAnnotation/SecurityToken.java");
		Node[] env = new Node[] {javaFile};
		BindingEnvironment bindingEnvironment = new BindingVector(env);
		ruleContext.setEnv(bindingEnvironment);
		
		//hibernateAnnotationDetection.headAction(env, 1, ruleContext);
		
		//Triple x = new Triple(javaFile, NodeFactory.createURI("http://softlang.de/refersTo"), NodeFactory.createURI("http://softlang.de/Table/AFETipoDescuento"));
	}
	@Test
	public void get_annoation_3() {
		Node javaFile = NodeFactory.createURI("http://softlang.com/HibernateAnnotation/GroupMembership.java");
		Node[] env = new Node[] {javaFile};
		BindingEnvironment bindingEnvironment = new BindingVector(env);
		ruleContext.setEnv(bindingEnvironment);
		
//		hibernateAnnotationDetection.headAction(env, 1, ruleContext);
		
		//Triple x = new Triple(javaFile, NodeFactory.createURI("http://softlang.de/refersTo"), NodeFactory.createURI("http://softlang.de/Table/AFETipoDescuento"));
	}
}
