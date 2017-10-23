package Plugin.HibernateSpecific;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import Services.LanguageServiceException;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

/**
 * Created by freddy on 02.10.17.
 */
public class HibernateGetMappingType extends BaseBuiltin {
	private FileRetrievementService fileRetrievementService = FileRetrievementService.getInstance();
	
	@Override
	public String getName() {
		return "HibernateGetMappingType";
	}
	
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		if (!args[0].isURI()) return false;
		
		BindingEnvironment env = context.getEnv();
		String uri = args[0].getURI();
		System.out.println("[HibernateGetMappingType] checking " + uri);
		try {
			String path = fileRetrievementService.uriToPath(uri);
			String className = getHibernateClassName(path);
			String hibernateFunction = className + "-OR-Mapping";
			Node classNameUri = NodeFactory.createURI("http://softlang.com/Functions/" + hibernateFunction);
			
			return env.bind(args[1], classNameUri);
		}
		catch (FileRetrievementServiceException exception) {
			exception.printError();
			return false;
		}
	}
	
	private String getHibernateClassName(String path) {
		String[] subdirs = path.split("/");
		String filename = subdirs[subdirs.length - 1];
		return filename.split("\\.")[0];
	}
}
