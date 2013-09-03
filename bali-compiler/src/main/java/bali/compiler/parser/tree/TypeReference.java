package bali.compiler.parser.tree;


import bali.compiler.validation.type.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class TypeReference extends Node {

	private String className;
	private List<TypeReference> parameters = new ArrayList<>();
	private Type declaration;
	private List<Method> parametrisedMethods;
	private Boolean erase = false;

	private Map<String, TypeReference> resolvedParameters = new HashMap<>();

	public TypeReference() {
	}

	public TypeReference(Integer line, Integer character) {
		super(line, character);
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public List<TypeReference> getParameters() {
		return parameters;
	}

	public void addParameter(TypeReference parameter) {
		this.parameters.add(parameter);
	}

	public List<Node> getChildren() {
		return new ArrayList<Node>(parameters);
	}

	public Type getDeclaration() {
		return declaration;
	}

	public void setDeclaration(Type declaration) {
		this.declaration = declaration;
	}

	public List<Method> getParametrisedMethods() {
		return parametrisedMethods;
	}

//	public List<Method> resolveParametrizedTypes(TypeReference site) throws CouldNotResolveException {
//
//		if (declaration == null){
//			throw new RuntimeException("The Reference's TypeDeclaration has not been set");
//		}
//
//		List<Method> declaredMethods = declaration.getMethods();
//		List<Method> parametrizedMethods = new ArrayList<>(declaredMethods.size());
//
//		for (Method declaredMethod : declaredMethods){
//
//			TypeReference returnType = declaredMethod.getType();
//
//			Method parametrizedMethod = new Method();
//			parametrizedMethod.setName(declaredMethod.getName());
//			parametrizedMethod.setOperator(declaredMethod.getOperator());
//			if (returnType != null){
//				parametrizedMethod.setType(returnType.getResolvedType(site));
//			}
//			for (Declaration arg : declaredMethod.getArguments()){
//				Declaration parameterizedArg = new Argument();
//				parameterizedArg.setName(arg.getName());
//				parameterizedArg.setType(arg.getType().getResolvedType(site));
//				parametrizedMethod.addArgument(parameterizedArg);
//			}
//			parametrizedMethods.add(parametrizedMethod);
//		}
//
//		return parametrizedMethods;
//	}

	public Boolean isAssignableTo(Type site) {

//		if (declaration == null || site == null){
//			return false;
//		}
//
//		String ths = declaration.getQualifiedClassName();
//		String tht = site.getDeclaration().getQualifiedClassName();
//
//		if (ths == null || tht == null || parameters.size() != site.parameters.size()) {
//			return false;
//		}
//
//		if (!ths.equals(tht) && !site.getDeclaration().getAbstract() && !isImplementation(site)){
//			return false;
//		}
//
//		Iterator<TypeReference> i = parameters.iterator();
//		Iterator<TypeReference> j = site.parameters.iterator();
//
//		while (i.hasNext()) {
//			TypeReference nextParameter = i.next();
//			TypeReference nextSiteParameter = j.next();
//			if (nextParameter == null || (nextParameter != this && !nextParameter.isAssignableTo(nextSiteParameter))) {
//				return false;
//			}
//		}
//
//		return true;
		return false;
	}

//	private boolean isImplementation(Type site){
//		for (TypeReference iface : declaration.getImplementations()){
//			try {
//				iface = iface.getResolvedType(this);
//			} catch (CouldNotResolveException e) {
//				return false;
//			}
//			if (iface.isAssignableTo(site)){
//				return true;
//			}
//		}
//		return false;
//	}

	public java.lang.Boolean getErase() {
		return erase;
	}

	public void setErase(Boolean erase) {
		this.erase = erase;
	}

//	public TypeReference getResolvedType(TypeReference withParameters) throws CouldNotResolveException {
//
//		TypeReference possiblyParameterized = this;
//		TypeReference ret = new TypeReference();
//
//		if (possiblyParameterized.getErase()){
//			String parameterName = possiblyParameterized.getClassName();
//			possiblyParameterized = withParameters.getParameters().get(getParameterIndexForName(parameterName, withParameters.getDeclaration()));
//		}
//
//		ret.setClassName(possiblyParameterized.getClassName());
//		ret.setDeclaration(possiblyParameterized.getDeclaration());
//		ret.setErase(possiblyParameterized.getErase());
//
//		for (TypeReference parameterType : possiblyParameterized.getParameters()){
//			ret.addParameter(parameterType.getResolvedType(withParameters));
//		}
//
//		return ret;
//	}

//	private int getParameterIndexForName(String parameterName, TypeDeclaration<Method> declaringType) throws CouldNotResolveException {
//		List<TypeReference> parameters = declaringType.getParameters();
//		int i = 0;
//		for (TypeReference parameter : parameters){
//			if (parameter.getClassName().equals(parameterName)){
//				return i;
//			}
//		}
//		throw new CouldNotResolveException();
//	}

	public boolean equals(Object o) {

		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TypeReference other = (TypeReference) o;

		if (!declaration.getClassName().equals(other.declaration.getClassName())){
			return false;
		}

		return parameters.equals(other.parameters);
	}

	public String toString() {

		StringBuilder sb = new StringBuilder(className);
		if (parameters.size() > 0) {
			sb.append("<");
			Iterator<TypeReference> i = parameters.iterator();
			sb.append(i.next());
			while (i.hasNext()) {
				sb.append(",").append(i.next());
			}
			sb.append(">");
		}
		return sb.toString();
	}

	public static class CouldNotResolveException extends RuntimeException {
	}
}
