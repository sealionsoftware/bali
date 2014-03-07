package bali.compiler.parser.tree;

import bali.compiler.type.Site;

/**
 * User: Richard
 * Date: 16/11/13
 */
public class ReferenceAssignmentNode extends AssignmentNode {

	private ReferenceNode reference;

	public ReferenceAssignmentNode() {
	}

	public ReferenceAssignmentNode(Integer line, Integer character) {
		super(line, character);
	}

	public ReferenceNode getReference() {
		return reference;
	}

	public void setReference(ReferenceNode reference) {
		this.reference = reference;
		children.add(reference);
	}

	public Site getType() {
		return reference.getType();
	}
}
