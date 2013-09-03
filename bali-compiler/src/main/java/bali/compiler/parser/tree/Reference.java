package bali.compiler.parser.tree;

import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class Reference extends Expression {

	private String name;

	private Declaration declaration;
	private ReferenceScope scope;
	private String hostClass;

	public Reference() {
	}

	public Reference(Integer line, Integer character) {
		super(line, character);
	}

	public TypeReference getType() {
		return declaration.getType();
	}

	public void setDeclaration(Declaration declaration) {
		this.declaration = declaration;
	}

	public Declaration getDeclaration() {
		return declaration;
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
