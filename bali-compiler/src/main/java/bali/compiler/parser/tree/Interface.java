package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class Interface extends Type {

	private List<Type> superInterfaces = new ArrayList<>();
	private List<MethodDeclaration> methodDeclarations = new ArrayList<>();

	public Interface() {}

	public Interface(Integer line, Integer character) {
		super(line, character);
	}

	public List<Type> getSuperInterfaces() {
		return superInterfaces;
	}

	public void addSuperInterface(Type superInterface){
		this.superInterfaces.add(superInterface);
	}

	public List<MethodDeclaration> getMethodDeclarations() {
		return methodDeclarations;
	}

	public void addMethodDeclaration(MethodDeclaration methodDeclaration){
		this.methodDeclarations.add(methodDeclaration);
	}

	public MethodDeclaration getDeclaration(String name){
		for (MethodDeclaration declaration : getMethodDeclarations()){
			if (declaration.getName().equals(name)){
				return declaration;
			}
		}
		return null;
	}

	public List<Node> getChildren() {
		List<Node> children = super.getChildren();
		children.addAll(superInterfaces);
		children.addAll(methodDeclarations);
		return children;
	}
}
