package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;
import bali.compiler.reference.Reference;
import bali.compiler.type.Site;

/**
 * User: Richard
 * Date: 03/03/14
 */
public class ArgumentNode extends Node {

	private String name;
	private ExpressionNode value;

	private Reference<Site> resolvedType = new BlockingReference<>();

	public ArgumentNode() {
	}

	public ArgumentNode(Integer line, Integer character) {
		super(line, character);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ExpressionNode getValue() {
		return value;
	}

	public void setValue(ExpressionNode value) {
		this.value = value;
		children.add(value);
	}

	public Site getResolvedType() {
		return resolvedType.get();
	}

	public void setResolvedType(Site resolvedType) {
		this.resolvedType.set(resolvedType);
	}

	public String toString() {
		return name == null ? value.toString() : name + ": " + value;
	}
}
