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
	private String qualifiedClassName;
	private List<Type> parameters = new ArrayList<>();
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

	public String getQualifiedClassName() {
		return qualifiedClassName;
	}

	public void setQualifiedClassName(String qualifiedClassName) {
		this.qualifiedClassName = qualifiedClassName;
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

	//TODO: sub/super etc
	public Boolean isAssignableTo(Type site) {

		String ths = qualifiedClassName;
		String tht = site.qualifiedClassName;

		if (ths == null || tht == null || !ths.equals(tht) || parameters.size() != site.parameters.size()) {
			return false;
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

		StringBuilder sb = new StringBuilder(qualifiedClassName);
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
