package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.ClassNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.ConstructionExpressionNode;
import bali.compiler.parser.tree.ImportNode;
import bali.compiler.parser.tree.InterfaceNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.type.ParametrizedSite;
import bali.compiler.type.Reference;
import bali.compiler.type.Site;
import bali.compiler.type.Type;
import bali.compiler.type.TypeLibrary;
import bali.compiler.type.VanillaSite;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Validates the Site declarations of constants, fields, variables, arguments etc
 * <p/>
 * Requires that the imports have been resolved
 * Sets the Site object on each typed site
 * <p/>
 * User: Richard
 * Date: 14/05/13
 */
public class TypeResolvingValidator implements Validator<CompilationUnitNode> {

	private TypeLibrary library;

	public TypeResolvingValidator(TypeLibrary library) {
		this.library = library;
	}

	// Engages at the root of the AST, constructs a lookup table of unqualified names to declarations
	public List<ValidationFailure> validate(CompilationUnitNode unit) {

		List<ValidationFailure> ret = new ArrayList<>();

		Map<String, Reference<Type>> resolvables = new HashMap<>();
		for (InterfaceNode iface : unit.getInterfaces()) {
			resolvables.put(iface.getClassName(), library.getReference(iface.getQualifiedClassName()));
		}
		for (ClassNode clazz : unit.getClasses()) {
			resolvables.put(clazz.getClassName(), library.getReference(clazz.getQualifiedClassName()));
		}
		for (ImportNode iport : unit.getImports()) {
			String name = iport.getName();
			resolvables.put(name.substring(name.lastIndexOf(".") + 1), new Reference<>(iport.getType()));
		}

		Agent agent = new Agent(resolvables, library);
		ConstructorAgent constructorAgent = new ConstructorAgent(resolvables, library);
		ret.addAll(walkAgentOverChildren(unit, agent, constructorAgent));
		return ret;
	}

	private List<ValidationFailure> walkAgentOverChildren(Node node, Agent agent, ConstructorAgent constructorAgent) {
		List<ValidationFailure> ret = new ArrayList<>();
		for (Node child : node.getChildren()) {
			ret.addAll(walkAgentOverChildren(child, agent, constructorAgent));
		}
		if (node instanceof SiteNode) {
			ret.addAll(agent.validate((SiteNode) node));
		}
		if (node instanceof ConstructionExpressionNode){
			ret.addAll(constructorAgent.validate((ConstructionExpressionNode) node));
		}
		return ret;
	}

	private static class Agent implements Validator<SiteNode> {

		private Map<String, Reference<Type>> resolvables;
		private TypeLibrary library;

		private Agent(Map<String, Reference<Type>> resolvables, TypeLibrary library) {
			this.resolvables = resolvables;
			this.library = library;
		}

		public List<ValidationFailure> validate(SiteNode type) {

			List<ValidationFailure> ret = new ArrayList<>();
			Reference<Type> reference = resolvables.get(type.getClassName());

			if (reference == null) {
				try {
					reference = library.getReference(type.getClassName());
				} catch (Exception e) {
					ret.add(new ValidationFailure(type, "Cannot resolve type " + type));
					return ret;
				}
			}

			List<SiteNode> parameterNodes = type.getParameters();
			if (parameterNodes.isEmpty()){
				type.setSite(new VanillaSite(reference));
			}

			List<Site> parameterSites = new ArrayList<>(parameterNodes.size());
			for (SiteNode parameterNode : parameterNodes) {
				parameterSites.add(parameterNode.getSite());
			}

			type.setSite(new ParametrizedSite(reference, parameterSites));
			return ret;
		}
	}

	private static class ConstructorAgent implements Validator<ConstructionExpressionNode> {

		private Map<String, Reference<Type>> resolvables;
		private TypeLibrary library;

		private ConstructorAgent(Map<String, Reference<Type>> resolvables, TypeLibrary library) {
			this.resolvables = resolvables;
			this.library = library;
		}

		public List<ValidationFailure> validate(ConstructionExpressionNode type) {

			List<ValidationFailure> ret = new ArrayList<>();
			Reference<Type> reference = resolvables.get(type.getClassName());

			if (reference == null) {
				try {
					reference = library.getReference(type.getClassName());
				} catch (Exception e) {
					ret.add(new ValidationFailure(type, "Cannot resolve type " + type));
					return ret;
				}
			}

			type.setType(new VanillaSite(reference));
			return ret;
		}
	}

}
