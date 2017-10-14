package Plugin;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import Services.LanguageService;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

import java.util.Set;

/**
 * Created by freddy on 14.10.17.
 */
public class RetrieveTables extends BaseBuiltin {
	private FileRetrievementService fileRetrievementService;
	private LanguageService languageService;
	
	private String predicateUriString;
	private String baseUri;
	
	public RetrieveTables() {
		languageService = LanguageService.getInstance();
		fileRetrievementService = FileRetrievementService.getInstance();
		baseUri = "http://softlang.com";
		predicateUriString = baseUri + "/hasTable";
	}
	
	@Override
	public String getName() {
		return "RetrieveTables";
	}
	
	public int getArgLength() {
		return 1;
	}
	
	@Override
	public void headAction(Node[] args, int length, RuleContext context) {
		Set<String> retrievedTables;
		checkArgs(length, context);
		
		Node sqlFileNode = getArg(0, args, context);
		
		if(!sqlFileNode.isURI()) return;
		String sqlFileUri = args[0].getURI();
		Node predicateUri = NodeFactory.createURI(predicateUriString);
		
		try {
			String sqlContent = fileRetrievementService.getContent(sqlFileUri);
			 retrievedTables = languageService.mysql_get_tables(sqlContent);
		}
		catch (FileRetrievementServiceException e) {
			return;
		}
		
		for(String table : retrievedTables) {
			Node tableNode = NodeFactory.createURI(baseUri + "/" + table);
			context.add( new Triple( sqlFileNode, predicateUri, tableNode ) );
		}
		
	}
}
