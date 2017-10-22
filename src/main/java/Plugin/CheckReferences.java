package Plugin;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
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
		fileNameReferencesMap = new WeakHashMap<>();
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
		try {
			String uri =args[0].getURI();
			String matchingPath = fileRetrievementService.uriToPath(args[1].getURI());
			List<String> referencedInFile;
			
			System.out.println("[CheckReference]: "  + uri + " REFERENCES " + matchingPath);
			
			if(fileNameReferencesMap.containsKey(uri)) {
				referencedInFile = fileNameReferencesMap.get(uri);
			}
			else {
				referencedInFile = getReferences(uri);
				fileNameReferencesMap.put(uri, referencedInFile);
			}
			String uriPath = fileRetrievementService.uriToPath(uri);
			int endOfDirectories = uriPath.lastIndexOf("/");
			if(endOfDirectories != -1) {
				uriPath = uriPath.substring(0, endOfDirectories);
			}
			else {
				uriPath = "";
			}
			
			for (String referencedFile : referencedInFile) {
				if(samePath(referencedFile, matchingPath, uriPath)) return true;
			}
		}
		catch (FileRetrievementServiceException e) {
			e.printError();
			e.printStackTrace();
		}
		return false;
		
		
	}
	
	private boolean samePath(String path_referenced, String path_argument, String filePath) {
		if(path_referenced.equals(path_argument)) return true;
		
		String[] elements_path_referenced = path_referenced.split("/");
		String[] elements_path_argument =path_argument.split("/");
		String[] uri_elements = filePath.split("/");
		
		ArrayUtils.reverse(elements_path_referenced);
		ArrayUtils.reverse(elements_path_argument);
		ArrayUtils.reverse(uri_elements);
		
		int iterations = Math.min(elements_path_argument.length, elements_path_referenced.length);
		for(int i = 0; i<iterations; i++) {
			//Case of a reference like../dir/file.txt
			if(elements_path_referenced[i].equals("../")) continue;
			// Case of normal reference
			if(!elements_path_argument[i].equals(elements_path_referenced[i])) return false;
		}
		// all elements already setup
		if(elements_path_argument.length == elements_path_referenced.length) return true;
		else if(elements_path_argument.length < elements_path_referenced.length) return false;
		
		//If referenced URI is a relative path
		for(int i = 0; i < uri_elements.length; i++) {
			if(i + iterations == elements_path_argument.length) return false;
			if(!uri_elements[i].equals(elements_path_argument[i + iterations])) return false;
		}
		return true;
		
	}
	private List<String> getReferences (String uri) {
		ArrayList<String> listOfReferences = new ArrayList<>();
		if(uri == null) return listOfReferences;
		
		String regex = "\"(.*?)\"";
		String regex2 = "(\\.*)+";
		
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
