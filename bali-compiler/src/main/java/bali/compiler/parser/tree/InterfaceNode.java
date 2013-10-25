package bali.compiler.parser.tree;


import java.util.ArrayList;
import java.util.List;

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
