package Services.ServiceExtensions;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by freddy on 24.07.17.
 */
public class PartOfDetectionExtensionTest {
	
	private PartOfDetectionExtension partOfDetectionExtension;
	
	@Before
	public void setUp() {
		partOfDetectionExtension = new PartOfDetectionExtension();
	}
	
	@Test
	public void test_apply_correct(){
		ArrayList<String> files = new ArrayList<String >();
		String technology = "maven";
		files.add("maven-4.0.0.xsd");
		files.add("File.java");
		
		List<String> result = partOfDetectionExtension.apply("", files, technology);
		
		assertEquals(result.size(), 2);
		assertEquals(result.get(0), "maven:maven-4.0.0.xsd sl:partOf sl:maven .");
		assertEquals(result.get(1), "maven:File.java sl:partOf sl:maven .");
	}
	
	@Test
	public void getName_correct() {
		
		assertEquals(partOfDetectionExtension.getName(), "sl:partOf");
	}
	
}
