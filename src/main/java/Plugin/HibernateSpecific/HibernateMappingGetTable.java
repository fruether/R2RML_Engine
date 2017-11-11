package Plugin.HibernateSpecific;

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
 * Created by freddy on 15.10.17.
 */
public class HibernateMappingGetTable extends BaseBuiltin {
	
	private FileRetrievementService fileRetrievementService;
	private LanguageService languageService;
	private String baseUriTable;
	
	public HibernateMappingGetTable() {
		fileRetrievementService = FileRetrievementService.getInstance();
		languageService = LanguageService.getInstance();
		baseUriTable = "http://softlang.com/URI/";
	}
	
	@Override
	public String getName() {
		return "HibernateMappingGetTable";
	}
	
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		if(args.length != 2) return false;
		
		String fileUri = args[0].getURI();
		System.out.println("[HibernateMappingGetTable] " + fileUri);
		BindingEnvironment environment = context.getEnv();
		try {
			String content = fileRetrievementService.getContent(fileUri);
			String tableName = languageService.getXMLFirstAttribute("class", "table", content);
			Node tableNode = null;
			if(tableName != "") {
				tableNode = NodeFactory.createURI(baseUriTable + tableName.toUpperCase());
			}
			else {
				String className = languageService.getXMLFirstAttribute("class", "name", content);
				
				if(className == "") {
					return false;
				}
				String defaultTableName = getClassName(className);
				tableNode = NodeFactory.createURI(baseUriTable + defaultTableName.toUpperCase());
				
			}
			return environment.bind(args[1], tableNode);
			
		}
		catch (FileRetrievementServiceException e) {
			e.printError();
		}
		catch (LanguageServiceException e) {
			e.printError();
		}
		return false;
	}
	
	private  String getClassName(String classname) {
		if(!classname.contains(".")) return classname;
		return classname.substring(classname.lastIndexOf(".") + 1);
		
	}
}
