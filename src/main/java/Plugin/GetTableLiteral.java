package Plugin;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

/**
 * Created by freddy on 15.10.17.
 */
public class GetTableLiteral extends BaseBuiltin {
	private String baseUri = "http://softlang.com/Language/";
	@Override
	public String getName() {
		return "GetTableLiteral";
	}
	
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		
		if (args.length != 2)
			return false;
		
		BindingEnvironment env = context.getEnv();
		String uri = args[0].getURI();
		String tableName = uri.substring(uri.lastIndexOf("/") + 1);
		
		Node tableLanguage = NodeFactory.createURI(baseUri + tableName + "-RLang");
		
		return env.bind(args[1], tableLanguage);
		
	}
}
