package bali.compiler.parser.tree;


import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class InterfaceNode extends TypeNode<MethodNode> {

	private List<MethodNode> methodDeclarations = new ArrayList<>();

	public InterfaceNode() {
	}

	public InterfaceNode(Integer line, Integer character) {
		super(line, character);
	}

	public List<MethodNode> getMethods() {
		return methodDeclarations;
	}

	public void addMethod(MethodNode methodDeclaration) {
		this.methodDeclarations.add(methodDeclaration);
	}

	public Boolean getAbstract() {
		return true;
	}

	public List<Node> getChildren() {
		List<Node> children = super.getChildren();
		children.addAll(methodDeclarations);
		return children;
	}
}
