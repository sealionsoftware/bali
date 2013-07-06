package bali.compiler.parser.tree;

import org.objectweb.asm.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class Variable extends Statement  {

	private Declaration declaration;
	private Expression value;

	public Variable() {
		super();
	}

	public Declaration getDeclaration() {
		return declaration;
	}

	public void setDeclaration(Declaration declaration) {
		this.declaration = declaration;
	}

	public Expression getValue() {
		return value;
	}

	public void setValue(Expression value) {
		this.value = value;
	}

	public Variable(Integer line, Integer character) {
		super(line, character);
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(declaration);
		if (value != null){
			children.add(value);
		}
		return children;
	}

}
