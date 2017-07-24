package Services.ServiceExtensions;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by freddy on 24.07.17.
 */
public class PrefixCreationExtensionTest {
	private PrefixCreationExtension prefixCreationExtension;
	
	@Before
	public void setUp() {
		prefixCreationExtension = new PrefixCreationExtension();
	}
	@Test
	public void apply() throws Exception {
		String technology = "maven";
		String path = "Plugins/maven";
		
		List<String> result = prefixCreationExtension.apply(path, null, technology);
		
		assertEquals(result.size(),  1);
		assertEquals(result.get(0), "@prefix maven: <http://softlang.com/plugins/maven/> .");
		
	}
	
	@Test
	public void getName() throws Exception {
		
		assertEquals(prefixCreationExtension.getName(), "prefix");
	}
	
}
