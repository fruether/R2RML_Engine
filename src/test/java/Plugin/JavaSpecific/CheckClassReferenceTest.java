package Plugin.JavaSpecific;

import Services.FileRetrievementService;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by freddy on 03.10.17.
 */
public class CheckClassReferenceTest {
	private  CheckClassReference checkClassReference;
	private FileRetrievementService fileRetrievementService;
	
	@Before
	public void setUp() {
		checkClassReference = new CheckClassReference();
		fileRetrievementService = FileRetrievementService.getInstance();
		fileRetrievementService.setDataPath("src/test/resources/");
	}
	@Test
	public void checkClassIs_references_no_cache() {
		Node javaFile = NodeFactory.createURI("http://softlang.com/Java/SampleClassDeclarations.java");
		Node className = NodeFactory.createURI("http://softlang.com/Class/java.lang.String");
		
		Node[] env = new Node[] {javaFile, className};
		
		boolean result = checkClassReference.bodyCall(env, 2, null);
		
		assertTrue(result);
	}
	@Test
	public void checkClassIs_references_cache() {
		Node javaFile = NodeFactory.createURI("http://softlang.com/Java/SampleClassDeclarations.java");
		Node className = NodeFactory.createURI("http://softlang.com/Class/java.lang.String");
		
		Node[] env = new Node[] {javaFile, className};
		
		checkClassReference.bodyCall(env, 2, null);
		Node className2 = NodeFactory.createURI("http://softlang.com/Class/java.util.LinkedHashMap");
		env = new Node[] {javaFile, className2};
		
		boolean result = checkClassReference.bodyCall(env, 2, null);
		
		
		assertTrue(result);
	}
	
	@Test
	public void checkClassIsNot_references_no_cache() {
		Node javaFile = NodeFactory.createURI("http://softlang.com/Java/SampleClassDeclarations.java");
		Node className = NodeFactory.createURI("http://softlang.com/Class/StringMap");
		
		Node[] env = new Node[] {javaFile, className};
		
		boolean result = checkClassReference.bodyCall(env, 2, null);
		
		assertFalse(result);
	}
	@Test
	public void checkClassIsNot_references_no_cache_withPackage() {
		Node javaFile = NodeFactory.createURI("http://softlang.com/Java/SampleClassDeclarations.java");
		Node className = NodeFactory.createURI("http://softlang.com/Class/java.util.LinkedHashMap");
		
		Node[] env = new Node[] {javaFile, className};
		
		boolean result = checkClassReference.bodyCall(env, 2, null);
		
		assertTrue(result);
	}
	
	@Test
	public void checkClassIs_referenced_no_cache_withPackage2() {
		Node javaFile = NodeFactory.createURI("http://softlang.com/Java/SampleClassDeclarations.java");
		Node className = NodeFactory.createURI("http://softlang.com/Class/java.lang.t.Object");
		
		Node[] env = new Node[] {javaFile, className};
		
		boolean result = checkClassReference.bodyCall(env, 2, null);
		
		assertTrue(result);
	}
	@Test
	public void checkClassIs_referenced_without_cache() {
		Node javaFile = NodeFactory.createURI("http://softlang.com/Java/MeasurementTypeDao.java");
		Node className = NodeFactory.createURI("http://softlang.com/Class/org.oscarehr.common.model.MeasurementType");
		
		Node[] env = new Node[] {javaFile, className};
		
		boolean result = checkClassReference.bodyCall(env, 2, null);
		
		assertTrue(result);
	}
	
	@Test
	public void checkClassIsReferenced_inLoop() {
		Node javaFile = NodeFactory.createURI("http://softlang.com/Java/LookupDao.java");
		Node className = NodeFactory.createURI("http://softlang.com/Class/com.quatro.model.FieldDefValue");
		
		Node[] env = new Node[] { javaFile, className };
		
		boolean result = checkClassReference.bodyCall(env, 2, null);
		
		assertTrue(result);
		
	}
	
	@Test
	public void checkClassIsReferenced_normalCase() {
		Node javaFile = NodeFactory.createURI("http://softlang.com/Java/SecPrivilegeDao.java");
		Node className = NodeFactory.createURI("http://softlang.com/Class/org.oscarehr.common.model.SecPrivilege");
		
		Node[] env = new Node[] { javaFile, className };
		
		boolean result = checkClassReference.bodyCall(env, 2, null);
		
		assertTrue(result);
		
	}
	
	
	@Test
	public void checkClassIsReferenced_normalCase2() {
		Node javaFile = NodeFactory.createURI("http://softlang.com/Java/Hl7TextInfoDao.java");
		Node className = NodeFactory.createURI("http://softlang.com/Class/org.oscarehr.common.model.Hl7TextMessageInfo2");
		
		Node[] env = new Node[] { javaFile, className };
		
		boolean result = checkClassReference.bodyCall(env, 2, null);
		
		assertTrue(result);
		
	}
	
	
	@Test
	public void checkClassIsReferenced_forLoopCase() {
		Node javaFile = NodeFactory.createURI("http://softlang.com/Java/HibernateCongregationDao.java");
		Node className = NodeFactory.createURI("http://softlang.com/Class/uk.org.rbc1b.roms.db.CongregationContact");
		
		Node[] env = new Node[] { javaFile, className };
		
		boolean result = checkClassReference.bodyCall(env, 2, null);
		
		assertTrue(result);
		
	}
	
	
}
