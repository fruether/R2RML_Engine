package Plugin;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

import java.io.File;

/**
 * Created by freddy on 02.10.17.
 */
public class ClassLiteral extends BaseBuiltin {
	private FileRetrievementService fileRetrievementService = FileRetrievementService.getInstance();
	
	@Override
	public String getName() {
		return "GetClassLiteral";
	}
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		
		if (!args[0].isURI()) return false;
	
		BindingEnvironment env = context.getEnv();
		String uri = args[0].getURI();
		
		System.out.println("[GetClassLiteral] checking " + uri);
		try {
			String path = fileRetrievementService.uriToPath(uri);
			String className = getClassName(path);
			String classLanguage = className + "OLang";
			Node classLanguageUri = NodeFactory.createURI("http://softlang.com/Languages/" + classLanguage);
		
			return env.bind(args[1], classLanguageUri);
	}
		catch (FileRetrievementServiceException exception) {
		exception.printError();
		return false;
	}
}
	
	private String getClassName(String path) {
		String[] subdirs = path.split("/");
		String filename = subdirs[subdirs.length - 1];
		return filename.substring(filename.lastIndexOf('.') + 1);
	}
}
