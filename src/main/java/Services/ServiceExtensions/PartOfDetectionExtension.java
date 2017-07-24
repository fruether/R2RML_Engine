package Services.ServiceExtensions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by freddy on 24.07.17.
 */
public class PartOfDetectionExtension extends PluginManagerExtension {
	
	@Override
	public List<String> apply(String path, List<String> files, String technology) {
		List<String> result  = new ArrayList<String >();
		
		for(String file : files) {
			StringBuilder builder = new StringBuilder(technology);
			builder.append(":").append(file).append(" ").append(getName()).append(" ").append("sl:").append(technology).append(" .");
			result.add(builder.toString());
		}
		return result;
	}
	
	@Override
	public String getName() {
		return "sl:partOf";
	}
}
