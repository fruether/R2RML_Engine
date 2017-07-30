package Services;

import Services.ServiceExtensions.PluginManagerExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by freddy on 25.07.17.
 */
public class CreationBaseService {
	
	protected String basePath = "";
	protected List<PluginManagerExtension> serviceExtensions;
	protected String megaFile;
	
	public CreationBaseService() {
		basePath = "src/main/resources/";
		serviceExtensions = new ArrayList<>();
		megaFile= "input.ttl";
	}
	
	protected List<String> getFileNames(String path) {
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
	
	protected void writeOutFile(String path, List<String> content) {
		try {
			File outFile = new File(path);
			outFile.createNewFile();
			org.apache.commons.io.FileUtils.writeLines(outFile, content);
		}
		catch (IOException e) {
			System.out.println("Error while writing the file " + path + " in method writeOutFile. Message: " + e.getMessage());
		}
		System.out.println("Wrote out the file " + path);
	}
	
	public void addExtension(PluginManagerExtension... extensions) {
		for(PluginManagerExtension serviceExtension : extensions) {
			if (!serviceExtensions.contains(serviceExtension)) {
				serviceExtensions.add(serviceExtension);
			}
		}
	}
	
	public void copyExtensions(CreationBaseService targetService) {
		for(PluginManagerExtension extension : serviceExtensions) {
			targetService.addExtension(extension);
		}
	}
	
}
