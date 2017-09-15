package Plugin;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import Services.LanguageService;
import Services.LanguageServiceException;
import org.apache.jena.graph.Node;
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
		boolean result = false;
		if (args.length != 2)
			return result;
		
		try {
			String uri = args[0].getURI();
			String className = getClassFromParh(fileRetrievementService.uriToPath(args[1].getURI()));
			System.out.println("[HibernateMappingAnalysis]: Checking for " + uri + " and " + className);
			
			String content = fileRetrievementService.getContent(uri);
			String refClassName = languageService.getXMLFirstAttribute("class", "name", content);
			
			result = className.equals(refClassName);
		}
		catch (FileRetrievementServiceException e) {
			e.printError();
		}
		catch (LanguageServiceException e) {
			e.printError();
		}
		return result;
	}
	
	private String getClassFromParh(String path) {
		return path.substring(6);
	}
}
