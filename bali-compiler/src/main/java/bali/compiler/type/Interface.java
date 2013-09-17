package bali.compiler.type;

import java.util.List;

/**
 * User: Richard
 * Date: 27/08/13
 */
public class Interface extends MethodDeclaringType {

	private List<Operator> operators;
	private List<UnaryOperator> unaryOperators;

	public Interface(String className,
	                 List<Declaration> parameters,
	                 List<Method> methods,
	                 List<Site> interfaces,
	                 List<Operator> operators,
	                 List<UnaryOperator> unaryOperators) {
		super(className, parameters, methods, interfaces);
		this.operators = operators;
		this.unaryOperators = unaryOperators;
	}

	public List<Operator> getOperators() {
		return operators;
	}

	public List<UnaryOperator> getUnaryOperators() {
		return unaryOperators;
	}

	public boolean isAbstract() {
		return true;
	}
}
