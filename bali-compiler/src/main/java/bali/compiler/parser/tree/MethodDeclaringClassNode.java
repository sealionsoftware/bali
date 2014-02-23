package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public abstract class MethodDeclaringClassNode<M extends MethodNode> extends ClassNode {

	private String className;
	private List<SiteNode> implementations = new ArrayList<>();
	private List<TypeParameterNode> typeParameters = new ArrayList<>();
	private List<M> methods = new ArrayList<>();

	public MethodDeclaringClassNode() {
	}

	public MethodDeclaringClassNode(Integer line, Integer character) {
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
