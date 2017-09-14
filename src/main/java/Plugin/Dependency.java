package Plugin;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by freddy on 14.09.17.
 */
public abstract class Dependency extends BaseBuiltin {
	private DocumentBuilder builder;
	
	public Dependency() throws ParserConfigurationException {
		DocumentBuilderFactory factory =  DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
	}
	
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		if (!args[0].isURI()) return false;
		
		String uri = args[0].getURI();
		System.out.println("[LiquidBaseDependencyPlugin] with " + uri);
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
			if(value.equals(getDependencyName())) {
				return true;
			}
		}
		return false;
	}
	
	protected abstract String getDependencyName();

}
