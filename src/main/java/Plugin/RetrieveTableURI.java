package Plugin;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import Services.LanguageService;
import Services.SQLService;
import Services.UriService;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

import java.util.Set;

/**
 * Created by freddy on 14.10.17.
 */
public class RetrieveTableURI extends BaseBuiltin {
	private FileRetrievementService fileRetrievementService;
	private LanguageService languageService;
	private SQLService sqlService;
	private UriService uriService;
	private String baseUri;
	
	public RetrieveTableURI() {
		languageService = LanguageService.getInstance();
		fileRetrievementService = FileRetrievementService.getInstance();
		sqlService = languageService.getSQLService();
		baseUri = "http://softlang.com/";
		uriService = new UriService(baseUri);
	}
	
	@Override
	public String getName() {
		return "RetrieveTableURI";
	}
	
	public int getArgLength() {
		return 1;
	}
	
	@Override
	public void headAction(Node[] args, int length, RuleContext context) {
		String retrievedTable;
		
		checkArgs(length, context);
		
		Node sqlFragmentNode = getArg(0, args, context);
		
		if(!sqlFragmentNode.isURI()) return;
		
		String sqlFragmentUri = sqlFragmentNode.getURI();
		String sqlFileUri = getUriFromFragment(sqlFragmentUri);
		int[] fragmentParts = getFragmentParts(sqlFragmentUri);
		
		try {
			String sqlContent = fileRetrievementService.getContent(sqlFileUri).toUpperCase();
			String sqlFragmentContent = sqlContent.substring(fragmentParts[0], fragmentParts[1]);
			retrievedTable = sqlService.get_table(sqlFragmentContent);
		}
		catch (FileRetrievementServiceException e) {
			return;
		}
		if(retrievedTable != null && retrievedTable != "") {
			Node tableUri = NodeFactory.createURI(uriService.getUri() + retrievedTable);
			context.add(new Triple(tableUri, uriService.getNodeElementOfUri(), uriService.getNodeQualifiedName()));
			context.add(new Triple(tableUri, uriService.getNodePartOfUri(), sqlFragmentNode));
			
		}
		
	}
	
	private String getUriFromFragment(String fragmentUri) {
		String[] uriParts = fragmentUri.split("#");
		return uriParts[0];
	}
	
	private int[] getFragmentParts(String fragmentUri) {
		String[] uriParts = fragmentUri.split("#");
		int[] positionPairs = new int[2];
		
		if(uriParts.length == 2) {
			String[] startEndFragment = uriParts[1].split(":");
			positionPairs[0] = Integer.parseInt(startEndFragment[0]);
			positionPairs[1] = Integer.parseInt(startEndFragment[1]);
		}
		return positionPairs;
	}
}
