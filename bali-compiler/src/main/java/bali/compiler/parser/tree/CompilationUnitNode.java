package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class CompilationUnitNode extends Node {

	private String name;
	private List<ImportNode> imports = new ArrayList<>();
	private List<ConstantNode> constants = new ArrayList<>();
	private List<InterfaceNode> interfaces = new ArrayList<>();
	private List<ClassNode> classes = new ArrayList<>();

	public CompilationUnitNode(Integer line, Integer character) {
		super(line, character);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addImport(ImportNode iport){
		imports.add(iport);
	}

	public void addConstant(ConstantNode constant){
		constants.add(constant);
	}

	public void addInterface(InterfaceNode iface){
		interfaces.add(iface);
	}

	public void addClass(ClassNode clazz){
		classes.add(clazz);
	}

	public String getName() {
		return name;
	}

	public List<ImportNode> getImports() {
		return imports;
	}

	public List<ConstantNode> getConstants() {
		return constants;
	}

	public List<InterfaceNode> getInterfaces() {
		return interfaces;
	}

	public List<ClassNode> getClasses() {
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
