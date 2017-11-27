package Plugin.JavaSpecific;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import Services.JavaService;
import Services.LanguageService;
import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Created by freddy on 26.11.17.
 */
public class CheckExtension extends JavaBase {
	private FileRetrievementService fileRetrievementService = FileRetrievementService.getInstance();
	private JavaService javaService = LanguageService.getInstance().getJavaService();
	private WeakHashMap<String, Set<String>> extendedElementsCache = new WeakHashMap<>();
	
	@Override
	public String getName() {
		return "CheckExtension";
	}
	@Override
	public int getArgLength() {
		return 2;
	}
	
	@Override
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		if(args.length != 2) {
			return false;
		}
		
		boolean result = false;
		String classUri = args[1].getURI();
		String fileUri = args[0].getURI();
		String javaFileClass = 	getClassFromJavaFilePath(fileUri);
		System.out.println("[CheckExtension:] " + fileUri);
		
		try {
			String content = fileRetrievementService.getContent(fileUri);
			Set<String> extendedElements;
			if(!extendedElementsCache.containsKey(fileUri)) {
				extendedElements = javaService.getExtendedTypes(content, javaFileClass);
				extendedElementsCache.put(fileUri, extendedElements);
			}
			else {
				extendedElements = extendedElementsCache.get(fileUri);
			}
			List<String> packages = getPackages(fileUri, content);
			
			String[] packageAndClass =  getPackageAndClass(getClassFromUri(classUri));
			
			if(!checkPackageIsImported(packageAndClass, packages)) {
				return false;
			}
			return extendedElements.contains(packageAndClass[1]);
			
			
		}
		catch (FileRetrievementServiceException e) {
			e.printStackTrace();
		}
	
		return result;
	}
}
