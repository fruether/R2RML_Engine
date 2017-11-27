package Plugin.SQLSpecific;

import Services.FileRetrievementService;
import Services.SQLService;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by freddy on 30.10.17.
 */
public class CreateStmtExtractionTest {
	private CreateStmtExtraction createStmtExtraction;
	
	@Before
	public void setUp() {
		createStmtExtraction = new CreateStmtExtraction();
		FileRetrievementService.getInstance().setDataPath("src/test/resources/");
	}
	@Test
	public void normal_sql_file() {
		Node uri = NodeFactory.createURI("http://softlang.com/SQL/drugref.sql");
		Node[] env = new Node[] {uri};
		
		createStmtExtraction.headAction(env, 1, null);
	}
	@Test
	public void normal_sql_file_2() {
		Node uri = NodeFactory.createURI("http://softlang.com/SQL/initcaisi.sql");
		Node[] env = new Node[] {uri};
		
		createStmtExtraction.headAction(env, 1, null);
	}
	
	@Test
	public void normal_sql_file_3() {
		Node uri = NodeFactory.createURI("http://softlang.com/SQL/patch-2008-12-06-2-quatromerge.sql");
		Node[] env = new Node[] {uri};
		
		createStmtExtraction.headAction(env, 1, null);
	}
	
}
