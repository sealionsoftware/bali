package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 25/06/13
 */
public class CatchStatement extends Statement {

	private Declaration declaration;
	private CodeBlock codeBlock;

	public CatchStatement() {
	}

	public CatchStatement(Integer line, Integer character) {
		super(line, character);
	}

	public Declaration getDeclaration() {
		return declaration;
	}

	public void setDeclaration(Declaration declaration) {
		this.declaration = declaration;
	}

	public CodeBlock getCodeBlock() {
		return codeBlock;
	}

	public void setCodeBlock(CodeBlock codeBlock) {
		this.codeBlock = codeBlock;
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(declaration);
		children.add(codeBlock);
		return children;
	}
}
