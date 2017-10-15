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
 * Created by freddy on 15.10.17.
 */
public class HibernateMappingGetTable extends BaseBuiltin {
	
	private FileRetrievementService fileRetrievementService;
	private LanguageService languageService;
	private String baseUriTable;
	
	public HibernateMappingGetTable() {
		fileRetrievementService = FileRetrievementService.getInstance();
		languageService = LanguageService.getInstance();
		baseUriTable = "http://softlang.com/Table/";
	}
	
	@Override
	public String getName() {
		return "HibernateMappingGetTable";
	}
	
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		if(args.length != 2) return false;
		
		String fileUri = args[0].getURI();
		BindingEnvironment environment = context.getEnv();
		try {
			String content = fileRetrievementService.getContent(fileUri);
			String tableName = languageService.getXMLFirstAttribute("class", "table", content);
			if(tableName != "") {
				Node tableNode = NodeFactory.createURI(baseUriTable + tableName.toUpperCase());
				return environment.bind(args[1], tableNode);
			}
			
		}
		catch (FileRetrievementServiceException e) {
			e.printError();
		}
		catch (LanguageServiceException e) {
			e.printError();
		}
		return false;
	}
}
