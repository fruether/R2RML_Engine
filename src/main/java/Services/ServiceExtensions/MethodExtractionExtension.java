package Services.ServiceExtensions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by freddy on 24.09.17.
 */
public class MethodExtractionExtension extends PreludeExtension {
	public List<String> apply(String path, List<String> files, String technology) {
		List<String> expected = new ArrayList<>();
		String apiFilePath = basePath +  path + "/api.json";
		File apiFile = new File(apiFilePath);
		if(apiFile.isFile()) {
			try {
				String content = new String(Files.readAllBytes(apiFile.toPath()));
				
				JSONArray jsonArray = new JSONArray(content);
				for(int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String curPackage = jsonObject.get("package").toString();
					String technologyPackagePath=technology + ":Package\\/" + curPackage;
					
					JSONArray methodsArray = jsonObject.getJSONArray("methods");
					
					for(int j = 0; j < methodsArray.length(); j++) {
						String method = methodsArray.getString(j);
						String methodDeclaration = technology + ":" + "\\/Method\\/" + method + " rdf:type " + "sl:Method .";
						String methodAssignemnt = technology + ":" + "\\/Method\\/" + method + " sl:partOf " + technologyPackagePath + " .";
						expected.add(methodDeclaration);
						expected.add(methodAssignemnt);
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return expected;
	}
	
	@Override
	public String getName() {
		return "sl:Method";
	}
}
