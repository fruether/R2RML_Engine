package Services;

import org.apache.jena.base.Sys;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by freddy on 09.07.17.
 */
public class PluginManagmentService {
	static private PluginManagmentService pluginManagmentService;
	private List<String> plugins;
	private String basePath;
	
	public PluginManagmentService(){
		
		plugins = new ArrayList<String>();
		basePath = "src/main/resources/";
	}
	
	public static PluginManagmentService getInstance() {
		if (pluginManagmentService == null) {
			pluginManagmentService = new PluginManagmentService();
		}
		return pluginManagmentService;
	}
	
	public void setPath(String basePath){
		this.basePath = basePath;
	}
	
	public List<String> getPlugins() {
		try {
			Files.walk(Paths.get(basePath + "Plugins/"))
					.filter(Files::isRegularFile)
					//.forEach(System.out::println)
					.filter(f->f.endsWith("input.ttl"))
					.map(f -> f.toString())
					.map(s->s.replace(basePath, ""))
					.forEach(s->plugins.add(s));
		}
		catch (IOException e) {
			System.out.println("Exception");
			e.printStackTrace();
		}
		return plugins;
	}
}
