package bali.compiler.type;

import java.util.List;

/**
 * User: Richard
 * Date: 27/08/13
 */
public class Method extends Declaration<Site> {

	private List<Declaration<Site>> parameters;

	public Method(String name, Site returnType, List<Declaration<Site>> parameters) {
		super(name, returnType);
		this.parameters = parameters;
	}

	public List<Declaration<Site>> getParameters() {
		return parameters;
	}
}
