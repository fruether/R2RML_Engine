package Plugin.JavaSpecific;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import Services.LanguageService;
import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;
import java.lang.Math.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by freddy on 24.09.17.
 */
public class CheckCall extends BaseBuiltin{
	private LanguageService languageService;
	private FileRetrievementService fileRetrievementService;
	private Map<String, List<String>> fileFunctionCache;

	public CheckCall() {
		languageService = LanguageService.getInstance();
		fileRetrievementService = FileRetrievementService.getInstance();
		fileFunctionCache = new HashMap<>();
	}

	@Override
	public String getName() {
		return "CheckCall";
	}
	public int getArgLength() {
		return 3;
	}
	
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		boolean result = false;
		if (args.length != getArgLength())
			return result;
		
		String fileUri = args[0].getURI();
		String methodName = getLastUriElement(args[1].getURI());
		String className = getLastUriClasst(args[2].getURI());
		
		List<String> methodCalls = null;
		System.out.println("[CheckCall] Checking in " + fileUri + " the class " + className +" for " + methodName);
		if(fileFunctionCache.containsKey(fileUri)) {
			methodCalls = fileFunctionCache.get(fileUri);
		}
		else {
			try {
				String content = fileRetrievementService.getContent(fileUri);
				methodCalls = languageService.getMethodCalls(content, className);
				fileFunctionCache.put(fileUri, methodCalls);
			}
			catch (FileRetrievementServiceException e) {
				e.printError();
			}
		}
		result = methodCalls.contains(methodName);
		return result;
	}
	private String getLastUriElement(String uri) {
		int lastSlash = uri.lastIndexOf("/") + 1;
		return uri.substring(lastSlash);
		
	}
	private String getLastUriClasst(String uri) {
		int lastSlash = uri.lastIndexOf("/") + 1;
		int lastDot = uri.lastIndexOf(".") + 1;
		int max = Math.max(lastDot, lastSlash);
		return uri.substring(max);
		
	}
}
