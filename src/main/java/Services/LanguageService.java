package Services;

import com.github.javaparser.*;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
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
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import nl.bigo.sqliteparser.*;
import util.AnnotationConsumer;
import util.Package;

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
	private SQLService sqlService;
	private JavaService javaService;
	
	private LanguageService() {
		
		sqlService = new SQLService();
		javaService = new JavaService();
	}
	
	public SQLService getSQLService() {
		return sqlService;
	}
	public static LanguageService getInstance() {
		if(languageService == null) {
			languageService = new LanguageService();
		}
		return languageService;
	}
	
	public JavaService getJavaService() {
		return javaService;
	}
	
	
	public String getLanguage(String prefix, String content) throws LanguageServiceException {
		String result="";
		switch (prefix) {
			case "java" : result = ((parseJava(content)) ? "Java" :  ""); break;
			case "xml" : result = ((parseXML(content)) ?   "XML"  :  ""); break;
			case "xsd" : result  = ((parseXSD(content)) ?  "XSD"  :  ""); break;
			case "sql" : result  = "SQL"/*((sqlService.parseSQL(content)) ?  "SQL"  :  "")*/; break;
			
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
				Node taggedNode = curNode.getAttributes().getNamedItem(tagName);
				if(taggedNode != null) {
					value = taggedNode.getNodeValue();
				}
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
