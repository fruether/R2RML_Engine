package Services;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
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
	
	private ValidationService() {
		matched = new HashSet<>();
	}
	static public ValidationService getInstance() {
		if (instance == null) {
			instance = new ValidationService();
		}
		return instance;
	}
	
	public boolean validateXMLSchema(String path, String XSDpath) {
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
	
	public boolean wasSuccessValidated(String path) {
		return matched.contains(path);
	}
	
	public void clean() {
		matched.clear();
	}
}
