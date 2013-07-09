package bali.compiler.bytecode;

import org.objectweb.asm.Label;

/**
 * User: Richard
 * Date: 08/07/13
 */
public class LoopContext {

	private Label start;
	private Label end;

	public LoopContext(Label start, Label end) {
		this.start = start;
		this.end = end;
	}

	public Label getStart() {
		return start;
	}

	public Label getEnd() {
		return end;
	}
}
