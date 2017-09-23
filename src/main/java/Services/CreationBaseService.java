package Services;

import Services.ServiceExtensions.PluginManagerExtension;
import org.apache.jena.base.Sys;

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
		megaFile= ".ttl";
	}
	
	protected List<String> getFileNames(String path) {
		List<String> files = new ArrayList<String>();
		
		if(!path.endsWith("/")) {
			path +="/";
		}
		String completePath = basePath + path;
		try {
			Files.walk(Paths.get(completePath))
					.filter(Files::isRegularFile)
					.filter(f -> !f.toString().endsWith(megaFile) && !f.toString().toLowerCase().endsWith("api.json"))
					.map(f->f.toString())
					.map(f->f.replace( completePath, ""))
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
	public void setPath(String basePath){
		this.basePath = basePath;
	}
	
	
}
