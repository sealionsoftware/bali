package bali.compiler.parser.tree;

import bali.compiler.type.Class;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class ClassNode extends TypeNode<MethodDeclarationNode, Class> {

	private List<ArgumentDeclarationNode> argumentDeclarations = new ArrayList<>();
	private List<FieldNode> fields = new ArrayList<>();
	private List<MethodDeclarationNode> methods = new ArrayList<>();

	private String sourceFile;

	public ClassNode() {
		this(null, null);
	}

	public ClassNode(Integer line, Integer character) {
		super(line, character);
	}

	public List<ArgumentDeclarationNode> getArgumentDeclarations() {
		return argumentDeclarations;
	}

	public void addArgument(ArgumentDeclarationNode argumentDeclaration) {
		this.argumentDeclarations.add(argumentDeclaration);
	}

	public List<MethodDeclarationNode> getMethods() {
		return methods;
	}

	public void addMethod(MethodDeclarationNode method) {
		this.methods.add(method);
	}

	public List<FieldNode> getFields() {
		return fields;
	}

	public void addField(FieldNode field) {
		this.fields.add(field);
	}

	public Boolean getAbstract() {
		return false;
	}

	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

	public String getSourceFile() {
		return sourceFile;
	}

	public List<Node> getChildren() {
		List<Node> children = super.getChildren();
		children.addAll(argumentDeclarations);
		children.addAll(fields);
		children.addAll(methods);
		return children;
	}
}
