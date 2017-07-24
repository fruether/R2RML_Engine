package Services.ServiceExtensions;

import Services.PluginManagmentService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by freddy on 23.07.17.
 */
public class ArtifactDetectionExtension extends PluginManagerExtension {
	
	@Override
	public List<String> apply(String path, List<String> files, String technology) {
		List<String> expected = new ArrayList<>();
		for(String file : files) {
			StringBuilder result = new StringBuilder(technology);
			result = result.append(":").append(file).append(" rdf:type ").append(getName()).append(" .");
			expected.add(result.toString());
		}
		
		return expected;
	}
	
	@Override
	public String getName() {
		return "sl:Artefact";
	}
}
