package bali.compiler.validation.visitor;

import bali._;
import bali.compiler.parser.tree.ArgumentDeclaration;
import bali.compiler.parser.tree.CatchStatement;
import bali.compiler.parser.tree.ClassDeclaration;
import bali.compiler.parser.tree.CodeBlock;
import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.ForStatement;
import bali.compiler.parser.tree.MethodDeclaration;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.Reference;
import bali.compiler.parser.tree.TypeReference;
import bali.compiler.parser.tree.Variable;
import bali.compiler.validation.TypeLibrary;
import bali.compiler.validation.ValidationFailure;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
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

	private Scope langScope;

	public ReferenceValidator(TypeLibrary library) {

		// Lang level constants

		Class<_> langClass = _.class;
		List<Declaration> langDeclarations = new ArrayList<>();
		for (Field f : langClass.getDeclaredFields()) {


			Declaration d = new ArgumentDeclaration();
			d.setName(f.getName());
			d.setType(buildType(f.getGenericType(), library));
			langDeclarations.add(d);
		}

		langScope = new Scope(
				Reference.ReferenceScope.STATIC,
				langClass.getName(),
				langDeclarations
		);
	}

	private TypeReference buildType(java.lang.reflect.Type in, TypeLibrary library){
		TypeReference type = new TypeReference();
		Class rawType;

		if (in instanceof ParameterizedType){
			ParameterizedType parameterizedType = (ParameterizedType) in;
			for (java.lang.reflect.Type typeArg : parameterizedType.getActualTypeArguments()){
				type.addParameter(buildType(typeArg, library));
			}
			rawType = (Class) parameterizedType.getRawType();
		} else if (in instanceof Class) {
			rawType = (Class) in;
		} else {
			throw new RuntimeException("Could not determine lang field type: " + type);
		}

		type.setClassName(rawType.getSimpleName());
		try {
			type.setDeclaration(library.getTypeDeclaration(rawType.getName()));
		} catch (ClassNotFoundException e) {
		}
		return type;
	}

	// Engages at the root of the AST, constructs a lookup table
	public List<ValidationFailure> validate(CompilationUnit unit) {

		Deque<Scope> unitLevelScopes = new ArrayDeque<>();
		unitLevelScopes.add(langScope);

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
		if (node instanceof ClassDeclaration) {
			validate((ClassDeclaration) node, agent);
		} else if (node instanceof MethodDeclaration) {
			validate((MethodDeclaration) node, agent);
		} else if (node instanceof ForStatement) {
			validate((ForStatement) node, agent);
		} else if (node instanceof CatchStatement) {
			validate((CatchStatement) node, agent);
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

	private void validate(ClassDeclaration clazz, ReferenceValidatorTypeAgent agent) {
		List<Declaration> referenceableFields = new ArrayList<>();
		referenceableFields.addAll(clazz.getArgumentDeclarations());
		referenceableFields.addAll(clazz.getFields());
		pushAndWalk(clazz, agent, new Scope(
				Reference.ReferenceScope.FIELD,
				clazz.getQualifiedClassName(),
				referenceableFields
		));
	}

	private void validate(MethodDeclaration method, ReferenceValidatorTypeAgent agent) {
		List<Declaration> declarations = new ArrayList<>();
		for (Declaration declaration : method.getArguments()){
			declarations.add(declaration);
		}
		pushAndWalk(method, agent, new Scope(
				Reference.ReferenceScope.VARIABLE,
				null,
				declarations
		));
	}

	private void validate(ForStatement statement, ReferenceValidatorTypeAgent agent) {
		List<Declaration> declarations = new ArrayList<>();
		declarations.add(statement.getElement());
		pushAndWalk(statement, agent, new Scope(
				Reference.ReferenceScope.VARIABLE,
				null,
				declarations
		));
	}

	private void validate(CatchStatement statement, ReferenceValidatorTypeAgent agent) {
		List<Declaration> declarations = new ArrayList<>();
		declarations.add(statement.getDeclaration());
		pushAndWalk(statement, agent, new Scope(
				Reference.ReferenceScope.VARIABLE,
				null,
				declarations
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

			value.setDeclaration(declaration);
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
