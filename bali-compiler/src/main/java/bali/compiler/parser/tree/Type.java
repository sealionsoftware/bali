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

		if (!ths.equals(tht)){
			for (Type iface : new ArrayList<Type>(site.getDeclaration().getImplementations())){ //TODO: why does this need wrapping?
				if (isAssignableTo(iface)){
					break;
				}
			}
		}

		Iterator<Type> i = parameters.iterator();
		Iterator<Type> j = site.parameters.iterator();

		while (i.hasNext()) {
			Type next = i.next();
			if (next == null || (next != this && !next.isAssignableTo(j.next()))) {
				return false;
			}
		}

		return true;
	}

	public Boolean getErase() {
		return erase;
	}

	public void setErase(Boolean erase) {
		this.erase = erase;
	}

	public String toString() {

		String declarationClassName = declaration != null ? declaration.getQualifiedClassName() : "null";
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
}
