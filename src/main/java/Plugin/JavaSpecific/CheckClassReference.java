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
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Created by freddy on 02.10.17.
 */
public class CheckClassReference extends BaseBuiltin {
	private JavaService javaService;
	private FileRetrievementService fileRetrievementService;
	private Map<String, Set<String>> fileClassCache;
	private Map<String, List<String>> fileImportedPackages;
	
	public CheckClassReference() {
		javaService = LanguageService.getInstance().getJavaService();
		fileRetrievementService = FileRetrievementService.getInstance();
		fileClassCache = new WeakHashMap<>();
		fileImportedPackages = new WeakHashMap<>();
		
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
		System.out.println("[CheckClassReference:] " + uriFile);
		
		try {
			Set<String> typeDeclarations = null;
			String content = fileRetrievementService.getContent(uriFile);
			
			if(fileClassCache.containsKey(uriFile)) {
				typeDeclarations = fileClassCache.get(uriFile);
			}
			else {
				String classNameOfFile = getClass(uriFile);
				typeDeclarations = javaService.getDeclaredClasses(content, classNameOfFile);
				fileClassCache.put(uriFile, typeDeclarations);
			}
			String[] packageAndClass = getPackageAndClass(classAndPackegeNameDest);
			if (packageAndClass[0] != null) {
				boolean isImported = false;
				List<String> importedElements = getPackages(uriFile, content);
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
		catch (Throwable throwable) {
			throwable.printStackTrace();
			result = false;
		}
		System.out.println("[CheckedSuccessfullClassReference] : " + uriFile + " " + classAndPackegeNameDest);
		
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
	
	private List<String> getPackages(String fileId, String content) {
		if(fileImportedPackages.containsKey(fileId)) {
			return fileImportedPackages.get(fileId);
		}
		else {
			List<String> result = javaService.getJavaImportedElements(content);
			fileImportedPackages.put(fileId, result);
			return result;
		}
	}
}
