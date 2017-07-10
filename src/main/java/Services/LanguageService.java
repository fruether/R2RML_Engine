package Services;

import com.github.javaparser.*;

/**
 * Created by freddy on 09.07.17.
 *
 * Fragen
 *  - Gib es in diesem Sertvice auch Methoden wie getImport
 *  - Soll dies im Plugin sein oder im  Service?
 *  -
 */
public class LanguageService {
	private static LanguageService languageService;
	private LanguageService() {
	
	}
	
	public static LanguageService getInstance() {
		if(languageService == null) {
			languageService = new LanguageService();
		}
		return languageService;
	}
	
	public String getLanguage(String prefix, String content) {
		String result ="";
		switch (prefix) {
			case "java" : result = ((parseJava(content)) ?   "Java" :  ""); break;
			case "xml" : result = "XML"; break;
			case "xsd" : result  ="XSD"; break;
			default: break;
		}
		return result;
	}
	
	
	private boolean parseJava(String content) {
		try {
			JavaParser.parse(content);
		}
		catch (ParseProblemException parseProblem) {
			return false;
		}
		return true;
		
	}
}
