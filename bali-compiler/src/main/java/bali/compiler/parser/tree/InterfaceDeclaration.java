package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class InterfaceDeclaration extends TypeDeclaration<Method> {

	private List<TypeReference> superInterfaces = new ArrayList<>();
	private List<Method> methodDeclarations = new ArrayList<>();

	public InterfaceDeclaration() {
	}

	public InterfaceDeclaration(Integer line, Integer character) {
		super(line, character);
	}

	public List<TypeReference> getSuperInterfaces() {
		return superInterfaces;
	}

	public void addSuperInterface(TypeReference superInterface) {
		this.superInterfaces.add(superInterface);
	}

	public List<Method> getMethods() {
		return methodDeclarations;
	}

	public void addMethod(Method methodDeclaration) {
		this.methodDeclarations.add(methodDeclaration);
	}

	public Boolean getAbstract() {
		return true;
	}

	public List<Node> getChildren() {
		List<Node> children = super.getChildren();
		children.addAll(superInterfaces);
		children.addAll(methodDeclarations);
		return children;
	}
}
