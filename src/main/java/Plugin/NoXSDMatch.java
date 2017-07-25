package Plugin;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import Services.ValidationService;
import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

/**
 * Created by freddy on 25.07.17.
 */
public class NoXSDMatch extends BaseBuiltin {
	
	@Override
	public String getName() {
		return "NoXSDMatch";
	}
	
	public int getArgLength() {
		return 1;
	}
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		if (!args[0].isURI())
			return false;
		
		String input = args[0].getURI();
		boolean result = false;
		
		try {
			String path_xml = FileRetrievementService.getInstance().uriToPath(input);
			String relativePath = FileRetrievementService.getInstance().getProjectRelativeResourcesPath();
			String path = relativePath + path_xml;
			result = !ValidationService.getInstance().wasSuccessValidated(path);
			
		}
		catch (FileRetrievementServiceException e) {
			e.printError();
		}
		return result;
		
	}
}
