package Plugin;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import Services.LanguageService;
import Services.ValidationService;
import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

import java.util.Set;

/**
 * Created by freddy on 14.10.17.
 */
public class RetrieveTables extends BaseBuiltin {
	private FileRetrievementService fileRetrievementService;
	private LanguageService languageService;
	
	public RetrieveTables() {
		languageService = LanguageService.getInstance();
		fileRetrievementService = FileRetrievementService.getInstance();
	}
	
	@Override
	public String getName() {
		return "RetrieveTables";
	}
	
	public int getArgLength() {
		return 1;
	}
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		if (!args[0].isURI())
			return false;
		
		String sqlFileUri = args[0].getURI();
		boolean result = false;
		
		try {
			String sqlContent = fileRetrievementService.getContent(sqlFileUri);
			Set<String> retrievedTables = languageService.mysql_get_tables(sqlContent);
			//Now returning the tables defined by the File
;
		
		}
		catch (FileRetrievementServiceException e) {
			e.printError();
		}
		return result;
		
	}
}
