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
		String uri_xsd = args[1].getURI();
		String regex = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"(.*?)\">";
		
		boolean dtdDef = false;
		try {
			dtdDef = true;
			
			if(!dtdDef) {
				FileRetrievementService.getInstance().checkContent(uri_xml,"<!DOCTYPE");
				return false;
			}
			String content = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">";
			final Pattern pattern = Pattern.compile(regex);
			final Matcher matcher = pattern.matcher(content);
			matcher.find();
			System.out.println(matcher.group(1)); // Prints String I want to extract
			
			
		}
		catch (FileRetrievementServiceException e) {
			e.printStackTrace();
		}
		
		return true;
	}
}
