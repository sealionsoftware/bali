package bali.compiler.parser.tree;

import bali.compiler.type.Declaration;
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

	private Method resolvedMethod;

	public InvocationNode() {
	}

	public InvocationNode(Integer line, Integer character) {
		super(line, character);
	}

	public Site getType() {
		return resolvedMethod.getType();
	}

	public Method getResolvedMethod(){
		return resolvedMethod;
	}

	public void setResolvedMethod(Method resolvedMethod) {
		this.resolvedMethod = resolvedMethod;
	}

	public void setTarget(ExpressionNode target) {
		this.target = target;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void addArgument(ExpressionNode argument) {
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

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		if (target != null) {
			children.add(target);
		}
		children.addAll(arguments);
		return children;
	}
}
