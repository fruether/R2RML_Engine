package Plugin;

import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

/**
 * Created by freddy on 04.09.17.
 */
public class FileEndingPlugin extends BaseBuiltin {
	
	@Override
	public String getName() {
		return "FileEnding";
	}
	public int getArgLength() {
		return 2;
	}
	
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		if(args.length != 2) return false;
		
		String uri_src = args[0].getURI();
		String suffix = args[1].getLiteral().toString();
		
		return uri_src.endsWith(suffix);
	}
	
}
