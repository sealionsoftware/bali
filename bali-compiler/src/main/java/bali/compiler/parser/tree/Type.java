package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class Type extends Node {

	private String className;
	private List<Type> parameters = new ArrayList<>();
	private TypeDeclaration declaration;
	private Boolean erase = false;

	public Type() {
	}

	public Type(Integer line, Integer character) {
		super(line, character);
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public List<Type> getParameters() {
		return parameters;
	}

	public void addParameter(Type parameter) {
		this.parameters.add(parameter);
	}

	public List<Node> getChildren() {
		return new ArrayList<Node>(parameters);
	}

	public TypeDeclaration getDeclaration() {
		return declaration;
	}

	public void setDeclaration(TypeDeclaration declaration) {
		this.declaration = declaration;
	}

	public Boolean isAssignableTo(Type site) {

		if (declaration == null || site.getDeclaration() == null){
			return false;
		}

		String ths = declaration.getQualifiedClassName();
		String tht = site.getDeclaration().getQualifiedClassName();

		if (ths == null || tht == null || parameters.size() != site.parameters.size()) {
			return false;
		}

		if (!ths.equals(tht) && !site.getDeclaration().getAbstract() && !isImplementation(site)){
			return false;
		}

		Iterator<Type> i = parameters.iterator();
		Iterator<Type> j = site.parameters.iterator();

		while (i.hasNext()) {
			Type nextParameter = i.next();
			Type nextSiteParameter = j.next();
			if (nextParameter == null || (nextParameter != this && !nextParameter.isAssignableTo(nextSiteParameter))) {
				return false;
			}
		}

		return true;
	}

	private boolean isImplementation(Type site){
		for (Type iface : (List<Type>) declaration.getImplementations()){
			try {
				iface = iface.getResolvedType(this);
			} catch (CouldNotResolveException e) {
				return false;
			}
			if (iface.isAssignableTo(site)){
				return true;
			}
		}
		return false;
	}

	public Boolean getErase() {
		return erase;
	}

	public void setErase(Boolean erase) {
		this.erase = erase;
	}

	public Type getResolvedType(Type top) throws CouldNotResolveException {

		Type possiblyParameterized = this;
		Type ret = new Type();

		if (possiblyParameterized.getErase()){
			String parameterName = possiblyParameterized.getClassName();
			possiblyParameterized = top.getParameters().get(getParameterIndexForName(parameterName, top.getDeclaration()));
		}

		ret.setClassName(possiblyParameterized.getClassName());
		ret.setDeclaration(possiblyParameterized.getDeclaration());
		ret.setErase(possiblyParameterized.getErase());

		for (Type parameterType : possiblyParameterized.getParameters()){
			ret.addParameter(parameterType.getResolvedType(top));
		}

		return ret;
	}

	private int getParameterIndexForName(String parameterName, TypeDeclaration declaringType) throws CouldNotResolveException {
		List<Type> parameters = declaringType.getParameters();
		int i = 0;
		for (Type parameter : parameters){
			if (parameter.getClassName().equals(parameterName)){
				return i;
			}
		}
		throw new CouldNotResolveException();
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Type other = (Type) o;
		String thisClassName = declaration != null ? declaration.getQualifiedClassName() : className;
		String thatClassName = other.declaration != null ? other.declaration.getQualifiedClassName() : other.className;

		if (!thisClassName.equals(thatClassName)){
			return false;
		}

		return parameters.equals(other.parameters);
	}

	public String toString() {

		String declarationClassName = declaration != null ? declaration.getQualifiedClassName() : className;
		StringBuilder sb = new StringBuilder(declarationClassName);
		if (parameters.size() > 0) {
			sb.append("<");
			Iterator<Type> i = parameters.iterator();
			sb.append(i.next());
			while (i.hasNext()) {
				sb.append(",").append(i.next());
			}
			sb.append(">");
		}
		return sb.toString();
	}

	public static class CouldNotResolveException extends Throwable {
	}
}
