package Services.ServiceExtensions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static javafx.scene.input.KeyCode.M;

/**
 * Created by freddy on 23.09.17.
 */
public class APIDetectionExtension extends PreludeExtension{
	@Override
	public List<String> apply(String path, List<String> files, String technology) {
		List<String> expected = new ArrayList<>();
		String apiFilePath = basePath +  path + "/api.json";
		File apiFile = new File(apiFilePath);
		if(apiFile.isFile()) {
			String content = null;
			try {
				content = new String(Files.readAllBytes(apiFile.toPath()));

				JSONArray jsonArray = new JSONArray(content);
				for(int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String curPackage = jsonObject.get("package").toString();
					String technologyPackagePath=technology + ":" + curPackage;
					expected.add(technologyPackagePath+ " rdf:type " + getName() + " .");
					expected.add(technologyPackagePath+ " sl:partOf " +  "sl:" + technology + " .");
					expected.add("sl:" + technology + " sl:hasPackage " + technologyPackagePath + " .");
					
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
		return "sl:Package";
	}
}
