package Plugin;

import Services.ValidationService;
import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;
import Services.*;

/**
 * Created by freddy on 03.07.17.
 */
public class XSDCheckPlugin extends BaseBuiltin {
	
	public String getName() {
		return "XSDCheck";
	}
	
	public int getArgLength() {
		return 2;
	}
	
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		if (!args[0].isURI()) return false;
		
		String uri_xml = args[0].getURI();
		String uri_xsd = args[1].getURI();
		try {
			String path_xml = FileRetrievementService.getInstance().uriToPath(uri_xml);
			String path_xsd = FileRetrievementService.getInstance().uriToPath(uri_xsd);
			
			System.out.println("XSDCheckPlugin " + uri_xml + " and " + uri_xsd);
			return validateMavenXMLSchema(path_xml, path_xsd);
		}
		catch (FileRetrievementServiceException fileRetrievementService) {
			fileRetrievementService.printError();
			return false;
		}
	}
	private boolean validateMavenXMLSchema(String path_xml, String path_xsd) {
		String relativePath = FileRetrievementService.getInstance().getProjectRelativeResourcesPath();
		return ValidationService.getInstance().validateXMLSchema(relativePath + path_xml, relativePath + path_xsd);
	}
	
}
