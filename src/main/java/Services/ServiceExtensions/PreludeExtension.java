package Services.ServiceExtensions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by freddy on 30.07.17.
 */
public class PreludeExtension extends PluginManagerExtension{
	
	@Override
	public List<String> apply(String path, List<String> files, String technology) {
		List<String> result = new ArrayList<>();
		
		result.add("@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .");
		result.add("@prefix rdfs:   <http://www.w3.org/2000/01/rdf-schema> .");
		result.add("@prefix sl: <http://softlang.com/> .");
		result.add("@prefix slash: </> .");
		
		
		return result;
	}
	
	@Override
	public String getName() {
		return "PreludeExtension";
	}
}
