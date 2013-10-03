package bali.compiler.parser.tree;

import bali.compiler.type.Site;

import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class ReferenceNode extends ExpressionNode {

	private String name;

	private Site declaration;
	private ReferenceScope scope;
	private String hostClass;
	private Boolean isFinal = false;

	public ReferenceNode() {
	}

	public ReferenceNode(Integer line, Integer character) {
		super(line, character);
	}

	public Site getType() {
		return declaration;
	}

	public void setDeclaration(Site declaration) {
		this.declaration = declaration;
	}

	public Site getDeclaration() {
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

	public Boolean getFinal() {
		return isFinal;
	}

	public void setFinal(Boolean aFinal) {
		isFinal = aFinal;
	}

	public enum ReferenceScope {
		STATIC, FIELD, VARIABLE
	}

	public String toString() {
		return declaration + " " + name;
	}
}
