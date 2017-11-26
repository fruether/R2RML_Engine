package Plugin.JavaSpecific;

import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

/**
 * Created by freddy on 26.11.17.
 */
public class CheckNotImported extends CheckImport {
	
	public String getName() {
		return "CheckNotImported";
	}
	
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		return  !super.bodyCall(args, length, context);
	}
	
}
