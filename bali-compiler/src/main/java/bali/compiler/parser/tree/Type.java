package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class Type extends Node {

	private String className;
	private String qualifiedClassName;
	private List<Type> parameters = new ArrayList<>();

	public Type() {}

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

//	public Boolean isAssignableTo(Type site){
//		String ths = qualifiedClassName;
//		String tht = site.qualifiedClassName;
//		return ths != null && tht != null && ths.equals(tht);
//	}


	public String toString() {
		return qualifiedClassName;
	}
}
