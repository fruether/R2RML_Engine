package Plugin.JavaSpecific;

import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;
import util.Package;

import java.util.List;

/**
 * Created by freddy on 03.11.17.
 */
public class CheckLiteralImported extends CheckImport {
	
	public String getName() {
		return "CheckLiteralImported";
	}
	
	public int getArgLength() {
		return 2;
	}
	
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		boolean result = false;
		if(args.length != getArgLength()) return result;
		
		String  fileUri = args[0].getURI();
		
		Package packageName = new Package(args[1].getLiteral().getLexicalForm());
		List<Package> importedPackages = getImportedPackages(fileUri);
		System.out.println("[CheckLiteralImported] Checking in " + fileUri + " for " + packageName);
		
		result = importedPackages.contains(packageName);
		return result;
		
	}
}
