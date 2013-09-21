package bali.compiler.bytecode;

import bali.compiler.parser.tree.MethodNode;
import bali.compiler.parser.tree.TypeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 19/06/13
 */
public class TestNode extends TypeNode<MethodNode> {

	public TestNode(String name) {
		setQualifiedClassName(name);
	}

	public List<MethodNode> getMethods() {
		return new ArrayList<>();
	}

	public void addMethod(MethodNode method) {
	}

	public Boolean getAbstract() {
		return false;
	}
}
