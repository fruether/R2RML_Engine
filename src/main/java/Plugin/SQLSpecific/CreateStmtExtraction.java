package Plugin.SQLSpecific;

import Services.FileRetrievementService;
import Services.LanguageService;
import Services.SQLService;
import Services.UriService;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

import java.io.File;
import java.util.Map;

/**
 * Created by freddy on 30.10.17.
 */
public class CreateStmtExtraction extends BaseBuiltin {
	private FileRetrievementService fileRetrievementService;
	private SQLService sqlService;
	private UriService uriService;
	
	public CreateStmtExtraction() {
		fileRetrievementService = FileRetrievementService.getInstance();
		sqlService = LanguageService.getInstance().getSQLService();
		uriService = new UriService("http://softlang.com/");
	}
	
	@Override
	public String getName() {
		return "CreateStmtExtraction";
	}
	
	@Override
	public void headAction(Node[] args, int length, RuleContext context) {
		if (args.length != 1) {
			return;
		}
		checkArgs(length, context);
		
		try {
			// Node javaFileNode = getArg(0, args, context);
			String uri =args[0].getURI();
			String content =  fileRetrievementService.getContent(uri).toUpperCase();
			Map<String, int[]> foundCreateStmts = sqlService.getCreateStmts(content);
			
			for(String table : foundCreateStmts.keySet()) {
				int[] matchedIdx = foundCreateStmts.get(table);
				Node fragmentPart = NodeFactory.createURI(uri + "#" + matchedIdx[0] + ":" + matchedIdx[1]);
				
				if(checkActualStmt(content, matchedIdx[0], matchedIdx[1])) {
					//System.out.println("True for " + fragmentPart.toString());
					context.add(new Triple(fragmentPart, uriService.getNodeElementOfUri() ,uriService.getNodeLanguageSqlCreateStmt()));
				}
				System.out.println("[CreateStmtExtraction] " + fragmentPart.toString());
				context.add(new Triple(fragmentPart, uriService.getNodeRdfType(), uriService.getNodeFragmentUri()));
				context.add(new Triple(fragmentPart, uriService.getNodePartOfUri(), args[0]));
			}
			
		}catch (Throwable t) {
		
		}
	}
	private boolean checkActualStmt(String content, int start, int end) {
		String createStmt = content.substring(start, end);
		return sqlService.parseSQL(createStmt);
	}
}
