package bali.compiler.type;

import bali.annotation.Kind;
import bali.compiler.parser.tree.BeanNode;
import bali.compiler.parser.tree.ClassNode;
import bali.compiler.parser.tree.InterfaceNode;
import bali.compiler.parser.tree.MethodDeclaringClassNode;
import bali.compiler.parser.tree.MethodNode;
import bali.compiler.parser.tree.ObjectNode;
import bali.compiler.parser.tree.ParameterNode;
import bali.compiler.parser.tree.PropertyNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.parser.tree.TypeParameterNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Maps between the Parsers internal AST representation of a "TypeDeclaration" and the Validators "Class" model
 * <p/>
 * User: Richard
 * Date: 28/08/13
 */
public class ClassDeclarationTypeBuilder {

	public Class build(ClassNode declaration) {
		if (declaration instanceof ObjectNode) {
			return build((ObjectNode) declaration);
		}
		if (declaration instanceof InterfaceNode) {
			return build((InterfaceNode) declaration);
		}
		if (declaration instanceof BeanNode){
			return build((BeanNode) declaration);
		}
		// TODO - self declared values, monitors
		throw new RuntimeException("Cannot build Kind like " + declaration);
	}

	public Class build(ObjectNode declaration) {
		return new MutableClassModel(
				declaration.getQualifiedClassName(),
				null,
				getTypeParameters(declaration),
				getInterfaces(declaration),
				getParameters(declaration),
				getMethods(declaration),
				Collections.<Operator>emptyList(),
				Collections.<UnaryOperator>emptyList(),
				Collections.<Declaration<Site>>emptyList(),
				Kind.OBJECT
		);
	}

	public Class build(InterfaceNode declaration) {
		return new MutableClassModel(
				declaration.getQualifiedClassName(),
				null,
				getTypeParameters(declaration),
				getInterfaces(declaration),
				Collections.<Declaration<Site>>emptyList(),
				getMethods(declaration),
				getOperators(declaration),
				getUnaryOperators(declaration),
				Collections.<Declaration<Site>>emptyList(),
				Kind.INTERFACE
		);
	}

	public Class build(BeanNode declaration) {
		SiteNode superType = declaration.getSuperType();
		List<Declaration<Site>> properties = getProperties(declaration);
		return new MutableClassModel(
				declaration.getQualifiedClassName(),
				superType != null ? superType.getSite() : null ,
				getTypeParameters(declaration),
				Collections.<Type>emptyList(),
				properties,
				Collections.<Method>emptyList(),
				Collections.<Operator>emptyList(),
				Collections.<UnaryOperator>emptyList(),
				properties,
				Kind.BEAN
		);
	}

	private List<Declaration<Site>> getProperties(BeanNode declaration) {
		List<Declaration<Site>> ret = new ArrayList<>();
		for (PropertyNode node : declaration.getProperties()){
			ret.add(new Declaration<>(node.getName(), node.getType().getSite()));
		}
		return ret;
	}

	private List<Declaration<Type>> getTypeParameters(ClassNode declaration) {

		List<Declaration<Type>> parameters = new ArrayList<>();
		for (TypeParameterNode declaredParameter : declaration.getTypeParameters()) {

			parameters.add(new Declaration<Type>(
					declaredParameter.getName(),
					getType(declaredParameter.getType())
			));
		}
		return parameters;
	}

	private List<Declaration<Site>> getParameters(ObjectNode declaration) {
		List<Declaration<Site>> arguments = new ArrayList<>();
		for (ParameterNode declaredArgument : declaration.getParameters()) {
			arguments.add(new Declaration<>(
					declaredArgument.getName(),
					getType(declaredArgument.getType())
			));
		}
		return arguments;
	}

	private List<Method> getMethods(MethodDeclaringClassNode<? extends MethodNode> declaration) {
		List<Method> methods = new ArrayList<>();
		for (MethodNode declaredMethod : declaration.getMethods()) {
			List<Declaration<Site>> arguments = new ArrayList<>();
			for (ParameterNode declaredArgument : declaredMethod.getParameters()) {
				arguments.add(new Declaration<>(
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

	private List<Type> getInterfaces(MethodDeclaringClassNode<? extends MethodNode> declaration) {
		List<Type> ret = new ArrayList<>();
		for (SiteNode typeReference : declaration.getImplementations()) {
			ret.add(getType(typeReference));
		}
		return ret;
	}

	private List<UnaryOperator> getUnaryOperators(InterfaceNode declaration) {
		List<UnaryOperator> ret = new ArrayList<>();
//		for (SiteNode typeReference : declaration.getO()){
//			ret.add(getTemplate(typeReference));
//		}
		return ret; //TODO: self defined unary operators
	}

	private List<Operator> getOperators(InterfaceNode declaration) {
		List<Operator> ret = new ArrayList<>();
//		for (SiteNode typeReference : declaration.getO()){
//			ret.add(getTemplate(typeReference));
//		}
		return ret; //TODO: self defined operators
	}

	private Site getType(SiteNode reference) {
		return reference != null ? reference.getSite() : null;
	}
}
