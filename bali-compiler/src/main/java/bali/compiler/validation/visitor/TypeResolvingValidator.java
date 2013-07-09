package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Import;
import bali.compiler.parser.tree.Interface;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.Type;
import bali.compiler.parser.tree.TypeDeclaration;
import bali.compiler.validation.TypeDeclarationLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Validates the Type declarations of constants, fields, variables, arguments etc
 * <p/>
 * Requires that the imports have been resolved
 * Sets the full type declaration object on each typed site
 * <p/>
 * User: Richard
 * Date: 14/05/13
 */
public class TypeResolvingValidator implements Validator<CompilationUnit> {

	private TypeDeclarationLibrary library;

	public TypeResolvingValidator(TypeDeclarationLibrary library) {
		this.library = library;
	}

	// Engages at the root of the AST, constructs a lookup table of unqualified names to declarations
	public List<ValidationFailure> validate(CompilationUnit unit) {

		List<ValidationFailure> ret = new ArrayList<>();

		Map<String, TypeDeclaration> resolvables = new HashMap<>();
		for (Interface iface : unit.getInterfaces()) {
			resolvables.put(iface.getClassName(), iface);
		}
		for (bali.compiler.parser.tree.Class clazz : unit.getClasses()) {
			resolvables.put(clazz.getClassName(), clazz);
		}
		for (Import iport : unit.getImports()) {
			String name = iport.getName();
			resolvables.put(name.substring(name.lastIndexOf(".") + 1), iport.getDeclaration());
		}

		Agent agent = new Agent(resolvables, library);
		ret.addAll(walkAgentOverChildren(unit, agent));
		return ret;
	}

	private List<ValidationFailure> walkAgentOverChildren(Node node, Agent agent) {
		List<ValidationFailure> ret = new ArrayList<>();
		if (node instanceof Type) {
			ret.addAll(agent.validate((Type) node));
		}
		for (Node child : node.getChildren()) {
			ret.addAll(walkAgentOverChildren(child, agent));
		}
		return ret;
	}

	private static class Agent implements Validator<Type> {

		private Map<String, TypeDeclaration> resolvables;
		private TypeDeclarationLibrary library;

		private Agent(Map<String, TypeDeclaration> resolvables, TypeDeclarationLibrary library) {
			this.resolvables = resolvables;
			this.library = library;
		}

		public List<ValidationFailure> validate(Type type) {

			List<ValidationFailure> ret = new ArrayList<>();
			TypeDeclaration declaration = resolvables.get(type.getClassName());

			if (declaration == null) {
				try {
					declaration = library.getTypeDeclaration(type.getClassName());
				} catch (ClassNotFoundException e) {
				}
			}

			if (declaration == null) {
				ret.add(new ValidationFailure(type, "Cannot resolve type " + type));
			}
			type.setDeclaration(declaration);
			return ret;
		}
	}


}
