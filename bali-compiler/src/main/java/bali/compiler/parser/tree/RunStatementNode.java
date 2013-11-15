package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;
import bali.compiler.type.Declaration;
import bali.compiler.type.Site;

import java.util.List;

/**
 * User: Richard
 * Date: 07/11/13
 */
public class RunStatementNode extends ControlExpressionNode {

	private ControlExpressionNode body;

	private BlockingReference<String> runnableClassName = new BlockingReference<>();
	private BlockingReference<String> sourceFileName = new BlockingReference<>();
	private BlockingReference<List<RunArgument>> arguments = new BlockingReference<>();

	public RunStatementNode(Integer line, Integer character) {
		super(line, character);
	}

	public ControlExpressionNode getBody() {
		return body;
	}

	public void setBody(ControlExpressionNode body) {
		children.add(body);
		this.body = body;
	}

	public String getRunnableClassName() {
		return runnableClassName.get();
	}

	public void setRunnableClassName(String runnableClassName) {
		this.runnableClassName.set(runnableClassName);
	}

	public String getSourceFileName() {
		return sourceFileName.get();
	}

	public void setSourceFileName(String sourceFileName) {
		this.sourceFileName.set(sourceFileName);
	}

	public List<RunArgument> getArguments() {
		return arguments.get();
	}

	public void setArguments(List<RunArgument> argumnets) {
		this.arguments.set(argumnets);
	}

	public static class RunArgument extends Declaration {

		private ReferenceNode.ReferenceScope scope;
		private String hostClassName;

		public RunArgument(String name, Site type, ReferenceNode.ReferenceScope scope, String hostClassName) {
			super(name, type);
			this.scope = scope;
			this.hostClassName = hostClassName;
		}

		public ReferenceNode.ReferenceScope getScope() {
			return scope;
		}

		public String getHostClassName() {
			return hostClassName;
		}
	}
}
