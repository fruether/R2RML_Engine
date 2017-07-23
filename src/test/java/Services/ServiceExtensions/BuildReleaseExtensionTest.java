package Services.ServiceExtensions;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by freddy on 23.07.17.
 */
public class BuildReleaseExtensionTest {
	private BuildReleaseExtension buildReleaseExtension;
	
	@Before
	public void setUp(){
		buildReleaseExtension = new BuildReleaseExtension();
	}
	@Test
	public void test_apply_ok() {
		String technology = "maven";
		
		List<String> result = buildReleaseExtension.apply("Maven", null, technology);
		
		assertTrue(result.size() == 1);
		assertEquals(result.get(0), "sl:maven rdf:type sl:BuildRelease .");
		
	}
	
	@Test
	public void test_apply_wrong()  {
		String technology = "";
		
		List<String> result = buildReleaseExtension.apply("Maven", null, technology);
		
		assertTrue(result.isEmpty());
		
	}
	
}
