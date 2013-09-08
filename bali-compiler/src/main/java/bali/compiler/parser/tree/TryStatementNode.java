package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 09/05/13
 */
public class TryStatementNode extends StatementNode {

	private CodeBlockNode main;
	private List<CatchStatementNode> catchStatements = new ArrayList<>();

	public TryStatementNode(Integer line, Integer character) {
		super(line, character);
	}

	public void setMain(CodeBlockNode main) {
		this.main = main;
	}

	public void addCatchStatement(CatchStatementNode catchStatement){
		catchStatements.add(catchStatement);
	}

	public CodeBlockNode getMain() {
		return main;
	}

	public List<CatchStatementNode> getCatchStatements() {
		return catchStatements;
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(main);
		children.addAll(catchStatements);
		return children;
	}
}
