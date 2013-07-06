package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 09/05/13
 */
public class TryStatement extends Statement {

	private CodeBlock main;
	private List<CatchStatement> catchStatements = new ArrayList<>();

	public TryStatement(Integer line, Integer character) {
		super(line, character);
	}

	public void setMain(CodeBlock main) {
		this.main = main;
	}

	public void addCatchStatement(CatchStatement catchStatement){
		catchStatements.add(catchStatement);
	}

	public CodeBlock getMain() {
		return main;
	}

	public List<CatchStatement> getCatchStatements() {
		return catchStatements;
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(main);
		children.addAll(catchStatements);
		return children;
	}
}
