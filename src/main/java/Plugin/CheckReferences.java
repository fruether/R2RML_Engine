package Plugin;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by freddy on 04.09.17.
 */
public class CheckReferences extends BaseBuiltin{
	private FileRetrievementService fileRetrievementService = FileRetrievementService.getInstance().getInstance();
	private Map<String, List<String>> fileNameReferencesMap;
	
	public CheckReferences() {
		super();
		fileNameReferencesMap = new HashMap<>();
	}
	
	@Override
	public String getName() {
		return "CheckReference";
	}
	public int getArgLength() {
		return 2;
	}
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		if(args.length != getArgLength()) return false;
		
		String uri =args[0].getURI();
		List<String> referencesInFile;
		
		if(fileNameReferencesMap.containsKey(uri)) {
			referencesInFile = fileNameReferencesMap.get(uri);
		}
		else {
			referencesInFile = getReferences(uri);
			fileNameReferencesMap.put(uri, referencesInFile);
		}
		
		return true;
	}
	private List<String> getReferences (String uri) {
		ArrayList<String> listOfReferences = new ArrayList<>();
		if(uri == null) return listOfReferences;
		
		String regex = "\"(.*?)\"";
		try {
			String content = fileRetrievementService.getContent(uri);
			Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(content);
			if(!matcher.find()) return listOfReferences;
			
			while (matcher.find()){
				String input = matcher.group(1);
				listOfReferences.add(input);
			}
		}
		catch (FileRetrievementServiceException e) {
			e.printError();
		}
		return listOfReferences;
		
	}
}
