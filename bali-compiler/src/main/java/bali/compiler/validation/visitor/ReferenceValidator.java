package bali.compiler.validation.visitor;

import bali._;
import bali.compiler.parser.tree.CodeBlock;
import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.Reference;
import bali.compiler.parser.tree.Type;
import bali.compiler.parser.tree.Variable;
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
public class ReferenceValidator implements Validator<CompilationUnit> {

	// Engages at the root of the AST, constructs a lookup table
	public List<ValidationFailure> validate(CompilationUnit unit) {

		Deque<Scope> unitLevelScopes = new ArrayDeque<>();

		// Lang level constants

		Class<_> langClass = _.class;
		List<Declaration> langDeclarations = new ArrayList<>();
		for (Field f : langClass.getDeclaredFields()) {
			Type type = new Type();
			type.setQualifiedClassName(f.getType().getName());
			Declaration d = new Declaration();
			d.setName(f.getName());
			d.setType(type);
			langDeclarations.add(d);
		}
		unitLevelScopes.add(new Scope(
				Reference.ReferenceScope.STATIC,
				langClass.getName(),
				langDeclarations
		));

		// TODO: Higher Packages?

		// Package Level Constants

		unitLevelScopes.add(new Scope(
				Reference.ReferenceScope.STATIC,
				unit.getName() + "._",
				unit.getConstants()
		));

		ReferenceValidatorTypeAgent agent = new ReferenceValidatorTypeAgent(unitLevelScopes);
		walkAgentOverChildren(unit, agent);
		return agent.getFailures();
	}

	private void validate(Node node, ReferenceValidatorTypeAgent agent) {
		if (node instanceof bali.compiler.parser.tree.Class) {
			validate((bali.compiler.parser.tree.Class) node, agent);
		} else if (node instanceof CodeBlock) {
			validate((CodeBlock) node, agent);
		} else if (node instanceof Variable) {
			validate((Variable) node, agent);
		} else if (node instanceof Reference) {
			agent.validate((Reference) node);
		} else {
			walkAgentOverChildren(node, agent);
		}
	}

	private void validate(bali.compiler.parser.tree.Class clazz, ReferenceValidatorTypeAgent agent) {
		pushAndWalk(clazz, agent, new Scope(
				Reference.ReferenceScope.FIELD,
				clazz.getQualifiedClassName(),
				clazz.getFields()
		));
	}

	private void validate(CodeBlock codeBlock, ReferenceValidatorTypeAgent agent) {
		pushAndWalk(codeBlock, agent, new Scope(
				Reference.ReferenceScope.VARIABLE,
				null,
				new ArrayList<Declaration>()
		));
	}

	private void validate(Variable variable, ReferenceValidatorTypeAgent agent) {
		Declaration declaration = new Declaration();
		declaration.setName(variable.getReference().getName());
		declaration.setType(variable.getType());
		agent.peek().add(declaration);
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

		public void validate(Reference value) {

			Declaration declaration = null;
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

			value.setType(declaration.getType());
			value.setHostClass(declarationScope.getClassName());
			value.setScope(declarationScope.getScope());
		}
	}

	public static class Scope {

		private Reference.ReferenceScope scope;
		private String className;
		private Map<String, Declaration> declarations;

		public Scope(Reference.ReferenceScope scope, String className, List<? extends Declaration> declarationsList) {
			this.scope = scope;
			this.className = className;
			this.declarations = new HashMap<>();
			for (Declaration declaration : declarationsList) {
				add(declaration);
			}
		}

		public Reference.ReferenceScope getScope() {
			return scope;
		}

		public String getClassName() {
			return className;
		}

		public void add(Declaration declaration) {
			declarations.put(declaration.getName(), declaration);
		}

		public Declaration find(String name) {
			return declarations.get(name);
		}
	}

}
