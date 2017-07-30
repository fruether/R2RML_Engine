package Services;

import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.riot.RDFDataMgr;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Services.ServiceExtensions.PluginManagerExtension;


/**
 * Created by freddy on 09.07.17.
 */
public class PluginManagmentService extends CreationBaseService{
	static private PluginManagmentService pluginManagmentService;
	private List<String> plugins;
	private String pluginPathBase;
	private Map<String, List<String>> pluginExpectedElements;
	
	public PluginManagmentService(){
		plugins = new ArrayList<String>();
		pluginPathBase = "Plugins/";
		pluginExpectedElements  = new HashMap<>();
	}

	
	public static PluginManagmentService getInstance() {
		if (pluginManagmentService == null) {
			pluginManagmentService = new PluginManagmentService();
		}
		return pluginManagmentService;
	}
	
	
	public void createPluginsOntology() {
		if(plugins.isEmpty()) {
			getPlugins();
		}
		createExpectedVector();
		createActualVector();
	}
	
	private void createExpectedVector() {
		List<String> expectedElements;
		
		
		for(String plugin : plugins) {
			expectedElements  = new ArrayList<>();
			int megaFileNameLength = (megaFile.length() + 1);
			
			String pluginPath = plugin.substring(0, plugin.length() - megaFileNameLength);
			String technologyName = plugin.substring(pluginPathBase.length(), plugin.length() - megaFileNameLength).toLowerCase();
			
			List<String> files = getFileNames(pluginPath);
			for(PluginManagerExtension extension : serviceExtensions) {
				expectedElements.addAll(extension.apply(pluginPath, files, technologyName));
				expectedElements.add(" ");
			}
			pluginExpectedElements.put(plugin, expectedElements);
		}
	}
	
	private void createActualVector() {
		for(String plugin : plugins) {
			
			List<String> actualContent = readFileToVector(basePath + plugin);
			List<String> expectedContent = pluginExpectedElements.get(plugin);
			Iterator<String> iter = expectedContent.iterator();
			
			while (iter.hasNext()) {
				String value = iter.next();
				if(actualContent.contains(value)) {
					iter.remove();
				}
			}
			System.out.println("The following amount of elements have to be added " + expectedContent.size() + " to the plugin " + plugin);
			iter = expectedContent.iterator();
			while (iter.hasNext()) {
				String value = iter.next();
				if(value.startsWith("@prefix")) {
					actualContent.add(0, value);
				}
				else {
					actualContent.add(value);
				}
			}
			writeOutFile(basePath+plugin, actualContent);
		}
	}
	
	private List<String> readFileToVector(String path) {
		List<String> result = new ArrayList<>();
		try {
			result = org.apache.commons.io.FileUtils.readLines(new File(path), Charset.forName("utf-8"));
		}
		catch (IOException ioe) {
			System.out.println("Not able to read the file " + path + " because of " + ioe.getMessage());
		}
		finally {
			return result;
		}
	}
	
	public void setPath(String basePath){
		this.basePath = basePath;
	}
	
	public List<String> getPlugins() {
		if(!plugins.isEmpty()) plugins.clear();
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
			System.out.println("Exception: " + basePath + pluginPathBase);
			e.printStackTrace();
		}
		return plugins;
	}
	
	public void addPluginsInfModel(InfModel infModel) {
		if(plugins.isEmpty()) {
			getPlugins();
		}
		for(String plugin : plugins) {
			System.out.println("Load the file " + plugin);
			RDFDataMgr.read(infModel, basePath+plugin);
			System.out.println("I just added the following plugin " + plugin);
		}
	}
}
