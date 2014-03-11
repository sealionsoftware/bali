package bali.compiler.type;

import java.util.List;

/**
 * User: Richard
 * Date: 12/02/14
 */
public class VariableType implements Type {

	private static final Type VOID_TYPE = new VoidType();

	private String name;
	private Type bound;

	public VariableType(String name, Type bound) {
		this.name = name;
		this.bound = bound != null ? bound : VOID_TYPE;
	}

	public String getName() {
		return name;
	}

	public boolean isAssignableTo(Type t) {
		if (t == this){
			return true;
		}
		if (t instanceof VariableType){
			VariableType vt = (VariableType) t;
			return name.equals(vt.getName());
		}
		return bound.isAssignableTo(t);
	}

	public Type getSuperType() {
		return bound.getSuperType();
	}

	public List<Site> getTypeArguments() {
		return bound.getTypeArguments();
	}

	public List<Declaration<Site>> getParameters() {
		return bound.getParameters();
	}

	public List<Method> getMethods() {
		return bound.getMethods();
	}

	public List<Type> getInterfaces() {
		return bound.getInterfaces();
	}

	public List<Operator> getOperators() {
		return bound.getOperators();
	}

	public List<UnaryOperator> getUnaryOperators() {
		return bound.getUnaryOperators();
	}

	public List<Declaration<Site>> getProperties() {
		return bound.getProperties();
	}

	public Class getTemplate() {
		return bound.getTemplate();
	}

	public String toString(){
		StringBuilder sb = new StringBuilder(name);
		if (!VOID_TYPE.equals(bound)){
			sb.append(" ").append(bound);
		}
		return sb.toString();
	}
}
