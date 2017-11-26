package Plugin.JavaSpecific;

import Services.JavaService;
import Services.LanguageService;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by freddy on 26.11.17.
 */
public abstract class JavaBase extends BaseBuiltin {
	private Map<String, List<String>> fileImportedPackages;
	private JavaService javaService = LanguageService.getInstance().getJavaService();
	
	public JavaBase() {
		fileImportedPackages = new HashMap<>();
	}
	
	protected String getClassFromUri(String uri) {
		return  uri.substring(uri.lastIndexOf("/") + 1);
	}
	
	protected String[] getPackageAndClass(String classPath) {
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
	
	protected String getClassFromJavaFilePath(String uri) {
		return  uri.substring(uri.lastIndexOf("/") + 1, uri.length() - ".java".length());
	}
	
	
	protected List<String> getPackages(String fileId, String content) {
		if(fileImportedPackages.containsKey(fileId)) {
			return fileImportedPackages.get(fileId);
		}
		else {
			List<String> result = javaService.getJavaImportedElements(content);
			fileImportedPackages.put(fileId, result);
			return result;
		}
	}
	
	protected boolean checkPackageIsImported(String[] packageAndClass, List<String> importedElements) {
		if (packageAndClass[0] != null) {
			for (String element : importedElements) {
				element = element.replace("*", "");
				if (element.equals(packageAndClass[0] + "." + packageAndClass[1]) || packageAndClass[0]
						.startsWith(element)) {
					return true;
				}
			}
		}
		return false;
	}
}
