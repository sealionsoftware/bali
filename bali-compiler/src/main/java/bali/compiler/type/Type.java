package bali.compiler.type;

import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 28/08/13
 */
public class Type {

	private String name;
	private List<Declaration> typeParameters;
	private List<Site> interfaces;
	private List<Declaration> parameters;
	private List<Method> methods;
	private List<Operator> operators;
	private List<UnaryOperator> unaryOperators;
	private List<Declaration> properties;

	private Boolean isAbstract;

	public Type(String name,
	            List<Declaration> typeParameters,
	            List<Site> interfaces,
	            List<Declaration> parameters,
	            List<Method> methods,
	            List<Operator> operators,
	            List<UnaryOperator> unaryOperators,
	            List<Declaration> properties,
	            Boolean isAbstract) {
		this.name = name;
		this.typeParameters = typeParameters;
		this.methods = methods;
		this.interfaces = interfaces;
		this.operators = operators;
		this.unaryOperators = unaryOperators;
		this.parameters = parameters;
		this.properties = properties;
		this.isAbstract = isAbstract;
	}

	public String getName() {
		return name;
	}

	public List<Declaration> getTypeParameters() {
		return typeParameters;
	}

	public List<Declaration> getParameters() {
		return parameters;
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

	public List<Site> getInterfaces() {
		return interfaces;
	}

	public List<Method> getMethods() {
		return methods;
	}

	public boolean isAbstract(){
		return isAbstract;
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Type type = (Type) o;

		if (!name.equals(type.name)) return false;
		if (!typeParameters.equals(type.typeParameters)) return false;

		return true;
	}

	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + typeParameters.hashCode();
		return result;
	}

	public Method getMethod(String name, List<Site> argumentTypes) {

		for (Method method : methods) {
			if (method.getName().equals(name)) {
				List<Declaration> parameters = method.getParameters();
				if (checkIfArgumentsAreAssignable(argumentTypes, parameters)) {
					return method;
				}
			}
		}
		return null;
	}

	private boolean checkIfArgumentsAreAssignable(List<Site> argumentTypes, List<Declaration> parameters) {
		if (argumentTypes.size() != parameters.size()) {
			return false;
		}
		Iterator<Site> i = argumentTypes.iterator();
		Iterator<Declaration> j = parameters.iterator();
		while (i.hasNext()) {
			if (!i.next().isAssignableTo(j.next().getType())) {
				return false;
			}
		}
		return true;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		if (typeParameters.size() > 0){
			sb.append("<");
			Iterator<Declaration> i = typeParameters.iterator();
			sb.append(i.next().getName());
			while(i.hasNext()){
				sb.append(",").append(i.next().getName());
			}
			sb.append(">");
		}
		return sb.toString();
	}
}
