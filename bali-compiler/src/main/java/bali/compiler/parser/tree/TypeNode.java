package bali.compiler.parser.tree;

import bali.compiler.type.Interface;
import bali.compiler.type.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public abstract class TypeNode<M extends MethodNode, T extends Type> extends Node {

	private String className;
	private List<SiteNode<Interface>> implementations = new ArrayList<>();
	private List<TypeParameterNode> parameters = new ArrayList<>();

	private String qualifiedClassName;
	private T resolvedType;

	public TypeNode() {
	}

	public TypeNode(Integer line, Integer character) {
		super(line, character);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<TypeParameterNode> getParameters() {
		return parameters;
	}

	public void addParameter(TypeParameterNode parameter) {
		parameters.add(parameter);
	}

	public List<SiteNode<Interface>> getImplementations() {
		return implementations;
	}

	public void addImplementation(SiteNode<Interface> implementation) {
		implementations.add(implementation);
	}

	public String getQualifiedClassName() {
		return qualifiedClassName;
	}

	public void setQualifiedClassName(String qualifiedClassName) {
		this.qualifiedClassName = qualifiedClassName;
	}

	public T getResolvedType() {
		return resolvedType;
	}

	public void setResolvedType(T type) {
		this.resolvedType = type;
	}

	public abstract Boolean getAbstract();

	public abstract List<M> getMethods();

	public abstract void addMethod(M method);

//	public Method getSite(String name, List<TypeReference> suppliedArgumentTypes) {
//		for (Method declaration : getMethods()) {
//			List<Declaration> argumentDeclarations = declaration.getTypeParameters();
//			if (declaration.getClassName().equals(name)
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

	public List<Node> getChildren() {
		List<Node> ret = new ArrayList<>();
		ret.addAll(parameters);
		ret.addAll(implementations);
		return ret;
	}
}
