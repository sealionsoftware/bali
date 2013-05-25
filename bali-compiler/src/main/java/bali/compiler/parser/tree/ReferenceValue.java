package bali.compiler.parser.tree;

import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class ReferenceValue extends Value {

	private String name;

	private Type type;
	private ReferenceScope scope;
	private String hostClass;

	public ReferenceValue(Integer line, Integer character) {
		super(line, character);
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public ReferenceScope getScope() {
		return scope;
	}

	public void setScope(ReferenceScope scope) {
		this.scope = scope;
	}

	public String getHostClass() {
		return hostClass;
	}

	public void setHostClass(String hostClass) {
		this.hostClass = hostClass;
	}

	public List<Node> getChildren() {
		return Collections.emptyList();
	}

	public enum ReferenceScope {
		STATIC, FIELD, VARIABLE
	}
}
