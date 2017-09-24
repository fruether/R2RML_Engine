package Plugin;

import Plugin.JavaSpecific.CheckImport;
import Services.FileRetrievementService;
import org.apache.jena.graph.NodeFactory;
import org.junit.Before;
import org.junit.Test;
import org.apache.jena.graph.Node;
import static org.junit.Assert.*;

/**
 * Created by freddy on 24.09.17.
 */
public class CheckImportTest {
	private CheckImport checkImport;
	@Before
	public void setUp() {
		checkImport = new CheckImport();
		FileRetrievementService.getInstance().setDataPath("src/test/resources/");
	}
	
	@Test
	public void getName() throws Exception {
		assertEquals(checkImport.getArgLength(), 2);
	}

	
	@Test
	public void correct_execution_without_cache()  {
		Node inputPath = NodeFactory.createURI("http://softlang.com/Java/SampleClass.java");
		Node packageName = NodeFactory.createURI("http://softlang.com/Package/junit.extensions.ActiveTestSuite");
		Node[] env = new Node[] {inputPath, packageName};
		
		
		
		boolean result = checkImport.bodyCall(env, 2, null);
		
		assertTrue(result);
		
	}
	@Test
	public void correct_execution_with_cache_different_apis() {
		Node inputPath = NodeFactory.createURI("http://softlang.com/Java/SampleClass.java");
		Node packageName1 = NodeFactory.createURI("http://softlang.com/Package/junit.extensions.ActiveTestSuite");
		Node packageName2 = NodeFactory.createURI("http://softlang.com/Package/java.util.List");
		
		Node[] env = new Node[] {inputPath, packageName1};
		
		// One call does just exist to check if the cache branch is working
		checkImport.bodyCall(env, 2, null);
		// Now the content should be cached
		env = new Node[] {inputPath, packageName2};
		boolean result = checkImport.bodyCall(env, 2, null);
		
		assertTrue(result);
	}
	@Test
	public void correct_execution_with_cache_same_apis() {
		Node inputPath = NodeFactory.createURI("http://softlang.com/Java/SampleClass.java");
		Node packageName = NodeFactory.createURI("http://softlang.com/Package/junit.extensions.ActiveTestSuite");
		
		Node[] env = new Node[] {inputPath, packageName};
		
		// One call does just exist to check if the cache branch is working
		boolean uncachedResult  =checkImport.bodyCall(env, 2, null);
		boolean cachedResult = checkImport.bodyCall(env, 2, null);
		
		assertEquals("Making sure cache and uncached give the same result", uncachedResult, cachedResult);
		assertTrue(uncachedResult);
	}
	
	@Test
	public void wrong_execution_without_cache() throws Exception {
		Node inputPath = NodeFactory.createURI("http://softlang.com/Java/SampleClass.java");
		Node packageName = NodeFactory.createURI("http://softlang.com/Package/junit.extensions.ActiveSuite");
		Node[] env = new Node[] {inputPath, packageName};
		
		boolean cachedResult = checkImport.bodyCall(env, 2, null);
		
		assertFalse(cachedResult);
	}
	
	@Test
	public void wrong_execution_with_cache_same_apis() {
		Node inputPath = NodeFactory.createURI("http://softlang.com/Java/SampleClass.java");
		Node packageName = NodeFactory.createURI("http://softlang.com/Package/junit.extensions.ActiveSuite");
		
		Node[] env = new Node[] {inputPath, packageName};
		
		// One call does just exist to check if the cache branch is working
		boolean uncachedResult  =checkImport.bodyCall(env, 2, null);
		boolean cachedResult = checkImport.bodyCall(env, 2, null);
		
		assertEquals("Making sure cache and uncached give the same result", uncachedResult, cachedResult);
		assertFalse(uncachedResult);
	}
	
}
