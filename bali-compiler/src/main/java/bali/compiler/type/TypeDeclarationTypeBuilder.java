package bali.compiler.type;

import bali.compiler.parser.tree.ArgumentDeclarationNode;
import bali.compiler.parser.tree.BeanNode;
import bali.compiler.parser.tree.ClassNode;
import bali.compiler.parser.tree.InterfaceNode;
import bali.compiler.parser.tree.MethodDeclaringTypeNode;
import bali.compiler.parser.tree.MethodNode;
import bali.compiler.parser.tree.PropertyNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.parser.tree.TypeNode;
import bali.compiler.parser.tree.TypeParameterNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Maps between the Parsers internal AST representation of a "TypeDeclaration" and the Validators "Type" model
 * <p/>
 * User: Richard
 * Date: 28/08/13
 */
public class TypeDeclarationTypeBuilder {

	public Type build(TypeNode declaration) {
		if (declaration instanceof ClassNode) {
			return build((ClassNode) declaration);
		}
		if (declaration instanceof InterfaceNode) {
			return build((InterfaceNode) declaration);
		}
		if (declaration instanceof BeanNode){
			return build((BeanNode) declaration);
		}
		throw new RuntimeException("Cannot build MetaTypes like " + declaration);
	}

	public Type build(ClassNode declaration) {
		return new Type(
				declaration.getQualifiedClassName(),
				null,
				getTypeParameters(declaration),
				getInterfaces(declaration),
				getParameters(declaration),
				getMethods(declaration),
				Collections.<Operator>emptyList(),
				Collections.<UnaryOperator>emptyList(),
				Collections.<Declaration>emptyList(),
				false
		);
	}

	public Type build(InterfaceNode declaration) {
		return new Type(
				declaration.getQualifiedClassName(),
				null,
				getTypeParameters(declaration),
				getInterfaces(declaration),
				Collections.<Declaration>emptyList(),
				getMethods(declaration),
				getOperators(declaration),
				getUnaryOperators(declaration),
				Collections.<Declaration>emptyList(),
				true
		); // TODO - self declared values, monitors
	}

	public Type build(BeanNode declaration) {
		SiteNode superType = declaration.getSuperType();
		return new Type(
				declaration.getQualifiedClassName(),
				superType != null ? superType.getSite() : null ,
				getTypeParameters(declaration),
				Collections.<Site>emptyList(),
				Collections.<Declaration>emptyList(),
				Collections.<Method>emptyList(),
				Collections.<Operator>emptyList(),
				Collections.<UnaryOperator>emptyList(),
				getProperties(declaration),
				true
		);
	}

	private List<Declaration> getProperties(BeanNode declaration) {
		List<Declaration> ret = new ArrayList<>();
		for (PropertyNode node : declaration.getProperties()){
			ret.add(new Declaration(node.getName(), node.getType().getSite()));
		}
		return ret;
	}

	private List<Declaration> getTypeParameters(TypeNode declaration) {

		List<Declaration> parameters = new ArrayList<>();
		for (TypeParameterNode declaredParameter : declaration.getTypeParameters()) {
			parameters.add(new Declaration(
					declaredParameter.getName(),
					getType(declaredParameter.getType())
			));
		}
		return parameters;
	}

	private List<Declaration> getParameters(ClassNode declaration) {
		List<Declaration> arguments = new ArrayList<>();
		for (ArgumentDeclarationNode declaredArgument : declaration.getArgumentDeclarations()) {
			arguments.add(new Declaration(
					declaredArgument.getName(),
					getType(declaredArgument.getType())
			));
		}
		return arguments;
	}

	private List<Method> getMethods(MethodDeclaringTypeNode<? extends MethodNode> declaration) {
		List<Method> methods = new ArrayList<>();
		for (MethodNode declaredMethod : declaration.getMethods()) {
			List<Declaration> arguments = new ArrayList<>();
			for (ArgumentDeclarationNode declaredArgument : declaredMethod.getArguments()) {
				arguments.add(new Declaration(
						declaredArgument.getName(),
						getType(declaredArgument.getType())
				));
			}
			SiteNode returnTypeNode = declaredMethod.getType();
			methods.add(new Method(
					declaredMethod.getName(),
					returnTypeNode != null ? getType(declaredMethod.getType()) : null,
					arguments
			));
		}
		return methods;
	}

	private List<Site> getInterfaces(MethodDeclaringTypeNode<? extends MethodNode> declaration) {
		List<Site> ret = new ArrayList<>();
		for (SiteNode typeReference : declaration.getImplementations()) {
			ret.add(getType(typeReference));
		}
		return ret;
	}

	private List<UnaryOperator> getUnaryOperators(InterfaceNode declaration) {
		List<UnaryOperator> ret = new ArrayList<>();
//		for (SiteNode typeReference : declaration.getO()){
//			ret.add(getType(typeReference));
//		}
		return ret; //TODO: self defined unary operators
	}

	private List<Operator> getOperators(InterfaceNode declaration) {
		List<Operator> ret = new ArrayList<>();
//		for (SiteNode typeReference : declaration.getO()){
//			ret.add(getType(typeReference));
//		}
		return ret; //TODO: self defined operators
	}

	private Site getType(SiteNode reference) {
		return reference.getSite();
	}
}
