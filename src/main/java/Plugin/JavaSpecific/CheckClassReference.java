package Plugin.JavaSpecific;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import Services.JavaService;
import Services.LanguageService;
import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Created by freddy on 02.10.17.
 */
public class CheckClassReference extends JavaBase {
	private JavaService javaService;
	private FileRetrievementService fileRetrievementService;
	private Map<String, Set<String>> fileClassCache;
	
	public CheckClassReference() {
		javaService = LanguageService.getInstance().getJavaService();
		fileRetrievementService = FileRetrievementService.getInstance();
		fileClassCache = new WeakHashMap<>();
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
				String classNameOfFile = getClassFromJavaFilePath(uriFile);
				typeDeclarations = javaService.getDeclaredClasses(content, classNameOfFile);
				fileClassCache.put(uriFile, typeDeclarations);
			}
			
			String[] packageAndClass = getPackageAndClass(classAndPackegeNameDest);
			List<String> importedElements = getPackages(uriFile, content);
			boolean isImported = checkPackageIsImported(packageAndClass, importedElements);
			if(!isImported)  return false;
			result = typeDeclarations.contains(packageAndClass[1]);
			System.out.println(typeDeclarations);
		}
		catch (FileRetrievementServiceException e) {
			e.printStackTrace();
		}
		catch (Throwable throwable) {
			throwable.printStackTrace();
			result = false;
		}
		System.out.println("[CheckedSuccessfullClassReference] : " + uriFile + " " + classAndPackegeNameDest + " " + result);
		
		return result;
	}
}
