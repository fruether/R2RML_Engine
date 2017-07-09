package Services;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;

/**
 * Created by freddy on 09.07.17.
 *
 * Fragen
 *  - Gib es in diesem Sertvice auch Methoden wie getImport
 *  - Soll dies im Plugin sein oder im  Service?
 *  -
 */
public class LanguageService {
	
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
