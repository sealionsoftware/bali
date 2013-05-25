package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class ConditionalStatement extends Statement {

	private List<ConditionalBlock> conditionalBlocks = new ArrayList<>();
	private CodeBlock alternate;

	public ConditionalStatement(Integer line, Integer character) {
		super(line, character);
	}

	public void addConditionalBlock(ConditionalBlock conditionalBlock){
		this.conditionalBlocks.add(conditionalBlock);
	}

	public List<ConditionalBlock> getConditionalBlocks() {
		return conditionalBlocks;
	}

	public CodeBlock getAlternate() {
		return alternate;
	}

	public void setAlternate(CodeBlock alternate) {
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
