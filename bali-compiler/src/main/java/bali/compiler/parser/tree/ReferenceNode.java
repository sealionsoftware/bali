package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;
import bali.compiler.type.Site;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class ReferenceNode extends ExpressionNode {

	private String name;
	private ExpressionNode target;

	private BlockingReference<String> hostClass = new BlockingReference<>();
	private BlockingReference<ReferenceScope> scope = new BlockingReference<>();
	private BlockingReference<Site> declaration = new BlockingReference<>();
	private BlockingReference<Boolean> isFinal = new BlockingReference<>();

	private BlockingReference<String> getterName = new BlockingReference<>();

	public ReferenceNode() {
	}

	public ReferenceNode(Integer line, Integer character) {
		super(line, character);
	}

	public Site getType() {
		return declaration.get();
	}

	public void setDeclaration(Site declaration) {
		this.declaration.set(declaration);
	}

	public Site getDeclaration() {
		return declaration.get();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public ReferenceScope getScope() {
		return scope.get();
	}

	public void setScope(ReferenceScope scope) {
		this.scope.set(scope);
	}

	public String getHostClass() {
		return hostClass.get();
	}

	public void setHostClass(String hostClass) {
		this.hostClass.set(hostClass);
	}

	public Boolean getFinal() {
		return isFinal.get();
	}

	public void setFinal(Boolean aFinal) {
		isFinal.set(aFinal);
	}

	public ExpressionNode getTarget() {
		return target;
	}

	public void setTarget(ExpressionNode target) {
		children.add(target);
		this.target = target;
	}

	public String getGetterName() {
		return getterName.get();
	}

	public void setGetterName(String getterName) {
		this.getterName.set(getterName);
	}

	public enum ReferenceScope {
		STATIC, FIELD, VARIABLE
	}

	public String toString() {
		return declaration.get() + " " + name;
	}
}
