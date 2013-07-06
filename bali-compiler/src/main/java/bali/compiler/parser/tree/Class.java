package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class Class extends TypeDeclaration<Method> {

	private List<Declaration> arguments = new ArrayList<>();
	private List<Field> fields = new ArrayList<>();
	private List<Method> methods = new ArrayList<>();

	private String sourceFile;

	public Class() {
		this(null, null);
	}

	public Class(Integer line, Integer character) {
		super(line, character);
	}

	public List<Declaration> getArguments() {
		return arguments;
	}

	public void addArgument(Declaration argument) {
		this.arguments.add(argument);
	}

	public List<Method> getMethods() {
		return methods;
	}

	public void addMethod(Method method) {
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
		children.addAll(arguments);
		children.addAll(fields);
		children.addAll(methods);
		return children;
	}
}
