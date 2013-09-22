package bali.compiler.parser.tree;

import bali.compiler.type.ParametrizedSite;

import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class ReferenceNode extends ExpressionNode {

	private String name;

	private DeclarationNode declaration;
	private ReferenceScope scope;
	private String hostClass;

	public ReferenceNode() {
	}

	public ReferenceNode(Integer line, Integer character) {
		super(line, character);
	}

	public ParametrizedSite getType() {
		return declaration.getType().getSite();
	}

	public void setDeclaration(DeclarationNode declaration) {
		this.declaration = declaration;
	}

	public DeclarationNode getDeclaration() {
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
