package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public abstract class TypeDeclaration<T extends MethodDeclaration> extends Node {

	private String className;
	private List<Type> implementations = new ArrayList<>();
	private List<Type> parameters = new ArrayList<>();

	private String qualifiedClassName;

	public TypeDeclaration() {
	}

	public TypeDeclaration(Integer line, Integer character) {
		super(line, character);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<Type> getParameters() {
		return parameters;
	}

	public void addParameter(Type parameter) {
		parameters.add(parameter);
	}

	public List<Type> getImplementations() {
		return implementations;
	}

	public void addImplementation(Type implementation) {
		implementations.add(implementation);
	}

	public String getQualifiedClassName() {
		return qualifiedClassName;
	}

	public void setQualifiedClassName(String qualifiedClassName) {
		this.qualifiedClassName = qualifiedClassName;
	}

	public abstract Boolean getAbstract();

	public abstract List<T> getMethods();

	public abstract void addMethod(T method);

	public List<Node> getChildren(){
		List<Node> ret =  new ArrayList<>();
		ret.addAll(parameters);
		ret.addAll(implementations);
		return ret;
	}
}
