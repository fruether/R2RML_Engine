package Plugin.HibernateSpecific;

import Services.FileRetrievementService;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.reasoner.rulesys.LPBackwardRuleInfGraph;
import org.apache.jena.reasoner.rulesys.impl.BBRuleContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by freddy on 01.01.18.
 */
public class CheckHibernateMethodUsageTest {
	private CheckHibernateMethodUsage hibernateAnnotationDetection;
	@Before
	public void setUp() {
		hibernateAnnotationDetection = new CheckHibernateMethodUsage();
		FileRetrievementService.getInstance().setDataPath("src/test/resources/");
	}
	@Test
	public void test_dao_demographicAccessory() {
		Node uri = NodeFactory.createURI("http://softlang.com/Java/DemographicAccessoryDao.java");
		Node[] env = new Node[]{uri};
		boolean result = hibernateAnnotationDetection.bodyCall(env, 1, null);
		assertTrue(result);
	}
	
	@Test
	public void test_dao_EntradaDaoHibernate() {
		Node uri = NodeFactory.createURI("http://softlang.com/Java/EntradaDaoHibernate.java");
		Node[] env = new Node[]{uri};
		boolean result = hibernateAnnotationDetection.bodyCall(env, 1, null);
		assertTrue(result);
	}
	
	@Test
	public void test_dao_hibernateCongregationDao() {
		Node uri = NodeFactory.createURI("http://softlang.com/Java/HibernateCongregationDao.java");
		Node[] env = new Node[]{uri};
		boolean result = hibernateAnnotationDetection.bodyCall(env, 1, null);
		assertTrue(result);
	}
	
	
	@Test
	public void test_dao_hibernateEmailDao() {
		Node uri = NodeFactory.createURI("http://softlang.com/Java/HibernateEmailDao.java");
		Node[] env = new Node[]{uri};
		boolean result = hibernateAnnotationDetection.bodyCall(env, 1, null);
		assertTrue(result);
	}
	
	
	@Test
	public void test_dao_Hl7TextInfoDao() {
		Node uri = NodeFactory.createURI("http://softlang.com/Java/Hl7TextInfoDao.java");
		Node[] env = new Node[]{uri};
		boolean result = hibernateAnnotationDetection.bodyCall(env, 1, null);
		assertTrue(result);
	}
	
	
	@Test
	public void test_dao_MdsZLBDao() {
		Node uri = NodeFactory.createURI("http://softlang.com/Java/MdsZLBDao.java");
		Node[] env = new Node[]{uri};
		boolean result = hibernateAnnotationDetection.bodyCall(env, 1, null);
		assertFalse(result);
	}
	
	
	@Test
	public void test_dao_LookupDao() {
		Node uri = NodeFactory.createURI("http://softlang.com/Java/LookupDao.java");
		Node[] env = new Node[]{uri};
		boolean result = hibernateAnnotationDetection.bodyCall(env, 1, null);
		assertTrue(result);
	}
	
	@Test
	public void test_dao_abstractDao() {
		Node uri = NodeFactory.createURI("http://softlang.com/Java/AbstractDao.java");
		Node[] env = new Node[]{uri};
		boolean result = hibernateAnnotationDetection.bodyCall(env, 1, null);
		assertTrue(result);
	}
	
	@Test
	public void test_dao_measurementTypeDao() {
		Node uri = NodeFactory.createURI("http://softlang.com/Java/MeasurementTypeDao.java");
		Node[] env = new Node[]{uri};
		boolean result = hibernateAnnotationDetection.bodyCall(env, 1, null);
		assertTrue(result);
	}
	
	@Test
	public void test_dao_migracionDaoHibernate() {
		Node uri = NodeFactory.createURI("http://softlang.com/Java/MigracionDaoHibernate.java");
		Node[] env = new Node[]{uri};
		boolean result = hibernateAnnotationDetection.bodyCall(env, 1, null);
		assertTrue(result);
	}
	
	
	@Test
	public void test_dao_queueDocumentLink() {
		Node uri = NodeFactory.createURI("http://softlang.com/Java/QueueDocumentLink.java");
		Node[] env = new Node[]{uri};
		boolean result = hibernateAnnotationDetection.bodyCall(env, 1, null);
		assertFalse(result);
	}
	
	@Test
	public void test_dao_sampleClassDeclarations() {
		Node uri = NodeFactory.createURI("http://softlang.com/Java/SampleClassDeclarations.java");
		Node[] env = new Node[]{uri};
		boolean result = hibernateAnnotationDetection.bodyCall(env, 1, null);
		assertFalse(result);
	}
	
	@Test
	public void test_dao_SecPrivilegeDao() {
		Node uri = NodeFactory.createURI("http://softlang.com/Java/SecPrivilegeDao.java");
		Node[] env = new Node[]{uri};
		boolean result = hibernateAnnotationDetection.bodyCall(env, 1, null);
		assertTrue(result);
	}
	
	@Test
	public void test_dao_germplasmListManagerImpl() {
		Node uri = NodeFactory.createURI("http://softlang.com/Java/GermplasmListManagerImpl.java");
		Node[] env = new Node[]{uri};
		boolean result = hibernateAnnotationDetection.bodyCall(env, 1, null);
		assertTrue(result);
	}
	
	@Test
	public void test_dao_methodDAO() {
		Node uri = NodeFactory.createURI("http://softlang.com/Java/MethodDAO.java");
		Node[] env = new Node[]{uri};
		boolean result = hibernateAnnotationDetection.bodyCall(env, 1, null);
		assertTrue(result);
	}
	
	@Test
	public void test_temporadaColportorDao() {
		Node uri = NodeFactory.createURI("http://softlang.com/Java/TemporadaColportorDao.java");
		Node[] env = new Node[]{uri};
		boolean result = hibernateAnnotationDetection.bodyCall(env, 1, null);
		assertTrue(result);
	}
	
	@Test
	public void test_temporadaDao() {
		Node uri = NodeFactory.createURI("http://softlang.com/Java/TemporadaDao.java");
		Node[] env = new Node[]{uri};
		boolean result = hibernateAnnotationDetection.bodyCall(env, 1, null);
		assertTrue(result);
	}
	@Test
	public void test_simpleAuthorizationManager() {
		Node uri = NodeFactory.createURI("http://softlang.com/Java/SimpleAuthorizationManager.java");
		Node[] env = new Node[]{uri};
		boolean result = hibernateAnnotationDetection.bodyCall(env, 1, null);
		assertTrue(result);
	}
	@Test
	public void test_teamEventUtilsTest() {
		Node uri = NodeFactory.createURI("http://softlang.com/Java/TeamEventUtilsTest.java");
		Node[] env = new Node[]{uri};
		boolean result = hibernateAnnotationDetection.bodyCall(env, 1, null);
		assertFalse(result);
	}
	
	

}



