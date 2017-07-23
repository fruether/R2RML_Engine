package Services.ServiceExtensions;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by freddy on 23.07.17.
 */
public class ArtifactDetectionExtensionTest {
	private ArtifactDetectionExtension artifactDetectionExtension;
	
	@Before
	public void setUp() {
		artifactDetectionExtension = new ArtifactDetectionExtension();
	}
	@Test
	public void test_apply_correct()  {
		ArrayList<String> files = new ArrayList<String >();
		String technology = "maven";
		
		files.add("maven-4.0.0.xsd");
		files.add("File.java");
		
		List<String> result = artifactDetectionExtension.apply("/Maven", files, technology);
		
		String expectedResult1 = technology + ":" +  files.get(0) + " rdf:type " + "sl:Artefact"  + " .";
		String expectedResult2 = technology + ":" +  files.get(1) + " rdf:type " + "sl:Artefact"  + " .";
		
		assertTrue(result.size() == 2);
		assertEquals(result.get(0), expectedResult1);
		assertEquals(result.get(1), expectedResult2);
		
	}
	
}
