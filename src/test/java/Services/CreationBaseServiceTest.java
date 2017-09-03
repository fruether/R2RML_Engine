package Services;

import Services.ServiceExtensions.ArtifactDetectionExtension;
import Services.ServiceExtensions.BuildReleaseExtension;
import Services.ServiceExtensions.PluginManagerExtension;
import Services.ServiceExtensions.PrefixCreationExtension;
import org.apache.jena.base.Sys;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by freddy on 30.07.17.
 */
public class CreationBaseServiceTest {
	private CreationBaseService creationBaseService;
	
	@Before
	public void setUp() {
		creationBaseService = new CreationBaseService();
		creationBaseService.setPath("src/test/resources/");
		
	}
	
	@Test
	public void getFileNames_test_oneDirectory() throws Exception {
		
		List<String> files = creationBaseService.getFileNames("input");
		
		assertEquals(4, files.size());
		assertTrue(files.contains("pom.xml"));
		assertTrue(files.contains("liquibase-update-to-latest.xml"));
		
	}
	
	@Test
	public void writeOutFile() throws Exception {
	}
	
	@Test
	public void test_addExtension_containing() throws Exception {
		creationBaseService.addExtension(new BuildReleaseExtension());
		
		creationBaseService.addExtension(new BuildReleaseExtension());
		
		assertEquals(1, creationBaseService.serviceExtensions.size());
	}
	
	@Test
	public void test_addExtension_not_containing() throws Exception {
		creationBaseService.addExtension(new BuildReleaseExtension());
		
		creationBaseService.addExtension(new ArtifactDetectionExtension());
		
		assertEquals(2, creationBaseService.serviceExtensions.size());
		
	}
	
	@Test
	public void test_copyExtensions() {
		CreationBaseService source = new CreationBaseService();
		source.addExtension(new ArtifactDetectionExtension(), new BuildReleaseExtension(), new PrefixCreationExtension());
		CreationBaseService target = new CreationBaseService();
		
		source.copyExtensions(target);
		
		assertEquals(source.serviceExtensions.size(), target.serviceExtensions.size());
		
	}
	
}
