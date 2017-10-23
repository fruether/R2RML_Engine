package Plugin.HibernateSpecific;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by freddy on 14.09.17.
 */
public class HibernateRoleIdentification extends BaseBuiltin {
	private FileRetrievementService fileRetrievementService;
	private Map<String, List<String >> roleNameMappingFileEnding;
	
	public HibernateRoleIdentification() {
		
		fileRetrievementService = FileRetrievementService.getInstance();
		
		roleNameMappingFileEnding = new HashMap<>();
		roleNameMappingFileEnding.put("HibernateConfiguration", addConfFiles());
		roleNameMappingFileEnding.put("HibernateMapping", addMappingFiles());
		
	}
	
	private List<String> addConfFiles() {
		List<String> hibernateConfFiles = new ArrayList<>();
		
		hibernateConfFiles.add("hibernate-configuration-3.0.dtd");
		hibernateConfFiles.add("hibernate-configuration-4.0.xsd");
		
		return hibernateConfFiles;
	}
	private List<String> addMappingFiles() {
		List<String> hibernateMappingFiles = new ArrayList<>();
		
		hibernateMappingFiles.add("hibernate-mapping-3.0.dtd");
		hibernateMappingFiles.add("hibernate-mapping-4.0.xsd");
		
		return hibernateMappingFiles;
	}
	
	@Override
	public String getName() {
		return "HibernateRoleIdentification";
	}
	
	private boolean checkElementStartsWithAny(String input, List<String> elements) {
		boolean result = false;
		
		for(String element : elements) {
			if(input.endsWith(element)) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		boolean result = false;
		if(args.length != 2) return result;
		try {
			String filename = fileRetrievementService.uriToPath(args[0].getURI());
			String resultValue = "http://softlang.com/Plugins/Hibernate/";
			System.out.println("[HibernateRoleIdentification]: Checking role for " + filename);
			
			for(String roleNames : roleNameMappingFileEnding.keySet()) {
				if(checkElementStartsWithAny(filename, roleNameMappingFileEnding.get(roleNames))) {
					resultValue += roleNames;
					result = true;
					break;
				}
			}
			Node resultNode = NodeFactory.createURI(resultValue);
			context.getEnv().bind(args[1], resultNode);
			
		}
		catch (FileRetrievementServiceException e) {
				e.printError();
		}
		return result;
	}
	
}
