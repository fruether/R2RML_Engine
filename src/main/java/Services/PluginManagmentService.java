package Services;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by freddy on 09.07.17.
 */
public class PluginManagmentService {
	static private PluginManagmentService pluginManagmentService;
	private List<String> plugins;
	
	public PluginManagmentService(){
		plugins = new ArrayList<String>();
	}
	
	public static PluginManagmentService getInstance() {
		if (pluginManagmentService == null) {
			pluginManagmentService = new PluginManagmentService();
		}
		return pluginManagmentService;
	}
	
	public List<String> getPlugins() {
		plugins.add("Plugins/Maven/input.ttl");
		return plugins;
	}
}
