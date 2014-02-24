package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;
import bali.compiler.type.Method;
import bali.compiler.type.Site;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class InvocationNode extends ExpressionNode {

	private ExpressionNode target;
	private String methodName;
	private List<ExpressionNode> arguments = new ArrayList<>();

	private BlockingReference<Method> resolvedMethod = new BlockingReference<>();
	private BlockingReference<Site> targetType = new BlockingReference<>();

	public InvocationNode() {
	}

	public InvocationNode(Integer line, Integer character) {
		super(line, character);
	}

	public Site getType() {
		return resolvedMethod.get().getType();
	}

	public Method getResolvedMethod(){
		return resolvedMethod.get();
	}

	public void setResolvedMethod(Method resolvedMethod) {
		this.resolvedMethod.set(resolvedMethod);
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

	public void addArgument(ExpressionNode argument) {
		children.add(argument);
		arguments.add(argument);
	}

	public ExpressionNode getTarget() {
		return target;
	}

	public String getMethodName() {
		return methodName;
	}

	public List<ExpressionNode> getArguments() {
		return arguments;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (target != null){
			sb.append(target).append(".");
		}
		sb.append(methodName);
		sb.append("(");
		for (ExpressionNode argument : arguments){
			sb.append(argument);
		}
		sb.append(")");
		return sb.toString();
	}
}
