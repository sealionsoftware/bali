package bali.compiler.parser.tree;

import java.util.List;

/**
 * User: Richard
 * Date: 08/05/13
 */
public abstract class Node {

//	public Node getParent();

	private Integer line;
	private Integer character;

	protected Node(){}

	protected Node(Integer line, Integer character) {
		this.line = line;
		this.character = character;
	}

	public Integer getLine() {
		return line;
	}

	public Integer getCharacter() {
		return character;
	}

	public abstract List<Node> getChildren();

}
