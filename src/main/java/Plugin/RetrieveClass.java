package Plugin;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import Services.LanguageService;
import Services.LanguageServiceException;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

/**
 * Created by freddy on 15.09.17.
 */
public class RetrieveClass extends BaseBuiltin {
	private FileRetrievementService fileRetrievementService = FileRetrievementService.getInstance();
	private LanguageService languageService = LanguageService.getInstance();
	
	@Override
	public String getName() {
		return "RetrieveClass";
	}
	
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		if (!args[0].isURI()) return false;
		
		BindingEnvironment env = context.getEnv();
		String uri = args[0].getURI();
		System.out.println("[RetrieveClass] parsing " + uri);
		try {
			//String path = fileRetrievementService.uriToPath(uri);
			String content = fileRetrievementService.getContent(uri);
			String path = fileRetrievementService.uriToPath(uri);
			
			String className = languageService.getJavaClass(content, path);
			if(className.isEmpty()) return false;
			
			Node value = NodeFactory.createURI("http://softlang.com/Class/" + className);
			return env.bind(args[1], value);
		}
		catch (FileRetrievementServiceException exception) {
			exception.printError();
			return false;
		}
	}
}
