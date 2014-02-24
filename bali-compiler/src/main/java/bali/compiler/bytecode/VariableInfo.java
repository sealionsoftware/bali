package bali.compiler.bytecode;

import bali.compiler.type.Site;
import org.objectweb.asm.Label;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class VariableInfo {

	private String name;
	private Site type;
	private Label start;
	private Label end;
	private Integer index;

	public VariableInfo(String name, Site type, Label start, Label end, Integer index) {
		this.name = name;
		this.type = type;
		this.start = start;
		this.end = end;
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public Site getType() {
		return type;
	}

	public Label getStart() {
		return start;
	}

	public Label getEnd() {
		return end;
	}

	public Integer getIndex() {
		return index;
	}
}
