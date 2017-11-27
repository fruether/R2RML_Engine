package Plugin;

import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by freddy on 12.11.17.
 */
public class Count extends BaseBuiltin {
	private Map<String, Integer> countRules = new HashMap<>();
	private Set<String> files = new HashSet<>();
	@Override
	public String getName() {
		return "Count";
	}
	
	public void headAction(Node[] args, int length, RuleContext context) {
		
		if(args.length <= 1) {
			String literal = args[0].getLiteral().getLexicalForm();
			if(countRules.containsKey(literal)) {
				int curCount = countRules.get(literal) + 1;
				countRules.put(literal, curCount);
			}
			else {
				countRules.put(literal, 1);
			}
			
		}
	}

	public void printResult() {
		for(String key : countRules.keySet()) {
			int keyCount = countRules.get(key);
			System.out.println(key + " ===> " + keyCount);
		}
	}

}
