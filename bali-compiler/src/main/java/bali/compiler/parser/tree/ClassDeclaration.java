package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class ClassDeclaration extends TypeDeclaration<MethodDeclaration> {

	private List<ArgumentDeclaration> argumentDeclarations = new ArrayList<>();
	private List<Field> fields = new ArrayList<>();
	private List<MethodDeclaration> methods = new ArrayList<>();

	private String sourceFile;

	public ClassDeclaration() {
		this(null, null);
	}

	public ClassDeclaration(Integer line, Integer character) {
		super(line, character);
	}

	public List<ArgumentDeclaration> getArgumentDeclarations() {
		return argumentDeclarations;
	}

	public void addArgument(ArgumentDeclaration argumentDeclaration) {
		this.argumentDeclarations.add(argumentDeclaration);
	}

	public List<MethodDeclaration> getMethods() {
		return methods;
	}

	public void addMethod(MethodDeclaration method) {
		this.methods.add(method);
	}

	public List<Field> getFields() {
		return fields;
	}

	public void addField(Field field) {
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
