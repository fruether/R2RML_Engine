package Plugin;

import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by freddy on 16.11.17.
 */
public class CountDistinct  extends BaseBuiltin{
	
	private Map<String, Set<String>> files = new HashMap<>();
	
	public String getName() {
		return "CountDistinct";
	}
	
	@Override
	public void headAction(Node[] args, int length, RuleContext context) {
		
		if (args.length >= 2) {
			String fileName = args[1].getURI();
			String literal = args[0].getLiteral().toString();
			Set<String> distinctElements;
			if(files.containsKey(literal)) {
				distinctElements = files.get(literal);
			}
			else {
				distinctElements = new HashSet<>();
				files.put(literal, distinctElements);
			}
			
			distinctElements.add(fileName);
		}
	}
	public void printResult() {
		for(String key : files.keySet())
		{
			Set<String> distinctFiles = files.get(key);
			System.out.println(key + " ==> " + distinctFiles.size());
			
		}
	}
}
