package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;
import bali.compiler.type.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public abstract class TypeNode extends Node {

	private String className;
	private List<TypeParameterNode> typeParameters = new ArrayList<>();

	private BlockingReference<String> qualifiedClassName = new BlockingReference<>();
	private BlockingReference<Type> resolvedType = new BlockingReference<>();

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
		children.add(parameter);
		typeParameters.add(parameter);
	}

	public String getQualifiedClassName() {
		return qualifiedClassName.get();
	}

	public void setQualifiedClassName(String qualifiedClassName) {
		this.qualifiedClassName.set(qualifiedClassName);
	}

	public Type getResolvedType() {
		return resolvedType.get();
	}

	public void setResolvedType(Type type) {
		this.resolvedType.set(type);
	}

	public String toString() {
		return className;
	}

}
