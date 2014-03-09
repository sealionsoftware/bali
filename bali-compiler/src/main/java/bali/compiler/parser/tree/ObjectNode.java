package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class ObjectNode extends MethodDeclaringClassNode<MethodDeclarationNode> {

	private final List<ParameterNode> parameters = new ArrayList<>();
	private final List<FieldNode> fields = new ArrayList<>();

	private BlockingReference<String> sourceFile = new BlockingReference<>();

	public ObjectNode() {
		this(null, null);
	}

	public ObjectNode(Integer line, Integer character) {
		super(line, character);
	}

	public List<ParameterNode> getParameters() {
		return parameters;
	}

	public void addParameter(ParameterNode parameter) {
		this.parameters.add(parameter);
		children.add(parameter);
	}

	public List<FieldNode> getFields() {
		return fields;
	}

	public void addField(FieldNode field) {
		this.fields.add(field);
		children.add(field);
	}

	public void setSourceFile(String sourceFile) {
		this.sourceFile.set(sourceFile);
	}

	public String getSourceFile() {
		return sourceFile.get();
	}

}
