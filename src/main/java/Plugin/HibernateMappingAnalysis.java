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
public class HibernateMappingAnalysis extends BaseBuiltin {
	
	private FileRetrievementService fileRetrievementService = FileRetrievementService.getInstance();
	private LanguageService languageService = LanguageService.getInstance();
	
	@Override
	public String getName() {
		return "HibernateMappingAnalysis";
	}
	
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		if (args.length != 2)
			return false;
		
		try {
			String classUri = "http://softlang.com/Class/";
			String uri = args[0].getURI();
			BindingEnvironment env = context.getEnv();
			
			System.out.println("[HibernateMappingAnalysis]: Checking for " + uri);
			
			String content = fileRetrievementService.getContent(uri);
			String packageName = languageService.getXMLFirstAttribute("hibernate-mapping", "package", content);
			String refClassName = languageService.getXMLFirstAttribute("class", "name", content);
			
			if(packageName != "" && refClassName != "" && refClassName.indexOf(".") == -1) {
				classUri += packageName + "." + refClassName;
			}
			else if(refClassName != "") {
				classUri += refClassName;
			}
			else {
				return false;
			}
			Node value = NodeFactory.createURI(classUri);
			return env.bind(args[1], value);
		}
		catch (FileRetrievementServiceException e) {
			e.printError();
		}
		catch (LanguageServiceException e) {
			e.printError();
		}
		return false;
	}
	
	private String getClassFromParh(String path) {
		return path.substring(6);
	}
}
