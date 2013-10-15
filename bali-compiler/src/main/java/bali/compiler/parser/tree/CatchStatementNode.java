package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 25/06/13
 */
public class CatchStatementNode extends StatementNode {

	private DeclarationNode declaration;
	private CodeBlockNode codeBlock;

	public CatchStatementNode() {
	}

	public CatchStatementNode(Integer line, Integer character) {
		super(line, character);
	}

	public DeclarationNode getDeclaration() {
		return declaration;
	}

	public void setDeclaration(DeclarationNode declaration) {
		this.declaration = declaration;
		children.add(declaration);
	}

	public CodeBlockNode getCodeBlock() {
		return codeBlock;
	}

	public void setCodeBlock(CodeBlockNode codeBlock) {
		this.codeBlock = codeBlock;
		children.add(codeBlock);
	}

}
