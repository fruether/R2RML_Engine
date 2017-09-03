package Services.ServiceExtensions;

import Services.PluginManagmentService;

import java.util.List;

/**
 * Created by freddy on 23.07.17.
 */
public abstract class PluginManagerExtension {
	
	public abstract List<String> apply(String path, List<String> files, String technology);
	public abstract String getName();
	
	public  boolean equals(Object obj) {
		if(!(obj instanceof PluginManagerExtension)) return false;
		PluginManagerExtension targetPluginManagerExtension = (PluginManagerExtension) obj;
		return targetPluginManagerExtension.getName().equals(getName());
	}
	
}
