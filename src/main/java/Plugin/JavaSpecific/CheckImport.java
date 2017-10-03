package Plugin.JavaSpecific;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import Services.LanguageService;
import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by freddy on 24.09.17.
 */
public class CheckImport extends BaseBuiltin {
	private LanguageService languageService;
	private FileRetrievementService fileRetrievementService;
	private Map<String, List<String>> fileImportsCache;
	
	public CheckImport() {
		languageService = LanguageService.getInstance();
		fileRetrievementService = FileRetrievementService.getInstance();
		fileImportsCache = new HashMap<>();
		
	}
	@Override
	public String getName() {
		return "CheckImport";
	}
	
	public int getArgLength() {
		return 2;
	}
	
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		boolean result = false;
		if(args.length != getArgLength()) return result;
		
		String  fileUri = args[0].getURI();
		String packageName = getPackageFromUri(args[1].getURI());
		List<String> importedPackages = null;
		
		System.out.println("[CheckImport] Checking in " + fileUri + " for " + packageName);
		
		if(fileImportsCache.containsKey(fileUri)) {
			importedPackages = fileImportsCache.get(fileUri);
		}
		else {
			String content = null;
			try {
				content = fileRetrievementService.getContent(fileUri);
				importedPackages = languageService.getJavaImportedElements(content);
				fileImportsCache.put(fileUri, importedPackages);
			}
			catch (FileRetrievementServiceException e) {
				e.printError();
			}
		}
		
		result = importedPackages.contains(packageName);
		return result;
		
	}
	
	private String getPackageFromUri(String uri) {
		int lastSlash = uri.lastIndexOf("/") + 1;
		return uri.substring(lastSlash);
		
	}
}
