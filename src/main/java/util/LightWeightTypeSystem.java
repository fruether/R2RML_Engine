package util;

import Services.LanguageService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by freddy on 02.01.18.
 */
public class LightWeightTypeSystem {
	private HashMap<String, HashMap<String, String>> fileToDeclaration;
	private String currentKey = null;
	private static LightWeightTypeSystem lightWeightTypeSystem;
	
	private LightWeightTypeSystem() {
		fileToDeclaration = new HashMap<>();
	}
	public static LightWeightTypeSystem getInstance() {
		 if(lightWeightTypeSystem == null) {
				lightWeightTypeSystem = new LightWeightTypeSystem();
		 }
		return lightWeightTypeSystem;
	}
	public void setKey(String key) {
		currentKey = key;
	}
	
	public void removeKey() {
		currentKey = null;
	}
	
	public void setPair(String name, String type) {
		if(currentKey == null) return;
		Map<String, String> declarations = fileToDeclaration.get(currentKey);
		declarations.put(name, type);
	}
	public Map<String, String> getTypeSystemForFile(String key) {
		return fileToDeclaration.get(currentKey);
	}
}
