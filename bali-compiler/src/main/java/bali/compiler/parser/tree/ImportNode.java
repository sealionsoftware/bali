package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;
import bali.compiler.type.Class;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class ImportNode extends Node {

	private String name;

	private BlockingReference<Class> type = new BlockingReference<>();

	public ImportNode() {
	}

	public ImportNode(Integer line, Integer character) {
		super(line, character);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Class getType() {
		return type.get();
	}

	public void setType(Class aClass) {
		this.type.set(aClass);
	}

}
