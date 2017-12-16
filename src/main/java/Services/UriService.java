package Services;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;

/**
 * Created by freddy on 30.10.17.
 */
public class UriService {
	private String elementOfUri;
	private String partOfUri;
	private String fragmentUri;
	private String sqlCreateStmt;
	private String baseUri;
	private String rdfType;
	private String tableType;
	private String hasRole;
	private String referenceLanguage;
	private String uriUri;
	private String refersTo;
	private String annotationLanguage;
	private String hibernateMappingRole;
	private String hibernateRefersTo;
	private String classUri;
	private String qualifiedName;
	private String embeddedRole;
	
	public String getElementOfUri() {
		return baseUri + elementOfUri;
	}
	public String getPartOfUri() {
		return baseUri + partOfUri;
	}
	public String getFragmentUri() {
		return baseUri + fragmentUri;
	}
	public String getLanguageSqlCreateStmt() {
		return baseUri + sqlCreateStmt;
	}
	public String getReferenceLanguage() {
		return baseUri + referenceLanguage;
	}
	public String getUri() {
		return baseUri + uriUri;
	}
	public String getRefersTo() {
		return baseUri + refersTo;
	}
	public String getAnnotationLanguage() {
		return baseUri + annotationLanguage;
	}
	public String getHibernateMappingRole() {
		return baseUri  + hibernateMappingRole;
	}
	public String getHasRole() {
		return baseUri + hasRole;
	}
	public String getClassUri() { return baseUri + classUri;}
	public String getQualifiedName() { return baseUri + qualifiedName;}
	public String getHibernateEmbeddedRole() { return baseUri + embeddedRole;}
	public Node getNodeHasRole() {return NodeFactory.createURI(getHasRole());}
	
	public String getHibernateRefersTo() {return baseUri + hibernateRefersTo;}
	
	public Node getNodeRefersTo() {
		return NodeFactory.createURI(getRefersTo());
	}
	public Node getNodeReferenceLanguage() {
		return NodeFactory.createURI(getReferenceLanguage());
	}
	public Node getNodeHibernateMappingRole() {return NodeFactory.createURI(getHibernateMappingRole());}
	public Node getNodeElementOfUri() {
		return NodeFactory.createURI(getElementOfUri());
	}
	public Node getNodeHibernateEmbeddedRole() {return NodeFactory.createURI(getHibernateEmbeddedRole());}
	
	public Node getNodePartOfUri() {
		return NodeFactory.createURI(getPartOfUri());
	}
	
	public Node getNodeFragmentUri() {
		return NodeFactory.createURI(getFragmentUri());
	}
	
	public Node getNodeLanguageSqlCreateStmt() {
		return NodeFactory.createURI(getLanguageSqlCreateStmt());
	}
	public Node getNodeRdfType() {
		return NodeFactory.createURI(rdfType);
	}
	public Node getNodeTableType() {
		return NodeFactory.createURI(baseUri + tableType);
	}
	public Node getAnnotationLanguageType() { return NodeFactory.createURI(getAnnotationLanguage());}
	public Node getNodeHibernateRefersTo() { return NodeFactory.createURI(getHibernateRefersTo());}
	public Node getNodeQualifiedName() {return NodeFactory.createURI(getQualifiedName());}
	
	public UriService(String baseUri) {
		this.baseUri = baseUri;
		if(baseUri.charAt(baseUri.length() - 1) != '/') {
			this.baseUri+= "/";
		}
		setUp();
	}
	
	public UriService() {
		this.baseUri = "http://softlang.com/";
		setUp();
	}
	
	private void setUp() {
		fragmentUri = "Fragment";
		partOfUri = "partOf";
		elementOfUri = "elementOf";
		sqlCreateStmt = "Language/SQLCreateTableStmt";
		rdfType = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
		tableType = "Table";
		referenceLanguage = "Language/ReferenceLanguage";
		uriUri = "URI/";
		refersTo = "refersTo";
		annotationLanguage = "Language/JavaAnnotatedElement";
		hibernateMappingRole = "HibernateMapping";
		embeddedRole = "HibernateEmbeddable";
		hasRole = "hasRole";
		classUri = "ClassURI/";
		hibernateRefersTo = "HibernateMappingRefersToRelationalTable";
		qualifiedName = "Language/QualifiedName";
	}

	
}
