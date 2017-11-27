package Plugin.HibernateSpecific;

import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import Services.JavaService;
import Services.LanguageService;
import Services.UriService;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;
import util.AnnotationConsumer;

import java.util.Map;
import java.util.Set;

/**
 * Created by freddy on 23.10.17.
 */
public class HibernateAnnotationDetection extends BaseBuiltin {
	private FileRetrievementService fileRetrievementService;
	private JavaService javaService;
	private String baseUri = "http://softlang.com/";
	private UriService uriService;
	
	public HibernateAnnotationDetection() {
		this.fileRetrievementService = FileRetrievementService.getInstance();
		javaService = LanguageService.getInstance().getJavaService();
		uriService = new UriService();
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
			
			System.out.println("[HibernateAnnotationDetection:] " + javaFileURI);
			String content = fileRetrievementService.getContent(javaFileURI);
			String className = getClass(javaFileURI);
			String tableName = null;
			AnnotationConsumer.AnnotationValue annotationValue;

			Map<String, AnnotationConsumer.AnnotationValue> annotations = javaService.getAnnotations(content, className);
			if(annotations.containsKey("Table")) {
				annotationValue = annotations.get("Table");
				if (annotationValue.hasKeys()) {
					tableName = annotationValue.keyValues.get("name");
					tableName = tableName.replace("\"", "");
					
				}
			}
			else if (annotations.containsKey("Entity")) {
				annotationValue = annotations.get("Entity");
			}
			else {
				return;
			}
			
			if (tableName == null) {
				tableName = className;
			}
			
//			System.out.println("t=>" + tableName);
			Node attributeTable = NodeFactory.createURI(uriService.getUri()+ tableName.toUpperCase());
			String fragmentUri = javaFileURI + "#" + annotationValue.getStartLine() + ":" + annotationValue.getEndLine();
			Node fragmentUriNode = NodeFactory.createURI(fragmentUri);
				
			//
			context.add(new Triple(fragmentUriNode, uriService.getNodeRdfType(), uriService.getNodeFragmentUri()));
			context.add(new Triple(fragmentUriNode, uriService.getNodeElementOfUri(), uriService.getAnnotationLanguageType()));
			context.add(new Triple(fragmentUriNode, uriService.getNodePartOfUri(), javaFileNode));
			context.add(new Triple(fragmentUriNode, uriService.getNodeHibernateRefersTo(), attributeTable));
			context.add(new Triple(fragmentUriNode, uriService.getNodeHasRole(), uriService.getNodeHibernateMappingRole()));
		}
		catch (FileRetrievementServiceException e) {
			e.printStackTrace();
		}
		catch (Throwable x) {
			System.out.print("Annotation exception");
			x.printStackTrace();
		}
	}
	
	private String getClass(String uri) {
		return  uri.substring(uri.lastIndexOf("/") + 1, uri.length() - ".java".length());
	}
	
	
}
