package Services;

import Services.ServiceExtensions.ArtifactDetectionExtension;
import Services.ServiceExtensions.BuildReleaseExtension;
import Services.ServiceExtensions.PartOfDetectionExtension;
import Services.ServiceExtensions.PrefixCreationExtension;
import org.apache.commons.io.FileUtils;
import org.apache.jena.base.Sys;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by freddy on 30.07.17.
 */
public class InputManagementServiceTest {
	private InputManagementService inputManagementService;
	private String outputFile = "testOutput.ttl";
	private String inputPath = "input/";
	private String basePath = "src/test/resources/";
	
	@Before
	public void setUp() {
		inputManagementService = new InputManagementService(inputPath,  outputFile);
		
		PluginManagmentService pluginManagmentService = PluginManagmentService.getInstance();
		pluginManagmentService.addExtension(new PrefixCreationExtension(), new BuildReleaseExtension(),
				new ArtifactDetectionExtension(), new PartOfDetectionExtension());
		pluginManagmentService.copyExtensions(inputManagementService);
		
		inputManagementService.setPath(basePath);
		
	}
	@Test
	public void test_createInputFile_correct() throws Exception {
		List<String> lines;
		
		inputManagementService.createInputFile();
		lines = FileUtils.readLines(new File(basePath + inputPath + outputFile), Charset.defaultCharset());
		assertEquals(lines.size(), 19);
		
		String[] expected_result = new String[]{
				"@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .",
				"@prefix rdfs:   <http://www.w3.org/2000/01/rdf-schema> .",
				"@prefix sl: <http://softlang.com/> .",
				"@prefix openmrs: <http://softlang.com/input/> .",
				"sl:openmrs rdf:type sl:BuildRelease .",
				"openmrs:liquibase-update-to-latest.xml rdf:type sl:Artefact .",
				"openmrs:pom.xml rdf:type sl:Artefact .",
				"openmrs:liquibase-update-to-latest.xml sl:partOf sl:openmrs .",
				"openmrs:pom.xml sl:partOf sl:openmrs .",
				"openmrs:inputHibernate.xml rdf:type sl:Artefact .",
				"openmrs:liquibase-update-to-latest.xml rdf:type sl:Artefact .",
				"@prefix technologies: <http://softlang.com/Plugins/> ."

		};
		
		for(String value : expected_result) {
			assertTrue(lines.contains(value));
		}
		
	}
	
	
}
