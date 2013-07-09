package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 08/07/13
 */
public abstract class LoopStatement extends Statement {

	protected CodeBlock body;

	public LoopStatement(Integer line, Integer character) {
		super(line, character);
	}

	public CodeBlock getBody() {
		return body;
	}

	public void setBody(CodeBlock body) {
		this.body = body;
	}

	public List<Node> getChildren() {
		List<Node> ret = new ArrayList<>();
		ret.add(body);
		return ret;
	}
}
