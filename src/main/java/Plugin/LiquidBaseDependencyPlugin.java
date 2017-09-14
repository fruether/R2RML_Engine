package Plugin;

import javax.xml.parsers.*;

/**
 * Created by freddy on 04.07.17.
 */
public class LiquidBaseDependencyPlugin extends Dependency {
	
	public String getName() {
		return "liquidBaseDependencyCheck";
	}
	
	public int getArgLength() {
		return 1;
	}
	
	public LiquidBaseDependencyPlugin() throws ParserConfigurationException{
		super();
	}
	
	@Override
	protected String getDependencyName() {
		return "org.liquibase";
	}
}
