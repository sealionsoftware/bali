package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;
import bali.compiler.type.Declaration;
import bali.compiler.type.Site;

import java.util.List;
import java.util.UUID;

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

	public void setArguments(List<RunArgument> arguments) {
		this.arguments.set(arguments);
	}

	public static class RunArgument extends Declaration<Site> {

		private ReferenceNode.ReferenceScope scope;
		private String hostClassName;
		private UUID id;

		public RunArgument(String name, Site type, ReferenceNode.ReferenceScope scope, String hostClassName, UUID id) {
			super(name, type);
			this.scope = scope;
			this.hostClassName = hostClassName;
			this.id = id;
		}

		public ReferenceNode.ReferenceScope getScope() {
			return scope;
		}

		public String getHostClassName() {
			return hostClassName;
		}

		public UUID getId() {
			return id;
		}
	}
}
