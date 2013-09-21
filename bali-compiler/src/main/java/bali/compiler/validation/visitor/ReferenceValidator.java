package bali.compiler.validation.visitor;

import bali._;
import bali.compiler.parser.tree.ArgumentDeclarationNode;
import bali.compiler.parser.tree.CatchStatementNode;
import bali.compiler.parser.tree.ClassNode;
import bali.compiler.parser.tree.CodeBlockNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.DeclarationNode;
import bali.compiler.parser.tree.ForStatementNode;
import bali.compiler.parser.tree.MethodDeclarationNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.ReferenceNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.parser.tree.VariableNode;
import bali.compiler.type.Site;
import bali.compiler.type.Type;
import bali.compiler.type.TypeLibrary;
import bali.compiler.validation.ValidationFailure;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ReferenceValidator implements Validator<CompilationUnitNode> {

	private Scope langScope;

	public ReferenceValidator(TypeLibrary library) {

		// Lang level constants

		Class<_> langClass = _.class;
		List<DeclarationNode> langDeclarations = new ArrayList<>();
		for (Field f : langClass.getDeclaredFields()) {

			Type fieldType = library.getType(f.getType().getName());
			SiteNode siteNode = new SiteNode();
			siteNode.setSite(new Site(
					fieldType,
					new ArrayList<Site>() //TODO
			));

			DeclarationNode d = new ArgumentDeclarationNode();
			d.setName(f.getName());
			d.setType(siteNode);
			langDeclarations.add(d);
		}

		langScope = new Scope(
				ReferenceNode.ReferenceScope.STATIC,
				langClass.getName(),
				langDeclarations
		);
	}

	// Engages at the root of the AST, constructs a lookup table
	public List<ValidationFailure> validate(CompilationUnitNode unit) {

		Deque<Scope> unitLevelScopes = new ArrayDeque<>();
		unitLevelScopes.add(langScope);

		// TODO: Higher Packages?

		// Package Level Constants

		unitLevelScopes.add(new Scope(
				ReferenceNode.ReferenceScope.STATIC,
				unit.getName() + "._",
				unit.getConstants()
		));

		ReferenceValidatorTypeAgent agent = new ReferenceValidatorTypeAgent(unitLevelScopes);
		walkAgentOverChildren(unit, agent);
		return agent.getFailures();
	}

	private void validate(Node node, ReferenceValidatorTypeAgent agent) {
		if (node instanceof ClassNode) {
			validate((ClassNode) node, agent);
		} else if (node instanceof MethodDeclarationNode) {
			validate((MethodDeclarationNode) node, agent);
		} else if (node instanceof ForStatementNode) {
			validate((ForStatementNode) node, agent);
		} else if (node instanceof CatchStatementNode) {
			validate((CatchStatementNode) node, agent);
		} else if (node instanceof CodeBlockNode) {
			validate((CodeBlockNode) node, agent);
		} else if (node instanceof VariableNode) {
			validate((VariableNode) node, agent);
		} else if (node instanceof ReferenceNode) {
			agent.validate((ReferenceNode) node);
		} else {
			walkAgentOverChildren(node, agent);
		}
	}

	private void validate(ClassNode clazz, ReferenceValidatorTypeAgent agent) {
		List<DeclarationNode> referenceableFields = new ArrayList<>();
		referenceableFields.addAll(clazz.getArgumentDeclarations());
		referenceableFields.addAll(clazz.getFields());
		pushAndWalk(clazz, agent, new Scope(
				ReferenceNode.ReferenceScope.FIELD,
				clazz.getQualifiedClassName(),
				referenceableFields
		));
	}

	private void validate(MethodDeclarationNode method, ReferenceValidatorTypeAgent agent) {
		List<DeclarationNode> declarations = new ArrayList<>();
		for (DeclarationNode declaration : method.getArguments()) {
			declarations.add(declaration);
		}
		pushAndWalk(method, agent, new Scope(
				ReferenceNode.ReferenceScope.VARIABLE,
				null,
				declarations
		));
	}

	private void validate(ForStatementNode statement, ReferenceValidatorTypeAgent agent) {
		List<DeclarationNode> declarations = new ArrayList<>();
		declarations.add(statement.getElement());
		pushAndWalk(statement, agent, new Scope(
				ReferenceNode.ReferenceScope.VARIABLE,
				null,
				declarations
		));
	}

	private void validate(CatchStatementNode statement, ReferenceValidatorTypeAgent agent) {
		List<DeclarationNode> declarations = new ArrayList<>();
		declarations.add(statement.getDeclaration());
		pushAndWalk(statement, agent, new Scope(
				ReferenceNode.ReferenceScope.VARIABLE,
				null,
				declarations
		));
	}

	private void validate(CodeBlockNode codeBlock, ReferenceValidatorTypeAgent agent) {
		pushAndWalk(codeBlock, agent, new Scope(
				ReferenceNode.ReferenceScope.VARIABLE,
				null,
				new ArrayList<DeclarationNode>()
		));
	}

	private void validate(VariableNode variable, ReferenceValidatorTypeAgent agent) {
		agent.peek().add(variable.getDeclaration());
		walkAgentOverChildren(variable, agent);
	}

	private void pushAndWalk(Node node, ReferenceValidatorTypeAgent agent, Scope scope) {
		agent.push(scope);
		walkAgentOverChildren(node, agent);
		agent.pop();
	}

	private void walkAgentOverChildren(Node node, ReferenceValidatorTypeAgent agent) {
		for (Node child : node.getChildren()) {
			validate(child, agent);
		}
	}

	public static class ReferenceValidatorTypeAgent {

		private Deque<Scope> scopeStack = new ArrayDeque<>();
		private List<ValidationFailure> failures = new ArrayList<>();

		public ReferenceValidatorTypeAgent(Deque<Scope> scopeStack) {
			this.scopeStack = scopeStack;
		}

		public List<ValidationFailure> getFailures() {
			return failures;
		}

		public void push(Scope scope) {
			scopeStack.push(scope);
		}

		public Scope peek() {
			return scopeStack.peek();
		}

		public void pop() {
			scopeStack.pop();
		}

		public void validate(ReferenceNode value) {

			DeclarationNode declaration = null;
			Scope declarationScope = null;

			for (Scope scope : scopeStack) {
				declaration = scope.find(value.getName());
				if (declaration != null) {
					declarationScope = scope;
					break;
				}
			}

			if (declaration == null) {
				failures.add(new ValidationFailure(value, "Could not resolve reference " + value.getName()));
				return;
			}

			value.setDeclaration(declaration);
			value.setHostClass(declarationScope.getClassName());
			value.setScope(declarationScope.getScope());
		}
	}

	public static class Scope {

		private ReferenceNode.ReferenceScope scope;
		private String className;
		private Map<String, DeclarationNode> declarations;

		public Scope(ReferenceNode.ReferenceScope scope, String className, List<? extends DeclarationNode> declarationsList) {
			this.scope = scope;
			this.className = className;
			this.declarations = new HashMap<>();
			for (DeclarationNode declaration : declarationsList) {
				add(declaration);
			}
		}

		public ReferenceNode.ReferenceScope getScope() {
			return scope;
		}

		public String getClassName() {
			return className;
		}

		public void add(DeclarationNode declaration) {
			declarations.put(declaration.getName(), declaration);
		}

		public DeclarationNode find(String name) {
			return declarations.get(name);
		}
	}

}
