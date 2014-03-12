package bali.compiler.type;

import java.util.Collections;
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
		this.bound = bound;
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
		if (bound == null){
			return true;
		}
		return bound.isAssignableTo(t);
	}

	public Type getSuperType() {
		if (bound == null){
			return null;
		}
		return bound.getSuperType();
	}

	public List<Site> getTypeArguments() {
		if (bound == null){
			return Collections.emptyList();
		}
		return bound.getTypeArguments();
	}

	public List<Declaration<Site>> getParameters() {
		if (bound == null){
			return Collections.emptyList();
		}
		return bound.getParameters();
	}

	public List<Method> getMethods() {
		if (bound == null){
			return Collections.emptyList();
		}
		return bound.getMethods();
	}

	public List<Type> getInterfaces() {
		if (bound == null){
			return Collections.emptyList();
		}
		return bound.getInterfaces();
	}

	public List<Operator> getOperators() {
		if (bound == null){
			return Collections.emptyList();
		}
		return bound.getOperators();
	}

	public List<UnaryOperator> getUnaryOperators() {
		if (bound == null){
			return Collections.emptyList();
		}
		return bound.getUnaryOperators();
	}

	public List<Declaration<Site>> getProperties() {
		if (bound == null){
			return Collections.emptyList();
		}
		return bound.getProperties();
	}

	public Class getTemplate() {
		if (bound == null){
			return null;
		}
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
