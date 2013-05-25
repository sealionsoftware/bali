package bali.compiler.bytecode;

import bali.compiler.parser.tree.Variable;
import org.objectweb.asm.Label;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class VariableInfo {

	private Variable declaration;
	private Label start;
	private Label end;
	private Integer index;

	public VariableInfo(Variable declaration, Label start, Label end, Integer index) {
		this.declaration = declaration;
		this.start = start;
		this.end = end;
		this.index = index;
	}

	public Variable getDeclaration() {
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
