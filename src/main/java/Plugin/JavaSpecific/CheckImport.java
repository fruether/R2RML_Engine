package Plugin.JavaSpecific;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import Services.JavaService;
import Services.LanguageService;
import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import util.Package;
/**
 * Created by freddy on 24.09.17.
 */
public class CheckImport extends BaseBuiltin {
	private JavaService javaService;
	private FileRetrievementService fileRetrievementService;
	private Map<String, List<Package>> fileImportsCache;
	
	public CheckImport() {
		javaService = LanguageService.getInstance().getJavaService();
		fileRetrievementService = FileRetrievementService.getInstance();
		fileImportsCache = new WeakHashMap<>();
		
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
		Package packageName = new Package(getPackageFromUri(args[1].getURI()));
		List<Package> importedPackages = null;
		
		//System.out.println("[CheckImport] Checking in " + fileUri + " for " + packageName);
		
		if(fileImportsCache.containsKey(fileUri)) {
			importedPackages = fileImportsCache.get(fileUri);
		}
		else {
			String content = null;
			try {
				content = fileRetrievementService.getContent(fileUri);
				importedPackages = javaService.getJavaImportedElements(content).stream().map(x->new Package(x)).collect(Collectors.toList());
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
