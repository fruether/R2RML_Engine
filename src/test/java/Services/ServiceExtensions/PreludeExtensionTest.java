package Services.ServiceExtensions;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by freddy on 30.07.17.
 */
public class PreludeExtensionTest {
	
	private PreludeExtension preludeExtension = new PreludeExtension();
	@Test
	public void test_apply_correct() throws Exception {
		
		List<String> results = preludeExtension.apply(null, null, null);
		
		assertEquals(results.size(), 4);
		assertTrue(results.contains("@prefix sl: <http://softlang.com/> ."));
		assertTrue(results.contains("@prefix rdfs:   <http://www.w3.org/2000/01/rdf-schema> ."));
		assertTrue(results.contains("@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ."));
		assertTrue(results.contains("@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ."));
		assertTrue(results.contains("@prefix technologies: <http://softlang.com/Plugins/> ."));
		
	}
	
	@Test
	public void test_getName_correct() throws Exception {
		assertEquals(preludeExtension.getName(), "PreludeExtension");
	}
	
}
