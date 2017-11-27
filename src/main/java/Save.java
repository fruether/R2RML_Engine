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
public class Save extends BaseBuiltin {
	private Set<String> elements = new HashSet<>();
	@Override
	public String getName() {
		return "Save";
	}
	
	public void headAction(Node[] args, int length, RuleContext context) {
		if(args.length == 1) {
			String literal = args[0].getURI();
			elements.add(literal);
		}
		
	}

	public void printResult() {
		System.out.print("Result");
		for(String value : elements) {
			System.out.println(value);
		}
	}

}
