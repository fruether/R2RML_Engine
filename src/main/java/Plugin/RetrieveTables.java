package Plugin;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import Services.LanguageService;
import Services.SQLService;
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
	private SQLService sqlService;
	
	private String predicateUriString;
	private String baseUri;
	private Node rdfTypeUri;
	private Node tableSoftlang;
	
	public RetrieveTables() {
		languageService = LanguageService.getInstance();
		fileRetrievementService = FileRetrievementService.getInstance();
		sqlService = languageService.getSQLService();
		baseUri = "http://softlang.com";
		predicateUriString = baseUri + "/definesTable";
		rdfTypeUri = NodeFactory.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
		tableSoftlang = NodeFactory.createURI("http://softlang.com/Table");
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
			 retrievedTables = sqlService.mysql_get_tables(sqlContent);
		}
		catch (FileRetrievementServiceException e) {
			return;
		}
		
		for(String table : retrievedTables) {
			Node tableNode = NodeFactory.createURI(baseUri + "/Table/" + table);
			context.add( new Triple( sqlFileNode, predicateUri, tableNode ) );
			context.add( new Triple( tableNode, rdfTypeUri, tableSoftlang ) );
		}
	}
}
