package Plugin;

import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

/**
 * Created by freddy on 24.09.17.
 */
public class CheckImport extends BaseBuiltin {
	
	@Override
	public String getName() {
		return "CheckImport";
	}
	
	public int getArgLength() {
		return 2;
	}
	
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		if(args.length != getArgLength()) return false;
		
		String  fileUri = args[0].getURI();
		String packageName = getPackageFromUri(args[0].getURI());
		
		return true;
		
	}
	
	private String getPackageFromUri(String uri) {
		int lastSlash = uri.lastIndexOf("/");
		return uri.substring(lastSlash);
		
	}
}
