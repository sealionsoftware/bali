package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public abstract class TypeDeclaration<T extends Method> extends Node {

	private String className;
	private List<TypeReference> implementations = new ArrayList<>();
	private List<TypeParameter> parameters = new ArrayList<>();

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

	public List<TypeParameter> getParameters() {
		return parameters;
	}

	public void addParameter(TypeParameter parameter) {
		parameters.add(parameter);
	}

	public List<TypeReference> getImplementations() {
		return implementations;
	}

	public void addImplementation(TypeReference implementation) {
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

//	public Method getDeclaration(String name, List<TypeReference> suppliedArgumentTypes) {
//		for (Method declaration : getMethods()) {
//			List<Declaration> argumentDeclarations = declaration.getArguments();
//			if (declaration.getName().equals(name)
//					&& suppliedArgumentTypes.size() == argumentDeclarations.size()
//					&& typesCompatible(argumentDeclarations, suppliedArgumentTypes)) {
//				return declaration;
//			}
//		}
//		return null;
//	}

//	public boolean typesCompatible(List<Declaration> argumentDeclarations, List<TypeReference> suppliedArgumentTypes){
//		Iterator<TypeReference> i = suppliedArgumentTypes.iterator();
//		Iterator<Declaration> j = argumentDeclarations.iterator();
//		while(i.hasNext()){
//			TypeReference suppliedArgumentType = i.next();
//			Declaration argumentDeclaration = j.next();
//			if (!suppliedArgumentType.isAssignableTo(argumentDeclaration.getType())){
//				return false;
//			}
//		}
//		return true;
//	}

	public List<Node> getChildren(){
		List<Node> ret =  new ArrayList<>();
		ret.addAll(parameters);
		ret.addAll(implementations);
		return ret;
	}
}
