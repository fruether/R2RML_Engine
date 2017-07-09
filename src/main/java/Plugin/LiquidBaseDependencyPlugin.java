package Plugin;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import Services.ValidationService;
import org.apache.jena.base.Sys;
import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by freddy on 04.07.17.
 */
public class LiquidBaseDependencyPlugin extends BaseBuiltin {
	private DocumentBuilder builder;
	
	public String getName() {
		return "liquidBaseDependencyCheck";
	}
	
	public int getArgLength() {
		return 1;
	}
	
	public LiquidBaseDependencyPlugin() throws ParserConfigurationException{
		DocumentBuilderFactory factory =  DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
	}
	
	
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		if (!args[0].isURI()) return false;
		
		String uri = args[0].getURI();
		System.out.println("Checking LiquidBaseDependencyPlugin");
		try {
			String content =  FileRetrievementService.getInstance().getContent(uri);
			InputSource is = new InputSource(new ByteArrayInputStream(content.getBytes()));
			Document doc = builder.parse(is);
			return checkDependency(doc);
		}
		catch (SAXException e) {
			System.out.println("SAXException: Error parsing XML content in LiquidBaseDependencyPlugin");
			System.out.println(e.getMessage());
			return false;
		}
		catch (IOException e) {
			System.out.println("IOException: Error parsing XML content in LiquidBaseDependencyPlugin");
			System.out.println(e.getMessage());
			return false;
		}
		catch (FileRetrievementServiceException fileRetrievementServiceException) {
			fileRetrievementServiceException.printError();
			return  false;
		}
	}
	private boolean checkDependency(Document doc) {
		if(doc == null) {
			return false;
		}
		NodeList elements = doc.getElementsByTagName("groupId");
		for(int i = 0; i < elements.getLength(); i++) {
			String value = elements.item(i).getTextContent();
			if(value.equals("org.liquibase")) {
				return true;
			}
		}
		return false;
	}
}
