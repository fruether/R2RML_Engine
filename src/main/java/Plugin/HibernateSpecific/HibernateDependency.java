package Plugin.HibernateSpecific;

import Plugin.Dependency;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by freddy on 14.09.17.
 */
public class HibernateDependency extends Dependency {
	
	public HibernateDependency() throws ParserConfigurationException {
	}
	
	@Override
	protected String getDependencyName() {
		return "org.hibernate";
	}
	
	@Override
	public String getName() {
		return "HibernateDependencyCheck";
	}
}
