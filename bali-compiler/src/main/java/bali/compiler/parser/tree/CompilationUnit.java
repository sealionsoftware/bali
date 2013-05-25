package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class CompilationUnit extends Node {

	private String name;
	private List<Import> imports = new ArrayList<>();
	private List<Constant> constants = new ArrayList<>();
	private List<Interface> interfaces = new ArrayList<>();
	private List<Class> classes = new ArrayList<>();

	public CompilationUnit(Integer line, Integer character) {
		super(line, character);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addImport(Import iport){
		imports.add(iport);
	}

	public void addConstant(Constant constant){
		constants.add(constant);
	}

	public void addInterface(Interface iface){
		interfaces.add(iface);
	}

	public void addClass(Class clazz){
		classes.add(clazz);
	}

	public String getName() {
		return name;
	}

	public List<Import> getImports() {
		return imports;
	}

	public List<Constant> getConstants() {
		return constants;
	}

	public List<Interface> getInterfaces() {
		return interfaces;
	}

	public List<Class> getClasses() {
		return classes;
	}

	public List<Node> getChildren() {
		List<Node> nodes = new ArrayList<>();
		nodes.addAll(imports);
		nodes.addAll(constants);
		nodes.addAll(interfaces);
		nodes.addAll(classes);
		return nodes;
	}
}
