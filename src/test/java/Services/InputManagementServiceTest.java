package Services;

import Services.ServiceExtensions.ArtifactDetectionExtension;
import Services.ServiceExtensions.BuildReleaseExtension;
import Services.ServiceExtensions.PartOfDetectionExtension;
import Services.ServiceExtensions.PrefixCreationExtension;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by freddy on 30.07.17.
 */
public class InputManagementServiceTest {
	private InputManagementService inputManagementService;
	
	@Before
	public void setUp() {
		inputManagementService = new InputManagementService("input", null);
		
		PluginManagmentService pluginManagmentService = PluginManagmentService.getInstance();
		pluginManagmentService.addExtension(new PrefixCreationExtension(), new BuildReleaseExtension(),
				new ArtifactDetectionExtension(), new PartOfDetectionExtension());
		pluginManagmentService.copyExtensions(inputManagementService);
		
	}
	@Test
	public void createInputFile() throws Exception {
		inputManagementService.createInputFile();
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
