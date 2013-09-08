package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class ConditionalStatementNode extends StatementNode {

	private List<ConditionalBlockNode> conditionalBlocks = new ArrayList<>();
	private CodeBlockNode alternate;

	public ConditionalStatementNode(Integer line, Integer character) {
		super(line, character);
	}

	public void addConditionalBlock(ConditionalBlockNode conditionalBlock){
		this.conditionalBlocks.add(conditionalBlock);
	}

	public List<ConditionalBlockNode> getConditionalBlocks() {
		return conditionalBlocks;
	}

	public CodeBlockNode getAlternate() {
		return alternate;
	}

	public void setAlternate(CodeBlockNode alternate) {
		this.alternate = alternate;
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.addAll(conditionalBlocks);
		if (alternate != null){
			children.add(alternate);
		}
		return children;
	}
}
