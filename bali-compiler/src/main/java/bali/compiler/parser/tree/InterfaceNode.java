package bali.compiler.parser.tree;


/**
 * User: Richard
 * Date: 29/04/13
 */
public class InterfaceNode extends MethodDeclaringTypeNode<MethodNode> {

	public InterfaceNode() {
	}

	public InterfaceNode(Integer line, Integer character) {
		super(line, character);
	}

}
