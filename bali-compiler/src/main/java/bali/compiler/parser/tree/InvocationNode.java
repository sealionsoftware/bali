package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;
import bali.compiler.type.Method;
import bali.compiler.type.Site;

import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class InvocationNode extends ParametrisedExpressionNode {

	private ExpressionNode target;
	private String methodName;

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

	public void setResolvedMethod(Method resolvedMethod) {
		this.resolvedMethod.set(resolvedMethod);
	}

	public Method getResolvedMethod() {
		return this.resolvedMethod.get();
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
		List<ArgumentNode> argumentNodes = getArguments();
		sb.append("(");
		Iterator<ArgumentNode> i = argumentNodes.iterator();
		if (i.hasNext()){
			sb.append(i.next());
		}
		while (i.hasNext()){
			sb.append(", ").append(i.next());
		}
		sb.append(")");
		return sb.toString();
	}
}
