package bali.compiler.bytecode;

import bali.compiler.parser.tree.DeclarationNode;
import org.objectweb.asm.Label;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class VariableInfo {

	private DeclarationNode declaration;
	private Label start;
	private Label end;
	private Integer index;

	public VariableInfo(DeclarationNode declaration, Label start, Label end, Integer index) {
		this.declaration = declaration;
		this.start = start;
		this.end = end;
		this.index = index;
	}

	public DeclarationNode getDeclaration() {
		return declaration;
	}

	public Label getStart() {
		return start;
	}

	public Label getEnd() {
		return end;
	}

	public Integer getIndex() {
		return index;
	}
}
