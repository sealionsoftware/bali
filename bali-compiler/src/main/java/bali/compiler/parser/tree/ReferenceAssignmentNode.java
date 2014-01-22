package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;
import bali.compiler.type.Site;

/**
 * User: Richard
 * Date: 16/11/13
 */
public class ReferenceAssignmentNode extends AssignmentNode {

	private ReferenceNode reference;
	private BlockingReference<String> setterName = new BlockingReference<>();

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

	public void setSetterName(String setterName) {
		this.setterName.set(setterName);
	}

	public String getSetterName() {
		return setterName.get();
	}

	public Site getType() {
		return reference.getType();
	}
}
