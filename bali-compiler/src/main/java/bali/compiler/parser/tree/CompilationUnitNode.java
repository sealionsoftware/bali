package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;
import bali.compiler.reference.Reference;
import bali.compiler.type.Class;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class CompilationUnitNode extends Node {

	private String name;
	private List<ImportNode> imports = new ArrayList<>();
	private List<ConstantNode> constants = new ArrayList<>();
	private List<BeanNode> beans = new ArrayList<>();
	private List<InterfaceNode> interfaces = new ArrayList<>();
	private List<ObjectNode> classes = new ArrayList<>();

	private BlockingReference<Map<String, Reference<Class>>> resolvables = new BlockingReference<>();
	private BlockingReference<List<RunStatementNode>> runStatements = new BlockingReference<>();

	public CompilationUnitNode(Integer line, Integer character) {
		super(line, character);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addImport(ImportNode iport){
		children.add(iport);
		imports.add(iport);
	}

	public void addConstant(ConstantNode constant){
		children.add(constant);
		constants.add(constant);
	}

	public void addBean(BeanNode bean){
		children.add(bean);
		beans.add(bean);
	}

	public void addInterface(InterfaceNode iface){
		children.add(iface);
		interfaces.add(iface);
	}

	public void addClass(ObjectNode clazz){
		children.add(clazz);
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

	public List<BeanNode> getBeans() {
		return beans;
	}

	public List<ObjectNode> getClasses() {
		return classes;
	}

	public Map<String, Reference<Class>> getResolvables() {
		return resolvables.get();
	}

	public void setResolvables(Map<String, Reference<Class>> resolvables) {
		this.resolvables.set(resolvables);
	}

	public List<RunStatementNode> getRunStatements() {
		return runStatements.get();
	}

	public void setRunStatements(List<RunStatementNode> runStatements) {
		this.runStatements.set(runStatements);
	}
}
