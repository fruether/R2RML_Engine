package Services.ServiceExtensions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by freddy on 24.07.17.
 */
public class PrefixCreationExtension extends PluginManagerExtension {
	
	@Override
	public List<String> apply(String path, List<String> files, String technology) {
		List<String> result = new ArrayList<String>();
		
		if(technology == null) return result;
		
		String prefix = "@prefix " + technology.toLowerCase() + ":";
		prefix+= " <http://softlang.com/" + path.toLowerCase() + (path.endsWith("/") ? "" : "/") +  "> .";
		result.add(prefix);
		
		return result;
	}
	
	@Override
	public String getName() {
		return "prefix";
	}
}
