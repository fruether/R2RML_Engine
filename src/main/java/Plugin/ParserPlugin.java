package Plugin;

import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

/**
 * Created by freddy on 09.07.17.
 */
public class ParserPlugin extends BaseBuiltin {
	
	@Override
	public String getName() {
		return "Parse";
	}
	
	public int getArgLength() {
		return 1;
	}
	
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		
		return true;
	}
}
