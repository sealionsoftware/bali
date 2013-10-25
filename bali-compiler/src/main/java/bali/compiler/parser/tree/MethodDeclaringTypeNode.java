package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;
import bali.compiler.type.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public abstract class MethodDeclaringTypeNode<M extends MethodNode> extends TypeNode {

	private String className;
	private List<SiteNode> implementations = new ArrayList<>();
	private List<TypeParameterNode> typeParameters = new ArrayList<>();
	private List<M> methods = new ArrayList<>();

	public MethodDeclaringTypeNode() {
	}

	public MethodDeclaringTypeNode(Integer line, Integer character) {
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

	public List<SiteNode> getImplementations() {
		return implementations;
	}

	public void addImplementation(SiteNode implementation) {
		children.add(implementation);
		implementations.add(implementation);
	}

	public List<M> getMethods() {
		return methods;
	}

	public void addMethod(M method) {
		this.methods.add(method);
		children.add(method);
	}

}
