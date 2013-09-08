package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 08/07/13
 */
public abstract class LoopStatementNode extends StatementNode {

	protected CodeBlockNode body;

	public LoopStatementNode(Integer line, Integer character) {
		super(line, character);
	}

	public CodeBlockNode getBody() {
		return body;
	}

	public void setBody(CodeBlockNode body) {
		this.body = body;
	}

	public List<Node> getChildren() {
		List<Node> ret = new ArrayList<>();
		ret.add(body);
		return ret;
	}
}
