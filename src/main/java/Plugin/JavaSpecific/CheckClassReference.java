package Plugin.JavaSpecific;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import Services.LanguageService;
import org.apache.jena.base.Sys;
import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by freddy on 02.10.17.
 */
public class CheckClassReference extends BaseBuiltin {
	private LanguageService languageService;
	private FileRetrievementService fileRetrievementService;
	private Map<String, Set<String>> fileClassCache;
	
	public CheckClassReference() {
		languageService = LanguageService.getInstance();
		fileRetrievementService = FileRetrievementService.getInstance();
		fileClassCache = new HashMap<>();
		
	}
	@Override
	public String getName() {
		return "CheckClassReference";
	}
	
	public int getArgLength() {
		return 2;
	}
	
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		boolean result = false;
		if (args.length != getArgLength())
			return result;
		
		String uriFile = args[0].toString();
		String classAndPackegeNameDest = getClassFromUri(args[1].toString());
		
		System.out.println("[CheckClassReference] : " + uriFile + " " + classAndPackegeNameDest);
		try {
			Set<String> typeDeclarations = null;
			String content = fileRetrievementService.getContent(uriFile);
			String classNameOfFile = getClass(uriFile);
			
			if(fileClassCache.containsKey(uriFile)) {
				typeDeclarations = fileClassCache.get(uriFile);
			}
			else {
				typeDeclarations = languageService.getDeclaredClasses(content, classNameOfFile);
				fileClassCache.put(uriFile, typeDeclarations);
			}
			String classNameDest = classAndPackegeNameDest.substring(classAndPackegeNameDest.lastIndexOf(".") + 1);
			String[] packageAndClass = getPackageAndClass(classAndPackegeNameDest);
			if (packageAndClass[0] != null) {
				boolean isImported = false;
				List<String> importedElements = languageService.getJavaImportedElements(content);
				for(String element : importedElements) {
					element = element.replace("*", "");
					if(element.equals(packageAndClass[0] + "." + packageAndClass[1]) || packageAndClass[0].startsWith(element)) {
						isImported = true;
						break;
					}
				}
				if(!isImported)
					return false;
			}
			
			result = typeDeclarations.contains(packageAndClass[1]);
		}
		catch (FileRetrievementServiceException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	private String getClassFromUri(String uri) {
		return  uri.substring(uri.lastIndexOf("/") + 1);
	}
	
	private String[] getPackageAndClass(String classPath) {
		String[] packageClass = new String[2];
		if(classPath.contains(".")) {
			packageClass[0] = classPath.substring(0, classPath.lastIndexOf("."));
			packageClass[1] = classPath.substring(classPath.lastIndexOf(".") + 1);
		}
		else {
			packageClass[0] = null;
			packageClass[1] = classPath;
		}
		return packageClass;
	}
	
	private String getClass(String uri) {
		return  uri.substring(uri.lastIndexOf("/") + 1, uri.length() - ".java".length());
	}
}
