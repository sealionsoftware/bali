package bali.compiler.parser.tree;

import bali.compiler.type.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public abstract class TypeNode<M extends MethodNode> extends Node {

	private String className;
	private List<SiteNode> implementations = new ArrayList<>();
	private List<TypeParameterNode> typeParameters = new ArrayList<>();

	private String qualifiedClassName;
	private Type resolvedType;

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

	public List<TypeParameterNode> getTypeParameters() {
		return typeParameters;
	}

	public void addParameter(TypeParameterNode parameter) {
		typeParameters.add(parameter);
	}

	public List<SiteNode> getImplementations() {
		return implementations;
	}

	public void addImplementation(SiteNode implementation) {
		implementations.add(implementation);
	}

	public String getQualifiedClassName() {
		return qualifiedClassName;
	}

	public void setQualifiedClassName(String qualifiedClassName) {
		this.qualifiedClassName = qualifiedClassName;
	}

	public Type getResolvedType() {
		return resolvedType;
	}

	public void setResolvedType(Type type) {
		this.resolvedType = type;
	}

	public abstract Boolean getAbstract();

	public abstract List<M> getMethods();

	public abstract void addMethod(M method);

//	public Method getSite(String name, List<TypeReference> suppliedArgumentTypes) {
//		for (Method declaration : getMethods()) {
//			List<Declaration> argumentDeclarations = declaration.getTypeParameters();
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

	public List<Node> getChildren() {
		List<Node> ret = new ArrayList<>();
		ret.addAll(typeParameters);
		ret.addAll(implementations);
		return ret;
	}
}
