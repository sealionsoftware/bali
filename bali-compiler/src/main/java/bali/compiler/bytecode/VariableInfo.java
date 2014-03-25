package bali.compiler.bytecode;

import bali.compiler.type.Site;
import org.objectweb.asm.Label;

import java.util.UUID;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class VariableInfo {

	private String name;
	private Site type;
	private Label start;
	private Label end;
	private UUID id;

	public VariableInfo(String name, Site type, Label start, Label end, UUID id) {
		this.name = name;
		this.type = type;
		this.start = start;
		this.end = end;
		this.id = id;
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

	public UUID getId() {
		return id;
	}
}
