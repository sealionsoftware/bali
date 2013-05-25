package bali.compiler.parser.tree;

import java.lang.Class;
import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class Import extends Node {

	private String name;

	private Class resolvedClass;

	public Import() {
	}

	public Import(Integer line, Integer character) {
		super(line, character);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Class getResolvedClass() {
		return resolvedClass;
	}

	public void setResolvedClass(Class resolvedClass) {
		this.resolvedClass = resolvedClass;
	}

	public List<Node> getChildren() {
		return Collections.emptyList();
	}
}
