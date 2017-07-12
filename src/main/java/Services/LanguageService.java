package Services;

import com.github.javaparser.*;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.apache.jena.base.Sys;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringBufferInputStream;

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
		String result="";
		switch (prefix) {
			case "java" : result = ((parseJava(content)) ? "Java" :  ""); break;
			case "xml" : result = ((parseXML(content)) ?   "XML"  :  ""); break;
			case "xsd" : result  = ((parseXSD(content)) ?  "XSD"  :  ""); break;
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
	
	private boolean parseXML(String content) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new ByteArrayInputStream(content.getBytes()));
			builder.parse(is);
		}
		catch (SAXException ex) {
			return false;
		}
		catch (Exception ex) {
			System.out.println("A non parsing error occured in parseXML()");
			return false;
		}
		return true;
	}
	
	private boolean parseXSD(String content) {
		String schemaLang = "http://www.w3.org/2001/XMLSchema";
		SchemaFactory factory = SchemaFactory.newInstance(schemaLang);
		try {
			Schema schema = factory.newSchema(new StreamSource("XML/XMLSchema.xsd"));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new ByteArrayInputStream(content.getBytes())));
		}
		catch (SAXException sax) {
			return false;
		}
		catch (IOException io) {
			return false;
		}
		return true;
	}
}
