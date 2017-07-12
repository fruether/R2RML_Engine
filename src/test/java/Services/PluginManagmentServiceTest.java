package Services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by freddy on 12.07.17.
 */
public class PluginManagmentServiceTest {
	private PluginManagmentService pluginManagmentService;
	@Before
	public void setUp() throws Exception {
		pluginManagmentService = PluginManagmentService.getInstance();
		pluginManagmentService.setPath("src/test/resources/");
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void getPlugins_correct() throws Exception {
		List<String> plugins = pluginManagmentService.getPlugins();
		String[] resultArray = new String[] {"Plugins/Liquidbase/input.ttl", "Plugins/Maven/input.ttl" };
		assertArrayEquals(plugins.toArray(), resultArray);
	}
	
}
