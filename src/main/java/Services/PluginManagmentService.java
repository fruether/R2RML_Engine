package Services;

import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.riot.RDFDataMgr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import Services.ServiceExtensions.PluginManagerExtension;

/**
 * Created by freddy on 09.07.17.
 */
public class PluginManagmentService {
	static private PluginManagmentService pluginManagmentService;
	private List<String> plugins;
	private String basePath;
	private String megaFile;
	private String pluginPathBase;
	private List<PluginManagerExtension> serviceExtensions;
	
	public PluginManagmentService(){
		
		plugins = new ArrayList<String>();
		basePath = "src/main/resources/";
		megaFile= "input.ttl";
		pluginPathBase = "Plugins/";
		serviceExtensions = new ArrayList<>();
	}
	public void addExtension(PluginManagerExtension... extensions) {
		for(PluginManagerExtension serviceExtension : extensions) {
			if (!serviceExtensions.contains(serviceExtension)) {
				serviceExtensions.add(serviceExtension);
			}
		}
	}
	
	public static PluginManagmentService getInstance() {
		if (pluginManagmentService == null) {
			pluginManagmentService = new PluginManagmentService();
		}
		return pluginManagmentService;
	}
	
	
	public void createArtifactsInPlugin() {
		List<String> expected = new ArrayList<>();
		
		if(plugins.isEmpty()) {
			getPlugins();
		}
		
		for(String plugin : plugins) {
			int magaFileNameLength = (megaFile.length() + 1);
			
			String pluginPath = plugin.substring(0, plugin.length() - magaFileNameLength);
			String technologyName = plugin.substring(pluginPathBase.length(), plugin.length() - magaFileNameLength);

			List<String> files = getFileNames(pluginPath);
			for(PluginManagerExtension extension : serviceExtensions) {
				expected.addAll(extension.apply(pluginPath, files, technologyName));
			}
		}
		for(String entry : expected)
			System.out.println(entry);
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
