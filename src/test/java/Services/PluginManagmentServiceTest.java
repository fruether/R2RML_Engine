package Services;

import Services.ServiceExtensions.ArtifactDetectionExtension;
import Services.ServiceExtensions.BuildReleaseExtension;
import Services.ServiceExtensions.PartOfDetectionExtension;
import Services.ServiceExtensions.PrefixCreationExtension;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by freddy on 12.07.17.
 */
public class PluginManagmentServiceTest {
	
	private PluginManagmentService pluginManagmentService;
	private File mavenFile;
	@Before
	public void setUp() throws Exception {
		pluginManagmentService = PluginManagmentService.getInstance();
		pluginManagmentService.setPath("src/test/resources/");
		pluginManagmentService.addExtension(new PrefixCreationExtension(), new BuildReleaseExtension(),
				                            new ArtifactDetectionExtension(), new PartOfDetectionExtension());
		
		mavenFile = new File("src/test/resources/Plugins/Maven/input.ttl");
		mavenFile.createNewFile();
	}
	
	
	@Test
	public void test_getPlugins_correct() throws Exception {
		List<String> plugins = pluginManagmentService.getPlugins();
		String[] resultArray = new String[] {"Plugins/Liquidbase/input.ttl", "Plugins/maven/input.ttl" };
		assertArrayEquals(plugins.toArray(), resultArray);
	}
	
	@Test
	public void test_createPluginsOntology() {
		pluginManagmentService.createPluginsOntology();
		List<String> lines = null;
		try {
			lines = FileUtils.readLines(mavenFile, Charset.defaultCharset());
		}
		catch (IOException io) {
			assertTrue(io==null);
		}
		assertEquals(lines.size(), 10);
		assertTrue(lines.contains("maven:maven-4.0.0.xsd sl:partOf sl:maven ."));
		assertTrue(lines.contains("maven:maven-4.0.0.xsd rdf:type sl:Artefact ."));
		assertTrue(lines.contains("sl:maven rdf:type sl:BuildRelease ."));
		assertTrue(lines.contains("@prefix maven: <http://softlang.com/plugins/maven/> ."));
	}
	
	@Test
	public void test_createPluginsOntology_noChange() {
		List<String> old_lines = null;
		String path = "src/test/resources/Plugins/Liquidbase/input.ttl";
		File liquidFile = new File(path);
		try {
			old_lines = FileUtils.readLines(liquidFile, Charset.defaultCharset());
		}
		catch (IOException io) {
			assertTrue(io==null);
		}
		
		pluginManagmentService.createPluginsOntology();
		
		List<String> new_lines = null;
		try {
			new_lines = FileUtils.readLines(liquidFile, Charset.defaultCharset());
		}
		catch (IOException io) {
			assertTrue(io==null);
		}
		assertEquals(old_lines.size(), new_lines.size());
		assertArrayEquals(old_lines.toArray(), new_lines.toArray());
	}
}
