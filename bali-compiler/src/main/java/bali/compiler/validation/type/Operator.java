package bali.compiler.validation.type;

import java.util.List;

/**
 * User: Richard
 * Date: 08/09/13
 */
public class Operator extends Declaration {

	private Site parameter;
	private String methodName;

	public Operator(String name, Site type, Site parameter, String methodName) {
		super(name, type);
		this.parameter = parameter;
		this.methodName = methodName;
	}

	public Site getParameter() {
		return parameter;
	}

	public String getMethodName() {
		return methodName;
	}
}
