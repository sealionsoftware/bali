package bali.compiler.type;

import bali.annotation.Kind;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A Class is a compiled Type Template
 *
 * User: Richard
 * Date: 28/08/13
 */
public class MutableClassModel implements Class {

	private String name;
	private List<Type> superTypes;
	private List<Declaration<Type>> typeParameters;
	private List<Type> interfaces;
	private List<Declaration<Site>> parameters;
	private List<Method> methods;
	private List<Operator> operators;
	private List<UnaryOperator> unaryOperators;
	private List<Declaration<Site>> properties;
	private Kind metaType;

	public MutableClassModel(String name) {
		this(
				name,
				Collections.<Type>emptyList(),
				Collections.<Declaration<Type>>emptyList(),
				Collections.<Type>emptyList(),
				Collections.<Declaration<Site>>emptyList(),
				Collections.<Method>emptyList(),
				Collections.<Operator>emptyList(),
				Collections.<UnaryOperator>emptyList(),
				Collections.<Declaration<Site>>emptyList(),
				Kind.INTERFACE
		);
	}

	public MutableClassModel(String name, List<Type> superTypes, List<Declaration<Type>> typeParameters, List<Type> interfaces, List<Declaration<Site>> parameters, List<Method> methods, List<Operator> operators, List<UnaryOperator> unaryOperators, List<Declaration<Site>> properties, Kind metaType) {
		this.name = name;
		this.superTypes = superTypes;
		this.typeParameters = typeParameters;
		this.interfaces = interfaces;
		this.parameters = parameters;
		this.methods = methods;
		this.operators = operators;
		this.unaryOperators = unaryOperators;
		this.properties = properties;
		this.metaType = metaType;
	}

	public String getName() {
		return name;
	}

	public List<Type> getSuperTypes() {
		return superTypes;
	}

	public List<Declaration<Type>> getTypeParameters() {
		return typeParameters;
	}

	public List<Declaration<Site>> getParameters() {
		return parameters;
	}

	public List<Operator> getOperators() {
		return operators;
	}

	public List<UnaryOperator> getUnaryOperators() {
		return unaryOperators;
	}

	public List<Declaration<Site>> getProperties() {
		return properties;
	}

	public List<Type> getInterfaces() {
		return interfaces;
	}

	public List<Method> getMethods() {
		return methods;
	}

	public Kind getMetaType() {
		return metaType;
	}

	public void setSuperTypes(List<Type> superTypes) {
		this.superTypes = superTypes;
	}

	public void setMetaType(Kind metaType) {
		this.metaType = metaType;
	}

	public void setTypeParameters(List<Declaration<Type>> typeParameters) {
		this.typeParameters = typeParameters;
	}

	public void setInterfaces(List<Type> interfaces) {
		this.interfaces = interfaces;
	}

	public void setParameters(List<Declaration<Site>> parameters) {
		this.parameters = parameters;
	}

	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}

	public void setOperators(List<Operator> operators) {
		this.operators = operators;
	}

	public void setUnaryOperators(List<UnaryOperator> unaryOperators) {
		this.unaryOperators = unaryOperators;
	}

	public void setProperties(List<Declaration<Site>> properties) {
		this.properties = properties;
	}

	public boolean equals(Object o) {
		return (o instanceof Class) && name.equals(((Class) o).getName());
	}

	public int hashCode() {
		return name.hashCode();
	}

	public Method getMethod(String name) {

		for (Method method : methods) {
			if (method.getName().equals(name)) {
				return method;
			}
		}
		for (Operator operator : operators){
			if (operator.getMethodName().equals(name)){
				return new Method(name, operator.getType(), Collections.singletonList(new Declaration<>(null, operator.getParameter())));
			}
		}
		for (UnaryOperator unaryOperator : unaryOperators){
			if (unaryOperator.getMethodName().equals(name)){
				return new Method(name, unaryOperator.getType(), Collections.<Declaration<Site>>emptyList());
			}
		}
		for (Type superType : superTypes){
			Method superMethod = superType.getTemplate().getMethod(name);
			if (superMethod != null){
				return superMethod;
			}
		}

		return null;
	}


	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		if (typeParameters.size() > 0){
			sb.append("[");
			Iterator<Declaration<Type>> i = typeParameters.iterator();
			sb.append(i.next().getName());
			while(i.hasNext()){
				sb.append(",").append(i.next().getName());
			}
			sb.append("]");
		}
		return sb.toString();
	}
}
