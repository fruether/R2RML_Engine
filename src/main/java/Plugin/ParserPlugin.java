package Plugin;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import Services.LanguageService;
import Services.LanguageServiceException;
import org.apache.jena.base.Sys;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

/**
 * Created by freddy on 09.07.17.
 */
public class ParserPlugin extends BaseBuiltin {
	private FileRetrievementService fileRetrievementService;
	private LanguageService languageService;
	public ParserPlugin() {
		fileRetrievementService = FileRetrievementService.getInstance();
		languageService = LanguageService.getInstance();
	}
	@Override
	public String getName() {
		return "Parse";
	}
	
	public int getArgLength() {
		return 2;
	}
	
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		if (!args[0].isURI()) return false;
		
		BindingEnvironment env = context.getEnv();
		String uri = args[0].getURI();
		System.out.println("[ParsePlugin] parsing " + uri);
		try {
			String content = fileRetrievementService.getContent(uri);
			String path = fileRetrievementService.uriToPath(uri);
			String prefix = getPrefix(path);
			String language = languageService.getLanguage(prefix,content);
			if(language.isEmpty()) return false;
			
			Node value = NodeFactory.createURI("http://softlang.com/Language/" + language);
			return env.bind(args[1], value);
		}
		catch (FileRetrievementServiceException exception) {
			exception.printError();
			return false;
		}
		catch (LanguageServiceException excption) {
			excption.printError();
			return false;
		}
	}
	
	private String getPrefix(String path) {
		String[] end = path.split("\\.");
		return end[end.length - 1];
	}
}
