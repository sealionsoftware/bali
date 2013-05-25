package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class Class extends Type {

	private List<Declaration> arguments = new ArrayList<>();
	private List<Type> interfaces = new ArrayList<>();
	private List<Field> fields = new ArrayList<>();
	private List<Method> methods = new ArrayList<>();

	public Class() {}

	public Class(Integer line, Integer character) {
		super(line, character);
	}

	public List<Declaration> getArguments() {
		return arguments;
	}

	public void addArgument(Declaration argument){
		this.arguments.add(argument);
	}

	public List<Method> getMethods() {
		return methods;
	}

	public void addMethod(Method method){
		this.methods.add(method);
	}

	public List<Type> getInterfaces() {
		return interfaces;
	}

	public void addInterface(Type iface){
		this.interfaces.add(iface);
	}

	public List<Field> getFields() {
		return fields;
	}

	public void addField(Field field){
		this.fields.add(field);
	}

	public List<Node> getChildren() {
		List<Node> children = super.getChildren();
		children.addAll(arguments);
		children.addAll(interfaces);
		children.addAll(fields);
		children.addAll(methods);
		return children;
	}
}
