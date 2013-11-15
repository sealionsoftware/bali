package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 09/05/13
 */
public class TryStatementNode extends ControlExpressionNode {

	private ControlExpressionNode main;
	private List<CatchStatementNode> catchStatements = new ArrayList<>();

	public TryStatementNode(Integer line, Integer character) {
		super(line, character);
	}

	public void setMain(ControlExpressionNode main) {
		children.add(main);
		this.main = main;
	}

	public void addCatchStatement(CatchStatementNode catchStatement){
		children.add(catchStatement);
		catchStatements.add(catchStatement);
	}

	public ControlExpressionNode getMain() {
		return main;
	}

	public List<CatchStatementNode> getCatchStatements() {
		return catchStatements;
	}

}
