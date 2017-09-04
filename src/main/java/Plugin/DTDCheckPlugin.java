package Plugin;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by freddy on 03.09.17.
 */
public class DTDCheckPlugin extends BaseBuiltin {
	
	@Override
	public String getName() {
		return "DTDCheckPlugin";
	}
	public int getArgLength() {
		return 2;
	}
	
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		if(args.length > 2) return false;
		
		String uri_xml = args[0].getURI();
		String uri_dtd = args[1].getURI();
		String dtd_file = last(uri_dtd.split("/"));
		
		String regex = ".*<!DOCTYPE\\s+[a-zA-Z_0-9|-]+\\s+PUBLIC\\s+\".*\"\\s+\"(.*?)\"\\s*>.*";
		
		boolean dtdDef = false;
		try {
			String content = FileRetrievementService.getInstance().getContent(uri_xml);
			dtdDef = content.contains("<!DOCTYPE");
			
			if(!dtdDef) {
				return false;
			}
			/*/
			String content = "Hallo<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" "
					+ "\"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\"> Bro";
			String content2 = "<!DOCTYPE hibernate-Configuration PUBLIC\n"
					+ " \"-//Hibernate/Hibernate Configuration DTD 3.0//EN\" "
					+ "\"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd\">";
			*/
			
			final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
			final Matcher matcher = pattern.matcher(content);
			if(!matcher.find()) return false;
			String dtdFile_reference = last(matcher.group(1).split("/"));
			
			System.out.println(matcher.group(1) + " " + dtdFile_reference);
			return  dtd_file.equals(dtdFile_reference);
			
		}
		catch (FileRetrievementServiceException e) {
			e.printStackTrace();
		}
		return  false;
	}
	
	private<T> T last(T[] element) {
		return  element[element.length - 1];
	}
}
