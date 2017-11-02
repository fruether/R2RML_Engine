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
	private String referenceLanguage;
	private String uriUri;
	
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
	
	public Node getNodeReferenceLanguage() {
		return NodeFactory.createURI(getReferenceLanguage());
	}
	
	
	public Node getNodeElementOfUri() {
		return NodeFactory.createURI(getElementOfUri());
	}
	
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
	}

	
}
