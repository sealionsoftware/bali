package bali.compiler.type;

import java.util.List;

/**
 * User: Richard
 * Date: 27/08/13
 */
public class Bean extends Type {

	private List<Declaration> properties;

	public Bean(String className, List<Declaration> parameters, List<Declaration> properties) {
		super(className, parameters);
		this.properties = properties;
	}

	public List<Declaration> getProperties() {
		return properties;
	}

	public boolean isAbstract() {
		return false;
	}
}
