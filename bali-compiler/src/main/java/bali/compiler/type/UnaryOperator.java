package bali.compiler.type;

/**
 * User: Richard
 * Date: 08/09/13
 */
public class UnaryOperator extends Declaration<Site> {

	private String methodName;

	public UnaryOperator(String name, Site type, String methodName) {
		super(name, type);
		this.methodName = methodName;
	}

	public String getMethodName() {
		return methodName;
	}
}
