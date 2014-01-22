package bali.compiler.type;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 29/08/13
 */
public class CopySite implements Site {

	private boolean nullable;
	private boolean threadSafe;
	private Type type;
	private Site superType;
	private List<Declaration> typeParameters;
	private List<Site> interfaces;
	private List<Declaration> parameters;
	private List<Method> methods;
	private List<Operator> operators;
	private List<UnaryOperator> unaryOperators;
	private List<Declaration> properties;

	public CopySite(boolean nullable, Type type, Site superType, List<Declaration> typeParameters, List<Site> interfaces, List<Declaration> parameters, List<Method> methods, List<Operator> operators, List<UnaryOperator> unaryOperators, List<Declaration> properties, boolean threadSafe) {
		this.nullable = nullable;
		this.type = type;
		this.superType = superType;
		this.typeParameters = typeParameters;
		this.interfaces = interfaces;
		this.parameters = parameters;
		this.methods = methods;
		this.operators = operators;
		this.unaryOperators = unaryOperators;
		this.properties = properties;
		this.threadSafe = threadSafe;
	}

	//TODO: Unify with ParameterizedSite
	public boolean isAssignableTo(Site t) {

		if (t == null) {
			return true;
		}

		if (nullable && !t.isNullable()){
			return false;
		}

		if (t.isThreadSafe() && !threadSafe){
			return false;
		}

		if (getName().equals(t.getName())) {
			Iterator<Declaration> i = t.getParameters().iterator();
			for (Declaration argument : getParameters()) {
				Declaration parameter = i.next();
				if (!argument.getType().isAssignableTo(parameter.getType())) {
					return false;
				}
			}
			return true;
		}

		Site superType = getSuperType();
		if (superType != null && getSuperType().isAssignableTo(t)){
			return true;
		}

		for (Site iface : getInterfaces()) {
			if (iface.isAssignableTo(t)) {
				return true;
			}
		}

		return false;
	}

	public boolean isNullable() {
		return nullable;
	}

	public boolean isThreadSafe() {
		return threadSafe;
	}

	public String getName() {
		return getType().getName();
	}

	public Site getSuperType() {
		return superType;
	}

	public List<Declaration> getTypeParameters() {
		return new ArrayList<>(typeParameters);
	}

	public List<Declaration> getParameters() {
		return parameters;
	}

	public List<Method> getMethods() {
		return methods;
	}

	public List<Site> getInterfaces() {
		return interfaces;
	}

	public List<Operator> getOperators() {
		return operators;
	}

	public List<UnaryOperator> getUnaryOperators() {
		return unaryOperators;
	}

	public List<Declaration> getProperties() {
		return properties;
	}

	public Type getType() {
		return type;
	}

	public String toString() {

		if (type == null) {
			return "Not initialised";
		}

		StringBuilder sb = new StringBuilder(type.getName());
		if (nullable) {
			sb.append("?");
		}
		return sb.toString();
	}
}
