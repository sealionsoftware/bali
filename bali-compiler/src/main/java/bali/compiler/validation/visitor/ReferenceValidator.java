package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.CatchStatementNode;
import bali.compiler.parser.tree.ClassNode;
import bali.compiler.parser.tree.CodeBlockNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.DeclarationNode;
import bali.compiler.parser.tree.ForStatementNode;
import bali.compiler.parser.tree.MethodDeclarationNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.ReferenceNode;
import bali.compiler.parser.tree.VariableNode;
import bali.compiler.type.Declaration;
import bali.compiler.type.Site;
import bali.compiler.type.TypeLibrary;
import bali.compiler.validation.ValidationFailure;

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

	private TypeLibrary library;

	public ReferenceValidator(TypeLibrary library) {
		this.library = library;
	}

	// Engages at the root of the AST, constructs a lookup table
	public List<ValidationFailure> validate(CompilationUnitNode unit) {

		Deque<Scope> unitLevelScopes = new ArrayDeque<>();
		String pkgName = "_";
		List<Declaration> packageConstants = library.getConstants(pkgName);
		Scope scope = new Scope(
				ReferenceNode.ReferenceScope.STATIC,
				pkgName,
				true
		);
		for (Declaration declaration : packageConstants){
			scope.add(declaration.getName(), declaration.getType());
		}
		unitLevelScopes.push(scope);

		String compilationUnitName = unit.getName();
		int i = -1;
		do {
			i = compilationUnitName.indexOf('.', i);
			pkgName = i > 0 ? compilationUnitName.substring(0, i) : compilationUnitName;
			packageConstants = library.getConstants(pkgName);
			scope = new Scope(
					ReferenceNode.ReferenceScope.STATIC,
					pkgName + "._",
					true
			);
			for (Declaration declaration : packageConstants){
				scope.add(declaration.getName(), declaration.getType());
			}
			unitLevelScopes.push(scope);
		} while (i > 0);

		// Package Level Constants

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

		Scope scope = new  Scope(
				ReferenceNode.ReferenceScope.FIELD,
				clazz.getQualifiedClassName(),
				false
		);
		for (DeclarationNode field : referenceableFields){
			scope.add(field.getName(), field.getType().getSite());
		}
		pushAndWalk(clazz, agent, scope);
	}

	private void validate(MethodDeclarationNode method, ReferenceValidatorTypeAgent agent) {
		Scope scope = new Scope(
				ReferenceNode.ReferenceScope.VARIABLE,
				null,
				true
		);
		for (DeclarationNode declaration : method.getArguments()) {
			scope.add(declaration.getName(), declaration.getType().getSite());
		}
		pushAndWalk(method, agent, scope);
	}

	private void validate(ForStatementNode statement, ReferenceValidatorTypeAgent agent) {
		Scope scope = new Scope(
				ReferenceNode.ReferenceScope.VARIABLE,
				null,
				true
		);
		DeclarationNode element = statement.getElement();
		scope.add(element.getName(), element.getType().getSite());
		pushAndWalk(statement, agent, scope);
	}

	private void validate(CatchStatementNode statement, ReferenceValidatorTypeAgent agent) {
		Scope scope = new Scope(
				ReferenceNode.ReferenceScope.VARIABLE,
				null,
				true
		);
		DeclarationNode element = statement.getDeclaration();
		scope.add(element.getName(), element.getType().getSite());
		pushAndWalk(statement, agent, scope);
	}

	private void validate(CodeBlockNode codeBlock, ReferenceValidatorTypeAgent agent) {
		Scope scope = new Scope(
				ReferenceNode.ReferenceScope.VARIABLE,
				null,
				false
		);
		pushAndWalk(codeBlock, agent, scope);
	}

	private void validate(VariableNode variable, ReferenceValidatorTypeAgent agent) {
		DeclarationNode declarationNode = variable.getDeclaration();
		agent.peek().add(declarationNode.getName(), declarationNode.getType().getSite());
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

			Site declaration = null;
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

			value.setFinal(declarationScope.getFinal());
			value.setDeclaration(declaration);
			value.setHostClass(declarationScope.getClassName());
			value.setScope(declarationScope.getScope());
		}
	}

	public static class Scope {

		private ReferenceNode.ReferenceScope scope;
		private String className;
		private Boolean isFinal;
		private Map<String, Site> declarations = new HashMap<>();

		public Scope(ReferenceNode.ReferenceScope scope, String className, Boolean isFinal) {
			this.scope = scope;
			this.className = className;
			this.isFinal = isFinal;
		}

		public ReferenceNode.ReferenceScope getScope() {
			return scope;
		}

		public String getClassName() {
			return className;
		}

		public Boolean getFinal() {
			return isFinal;
		}

		public void add(String name, Site type) {
			declarations.put(name, type);
		}

		public Site find(String name) {
			return declarations.get(name);
		}
	}

}
