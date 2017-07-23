package Services;

import org.apache.jena.base.Sys;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.riot.RDFDataMgr;
import sun.plugin2.main.server.Plugin;

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
	private String megaFile;
	private String pluginPathBase;
	
	public PluginManagmentService(){
		
		plugins = new ArrayList<String>();
		basePath = "src/main/resources/";
		megaFile= "input.ttl";
		pluginPathBase = "Plugins/";
	}
	
	public static PluginManagmentService getInstance() {
		if (pluginManagmentService == null) {
			pluginManagmentService = new PluginManagmentService();
		}
		return pluginManagmentService;
	}
	
	
	public void createArtifactsInPlugin() {
		if(plugins.isEmpty()) {
			getPlugins();
		}
		for(String plugin : plugins) {
			int magaFileNameLength = (megaFile.length() + 1);
			
			String pluginPath = plugin.substring(0, plugin.length() - magaFileNameLength);
			String technologyName = plugin.substring(pluginPathBase.length(), plugin.length() - magaFileNameLength);

			List<String> files = getFileNames(pluginPath);
			for(String file : files) {
				System.out.println("Found the following file: " + file);
				
				StringBuilder result = new StringBuilder(file);
				result = result.append(" partOf ").append(technologyName).append(" .").append(System.getProperty("line.separator"));
				result = result.append(file).append(" rdf:type ").append("Artifact .");
				System.out.println(result.toString());
			}
		}
	}
	private List<String> getFileNames(String path) {
		List<String> files = new ArrayList<String>();
		try {
			Files.walk(Paths.get(basePath + path))
					.filter(Files::isRegularFile)
					.filter(f -> !f.endsWith(megaFile))
					.map(f -> f.getFileName())
					.map(f -> f.toString())
					.forEach(s -> files.add(s));
		}
		catch (IOException io) {
			System.out.println("Error while reading " + path + ". The IO Exception says the following " + io.getMessage());
		}
		finally {
			return files;
		}
	}
	
	
	public void setPath(String basePath){
		this.basePath = basePath;
	}
	
	public List<String> getPlugins() {
		try {
			Files.walk(Paths.get(basePath + pluginPathBase))
					.filter(Files::isRegularFile)
					//.forEach(System.out::println)
					.filter(f->f.endsWith(megaFile))
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
	
	public void addPluginsInfModel(InfModel infModel) {
		if(plugins.isEmpty()) {
			getPlugins();
		}
		for(String plugin : plugins) {
			RDFDataMgr.read(infModel, plugin);
			System.out.println("I just added the following pluging " + plugin);
		}
	}
}
