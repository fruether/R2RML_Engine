package Services.ServiceExtensions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by freddy on 23.07.17.
 */
public class BuildReleaseExtension extends PluginManagerExtension {
	
	@Override
	public List<String> apply(String path, List<String> files, String technology) {
		if(technology == null || technology.isEmpty()) {
			return new ArrayList<>();
		}
		
		String entry = "sl:" + technology + " rdf:type " + getName() + " .";
		List<String> result = new ArrayList<String>();
		result.add(entry);
		return result;
	}
	
	@Override
	public String getName() {
		return "sl:BuildRelease";
	}
}
