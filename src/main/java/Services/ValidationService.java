package Services;

import org.apache.jena.base.Sys;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.swing.text.Document;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by freddy on 03.07.17.
 */
public class ValidationService {
	static private ValidationService instance;
	private Set<String> matched;
	private SchemaFactory sf;
	private ValidationService() {
		
		matched = new HashSet<>();
		sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		
	}
	static public ValidationService getInstance() {
		if (instance == null) {
			instance = new ValidationService();
		}
		return instance;
	}
	
	public boolean validateXMLSchema_Deprecated(String path, String XSDpath) {
		try {
			SchemaFactory factory =
					SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new File(XSDpath));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new File(path)));
		} catch (IOException | SAXException e) {
			System.out.println("Exception: " + e.getMessage());
			return false;
		}
		matched.add(path);
		return true;
	}
	public boolean validateXMLSchema(String path, String XSDpath) {
		try {
			Schema schema = sf.newSchema(new File(XSDpath));
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			dbf.setSchema(schema);
			DocumentBuilder db = dbf.newDocumentBuilder();
			db.setErrorHandler(new ErrorHandler() {
				
				@Override
				public void warning(SAXParseException exception) throws SAXException {
				
				}
				
				@Override
				public void error(SAXParseException exception) throws SAXException {
					throw exception;
				}
				
				@Override
				public void fatalError(SAXParseException exception) throws SAXException {
					throw exception;
				}
			});
			db.parse(new File(path));
			
		}
		catch (SAXException | IOException | ParserConfigurationException error) {
			return false;
		}
		matched.add(path);
		return true;
	}
	public boolean wasSuccessValidated(String path) {
		return matched.contains(path);
	}
	
	public void clean() {
		matched.clear();
	}
}
