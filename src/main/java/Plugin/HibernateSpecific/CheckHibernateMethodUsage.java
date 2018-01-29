package Plugin.HibernateSpecific;

import Plugin.JavaSpecific.JavaBase;
import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import Services.JavaService;
import Services.LanguageService;
import com.sun.tools.javac.comp.Check;
import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by freddy on 01.01.18.
 */
public class CheckMethodUsage extends JavaBase {
	
	private List<String> validMethods;
	private FileRetrievementService fileRetrievementService = FileRetrievementService.getInstance();
	JavaService javaService = LanguageService.getInstance().getJavaService();
	@Override
	public String getName() {
		return "CheckMethodUsage";
	}
	
	public CheckMethodUsage() {
		validMethods = new ArrayList<>();
		setUp();
	}
	
	private void setUp() {
		validMethods.add("createSQLQuery");
		validMethods.add("iterate");
		validMethods.add("executeUpdate");
		validMethods.add("createCriteria");
		validMethods.add("list");
		validMethods.add("executeUpdate");
		validMethods.add("getFirstResult");
		validMethods.add("getResultList");
		validMethods.add("getSingleResult");
		validMethods.add("unwrap");
		validMethods.add("delete");
		validMethods.add("beginTransaction");
		validMethods.add("get");
		validMethods.add("load");
		validMethods.add("save");
		validMethods.add("persist");
		validMethods.add("refresh");
		validMethods.add("replicate");
		validMethods.add("saveOrUpdate");
		validMethods.add("update");
	}
	
	public int getArgLength() {
		return 1;
	}
	
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		if (args.length != getArgLength())
			return false;
		try {
			String fileUri = args[0].getURI();
			String content = fileRetrievementService.getContent(fileUri);
			String className = getClassFromJavaFilePath(fileUri);
			List<String> methodCalls = javaService.getMethodCalls(content, className);
			Collections.disjoint(methodCalls, validMethods);
		}
		catch (FileRetrievementServiceException e) {
			e.printError();
			return false;
		}
	}
}
