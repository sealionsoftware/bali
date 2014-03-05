package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;
import bali.compiler.type.Method;
import bali.compiler.type.Site;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class InvocationNode extends ParametrisedExpressionNode {

	private ExpressionNode target;
	private String methodName;

	private BlockingReference<Site> resolvedType = new BlockingReference<>();
	private BlockingReference<Site> targetType = new BlockingReference<>();

	public InvocationNode() {
	}

	public InvocationNode(Integer line, Integer character) {
		super(line, character);
	}

	public Site getType() {
		return resolvedType.get();
	}

	public void setResolvedType(Site resolvedType) {
		this.resolvedType.set(resolvedType);
	}

	public Site getTargetType(){
		return targetType.get();
	}

	public void setTargetType(Site targetType) {
		this.targetType.set(targetType);
	}

	public void setTarget(ExpressionNode target) {
		children.add(target);
		this.target = target;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public ExpressionNode getTarget() {
		return target;
	}

	public String getMethodName() {
		return methodName;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (target != null){
			sb.append(target).append(".");
		}
		sb.append(methodName);
		sb.append("(");
		for (ArgumentNode argument : getArguments()){
			sb.append(argument);
		}
		sb.append(")");
		return sb.toString();
	}
}
