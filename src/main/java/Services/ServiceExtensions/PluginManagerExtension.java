package Services.ServiceExtensions;

import Services.PluginManagmentService;

import java.util.List;

/**
 * Created by freddy on 23.07.17.
 */
public abstract class PluginManagerExtension {
	
	protected String basePath;
	public abstract List<String> apply(String path, List<String> files, String technology);
	public abstract String getName();
	
	public void setBasePath(String path) {
		basePath = path;
	}
	
	public  boolean equals(Object obj) {
		if(!(obj instanceof PluginManagerExtension)) return false;
		PluginManagerExtension targetPluginManagerExtension = (PluginManagerExtension) obj;
		return targetPluginManagerExtension.getName().equals(getName());
	}
	
	protected String pathToURI(String path) {
		
		if(path.startsWith("."))
			path = "\\" + path;
		
		String result =  path.replace("/", "\\/");
		result =  result.replace("(", "%28").replace(")", "%29");
		result = result.replace(" ", "%20");
		result = result.replace("@", "%40");
		result = result.replace("$", "%24");
		result = result.replace("~", "%7e");
		return result;
	}
}
