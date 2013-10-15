package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class ClassNode extends TypeNode<MethodDeclarationNode> {

	private final List<ArgumentDeclarationNode> argumentDeclarations = new ArrayList<>();
	private final List<FieldNode> fields = new ArrayList<>();
	private final List<MethodDeclarationNode> methods = new ArrayList<>();

	private BlockingReference<String> sourceFile = new BlockingReference<>();

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
		children.add(argumentDeclaration);
	}

	public List<MethodDeclarationNode> getMethods() {
		return methods;
	}

	public void addMethod(MethodDeclarationNode method) {
		this.methods.add(method);
		children.add(method);
	}

	public List<FieldNode> getFields() {
		return fields;
	}

	public void addField(FieldNode field) {
		this.fields.add(field);
		children.add(field);
	}

	public Boolean getAbstract() {
		return false;
	}

	public void setSourceFile(String sourceFile) {
		this.sourceFile.set(sourceFile);
	}

	public String getSourceFile() {
		return sourceFile.get();
	}
}
