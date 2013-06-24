package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.Iterator;
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

	public MethodDeclaration getDeclaration(String name, List<Type> suppliedArgumentTypes) {
		for (MethodDeclaration declaration : getMethods()) {
			List<Declaration> argumentDeclarations = declaration.getArguments();
			if (declaration.getName().equals(name)
					&& suppliedArgumentTypes.size() == argumentDeclarations.size()
					&& typesCompatible(argumentDeclarations, suppliedArgumentTypes)) {
				return declaration;
			}
		}
		return null;
	}

	public boolean typesCompatible(List<Declaration> argumentDeclarations, List<Type> suppliedArgumentTypes){
		Iterator<Type> i = suppliedArgumentTypes.iterator();
		Iterator<Declaration> j = argumentDeclarations.iterator();
		while(i.hasNext()){
			Type suppliedArgumentType = i.next();
			Declaration argumentDeclaration = j.next();
			if (!suppliedArgumentType.isAssignableTo(argumentDeclaration.getType())){
				return false;
			}
		}
		return true;
	}

	public List<Node> getChildren(){
		List<Node> ret =  new ArrayList<>();
		ret.addAll(parameters);
		ret.addAll(implementations);
		return ret;
	}
}
