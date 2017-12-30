package Plugin.JavaSpecific;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
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
		args[1] = NodeFactory.createURI("http://softlang.com" + args[1].getLiteral().toString());
		return  !super.bodyCall(args, length, context);
	}
	
}
