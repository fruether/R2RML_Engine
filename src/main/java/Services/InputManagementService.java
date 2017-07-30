package Services;

import Services.ServiceExtensions.PluginManagerExtension;
import Services.ServiceExtensions.PreludeExtension;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by freddy on 25.07.17.
 */
public class InputManagementService extends CreationBaseService{
	private String inputPath = "";
	private String technology ="";
	private String outputFile = "";
	public InputManagementService(String path, String output) {
		super();
		inputPath = path;
		technology = "OpenMRS";
		if(output == null) {
			outputFile = technology.toLowerCase().concat(".ttl");
		}
		else {
			outputFile = output;
		}
		serviceExtensions.add(new PreludeExtension());
	}
	
	public void createInputFile() {
		List<String> files = getFileNames(inputPath);
		List<String> output = new ArrayList<>();
		
		for(PluginManagerExtension plugin : super.serviceExtensions) {
			List<String>  result = plugin.apply(inputPath, files, technology.toLowerCase());
			output.addAll(result);
			output.add("");
		}
		System.out.println(basePath + inputPath +outputFile);
		writeOutFile(getPath(), output);
	}
	
	public Model getModel() {
		return FileManager.get().loadModel(getPath());
	}
	
	private String getPath() {
		return basePath  + inputPath + outputFile;
	}
	
	
	
}
