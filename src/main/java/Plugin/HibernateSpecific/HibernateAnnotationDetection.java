package Plugin.HibernateSpecific;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import Services.JavaService;
import Services.LanguageService;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;
import util.AnnotationConsumer;

import java.util.Set;

/**
 * Created by freddy on 23.10.17.
 */
public class HibernateAnnotationDetection extends BaseBuiltin {
	private FileRetrievementService fileRetrievementService;
	private JavaService javaService;
	private String baseUri = "http://softlang.de/";
	private String verbUri = baseUri + "refersTo";
	
	public HibernateAnnotationDetection() {
		this.fileRetrievementService = FileRetrievementService.getInstance();
		javaService = LanguageService.getInstance().getJavaService();
	}
	@Override
	public String getName() {
		return "HibernateAnnotationDetection";
	}
	
	@Override
	public void headAction(Node[] args, int length, RuleContext context) {
		if(args.length  != 1) {
			return;
		}
		checkArgs(length, context);
		
		try {
			Node javaFileNode = getArg(0, args, context);
			String javaFileURI = javaFileNode.getURI();
		

			String content = fileRetrievementService.getContent(javaFileURI);
			String className = getClass(javaFileURI);
			
			AnnotationConsumer.AnnotationValue annotationValue = javaService.getAnnotationByName(content, className, "Table");
			if(annotationValue != null) {
				System.out.println("Not null");
				
				String tableName = null;
				if(annotationValue.hasKeys()) {
					tableName = annotationValue.keyValues.get("name");
					tableName = tableName.replace("\"", "");
				}
				if(tableName == null) {
					tableName = className;
				}
				System.out.println("t=>" + tableName);
				Node attributeUri = NodeFactory.createURI(verbUri);
				Node attributeTable = NodeFactory.createURI(baseUri + "Table/"+ tableName);
				context.add(new Triple(javaFileNode, attributeUri, attributeTable));
			}
			
		}
		catch (FileRetrievementServiceException e) {
			e.printStackTrace();
		}
	}
	
	private String getClass(String uri) {
		return  uri.substring(uri.lastIndexOf("/") + 1, uri.length() - ".java".length());
	}
	
	
}
