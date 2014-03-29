package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;
import bali.compiler.type.Site;

import java.lang.Integer;
import java.util.UUID;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class ReferenceNode extends ExpressionNode {

	private String name;
	private ExpressionNode target;

	private BlockingReference<String> hostClass = new BlockingReference<>();
	private BlockingReference<ReferenceScope> scope = new BlockingReference<>();
	private BlockingReference<Site> type = new BlockingReference<>();
	private BlockingReference<Boolean> isFinal = new BlockingReference<>();
	private BlockingReference<UUID> id = new BlockingReference<>();

	public ReferenceNode() {
	}

	public ReferenceNode(Integer line, Integer character) {
		super(line, character);
	}

	public Site getType() {
		return type.get();
	}

	public void setType(Site type) {
		this.type.set(type);
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

	public UUID getId() {
		return id.get();
	}

	public void setId(UUID id) {
		this.id.set(id);
	}

	public ExpressionNode getTarget() {
		return target;
	}

	public void setTarget(ExpressionNode target) {
		children.add(target);
		this.target = target;
	}

	public enum ReferenceScope {
		STATIC, FIELD, VARIABLE
	}

	public String toString() {
		return name;
	}
}
