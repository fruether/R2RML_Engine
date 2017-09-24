package Services.ServiceExtensions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by freddy on 24.09.17.
 */
public class PackageDependencyExtension extends PluginManagerExtension {
@Override

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
					if(!jsonObject.has("dependency")) continue;
					
					String curPackage = jsonObject.getString("package");
					JSONArray dependencyJsonArray = jsonObject.getJSONArray("dependency");
		
					for(int j  = 0; j < dependencyJsonArray.length(); j++) {
						
						String dependPackageDeclaration = technology + ":Package\\/" +dependencyJsonArray.getString(j);
						String curPackageDeclaration = technology + ":Package\\/" + curPackage;
						String depDeclaration = curPackageDeclaration + " sl:uses " + dependPackageDeclaration + " .";
						expected.add(depDeclaration);
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
		return "Dependency";
	}
	
}
