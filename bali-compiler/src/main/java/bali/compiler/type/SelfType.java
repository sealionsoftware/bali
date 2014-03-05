package bali.compiler.type;

import java.util.List;

/**
 * User: Richard
 * Date: 12/02/14
 */
public class SelfType implements Type {

	private Type self;

	public SelfType(Type self) {
		this.self = self;
	}

	public boolean isAssignableTo(Type t) {
		return self.isAssignableTo(t);
	}

	public Type getSuperType() {
		return self.getSuperType();
	}

	public List<Site> getTypeArguments() {
		return self.getTypeArguments();
	}

	public List<Declaration<Site>> getParameters() {
		return self.getParameters();
	}

	public List<Method> getMethods() {
		return self.getMethods();
	}

	public List<Type> getInterfaces() {
		return self.getInterfaces();
	}

	public List<Operator> getOperators() {
		return self.getOperators();
	}

	public List<UnaryOperator> getUnaryOperators() {
		return self.getUnaryOperators();
	}

	public List<Declaration<Site>> getProperties() {
		return self.getProperties();
	}

	public Class getTemplate() {
		return self.getTemplate();
	}

	public String toString(){
		return self.toString();
	}

	public boolean isAssignableTo(Site s) {
		return self.isAssignableTo(s);
	}
}
