package bali.compiler.validation.visitor;


import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Import;
import bali.compiler.parser.tree.Interface;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.Type;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ClassNameValidator implements Validator<CompilationUnit> {

	// Engages at the root of the AST, constructs a lookup table
	public List<ValidationFailure> validate(CompilationUnit unit) {

		Map<String, String> resolvables = new HashMap<>();
		for (Interface iface: unit.getInterfaces()){
			String ifaceName = iface.getClassName();
			resolvables.put(ifaceName, unit.getName() + "." + ifaceName);
		}
		for (bali.compiler.parser.tree.Class clazz: unit.getClasses()){
			String clazzName = clazz.getClassName();
			resolvables.put(clazzName, unit.getName() + "." + clazzName);
		}
		for (Import iport: unit.getImports()){
			String importName = iport.getName();
			resolvables.put(importName.substring(importName.lastIndexOf('.') + 1), iport.getName());
		}

		ClassNameValidatorTypeAgent agent = new ClassNameValidatorTypeAgent(resolvables);
		walkAgentOverChildren(unit, agent);
		return agent.getFailures();
	}

	private void walkAgentOverChildren(Node node, ClassNameValidatorTypeAgent agent){
		if (node instanceof Type){
			agent.validate((Type) node);
		}
		for (Node child : node.getChildren()){
			walkAgentOverChildren(child, agent);
		}
	}

	public static class ClassNameValidatorTypeAgent {

		private Map<String, String> resolvables;
		private List<ValidationFailure> failures = new ArrayList<>();

		public ClassNameValidatorTypeAgent(Map<String, String> resolvables) {
			this.resolvables = resolvables;
		}

		public List<ValidationFailure> getFailures() {
			return failures;
		}

		public void validate(Type type) {
			if (type.getClassName().contains(".")){
				type.setQualifiedClassName(type.getClassName());
			} else {
				String fqName = resolvables.get(type.getClassName());
				if (fqName == null){
					failures.add(new ValidationFailure(type, "Cannot resolve a qualified classname for type " + type.getClassName()));
					return;
				}
				type.setQualifiedClassName(fqName);
			}
		}
	}

}
