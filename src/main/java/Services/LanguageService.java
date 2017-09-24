package Services;

import com.github.javaparser.*;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
	
	public String getLanguage(String prefix, String content) throws LanguageServiceException {
		String result="";
		switch (prefix) {
			case "java" : result = ((parseJava(content)) ? "Java" :  ""); break;
			case "xml" : result = ((parseXML(content)) ?   "XML"  :  ""); break;
			case "xsd" : result  = ((parseXSD(content)) ?  "XSD"  :  ""); break;
			default: break;
		}
		return result;
	}
	
	public String getJavaClass(String content, String path) {
		CompilationUnit cu = JavaParser.parse( content );
		
		String packageName = cu.getPackageDeclaration().get().getNameAsString();
		String className = path.substring(path.lastIndexOf("/") + 1, path.length() - 5);

		return packageName + (packageName.isEmpty() ? "" : "." ) + className;
	}
	
	public List<String> getJavaImportetElements(String content) {
		ArrayList<String> importedElements = new ArrayList<>();
		
		CompilationUnit cu = JavaParser.parse( content );
		com.github.javaparser.ast.NodeList<ImportDeclaration> imports = cu.getImports();
		for(int i = 0; i < imports.size(); i++) {
			String importName = imports.get(i).getNameAsString();
			importedElements.add(importName);
		}
		return importedElements;
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
	
	protected class MethodCallVisitor extends VoidVisitorAdapter<Void> {
		@Override
		public void visit(MethodCallExpr n, Void arg) {
			// Found a method call
			System.out.println(n.getScope() + " - " + n.getName());
			// Don't forget to call super, it may find more method calls inside the arguments of this method call, for example.
			super.visit(n, arg);
		}
	}
	
	public List<String> getMethodCalls(String content, String className) {
		ArrayList<String> methodCalls = new ArrayList();
		CompilationUnit compilationUnit = JavaParser.parse(content);
		ClassOrInterfaceDeclaration mainClass = compilationUnit.getClassByName(className).orElseThrow(null);
		if(mainClass == null) return methodCalls;
		
		for(MethodDeclaration method : mainClass.getMethods()) {
			method.accept(new VoidVisitorAdapter<Void> (){
				
				public void visit (MethodCallExpr n, Void arg) {
					// Found a method call
					methodCalls.add(n.getNameAsString());
					super.visit(n, arg);
				}
			}
			, null);
			
		}
		return methodCalls;
	}
	
	public String getXMLFirstAttribute(String key, String tagName, String content) throws LanguageServiceException {
		String value = "";
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new ByteArrayInputStream(content.getBytes()));
			org.w3c.dom.Document doc = builder.parse(is);
			
			NodeList nList = doc.getElementsByTagName(key);
			if(nList.getLength() >= 1) {
				Node curNode = nList.item(0);
				value = curNode.getAttributes().getNamedItem(tagName).getNodeValue();
			}
		}
		catch (SAXException e) {
			throw new LanguageServiceException(e, "getXMLAttribute", "I  am not able to parse the XML. But this should be possible", key);
		}
		catch (IOException e) {
			throw new LanguageServiceException(e, "getXMLAttribute", "I  am not able to parse the XML. But this should be possible", key);
		}
		catch (ParserConfigurationException e) {
			throw new LanguageServiceException(e, "getXMLAttribute", "I  am not able to parse the XML. But this should be possible", key);
		}
		return value;
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
	
	private boolean parseXSD(String content) throws LanguageServiceException {
		String schemaLang = "http://www.w3.org/2001/XMLSchema";
		SchemaFactory factory = SchemaFactory.newInstance(schemaLang);
		try {
			URL path = getClass().getResource("/XML/XMLSchema.xsd");
			Schema schema = factory.newSchema(new StreamSource(new File(path.toURI())));
			
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new ByteArrayInputStream(content.getBytes())));
		}
		catch (SAXException sax) {
			System.out.println("Error parsing" + sax.getMessage());
			return false;
		}
		catch (IOException io) {
			throw new LanguageServiceException(io, "parseXSD", "We were not able to open a file. This should be the case", "N/A");
		}
		catch (Exception e) {
			throw new LanguageServiceException(e, "parseXSD", "This kind of exception should not appear", "N/A");
		}
		return true;
	}
}
