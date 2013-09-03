package bali.compiler.validation;

import bali.compiler.parser.tree.ArgumentDeclaration;
import bali.compiler.parser.tree.ClassDeclaration;
import bali.compiler.parser.tree.InterfaceDeclaration;
import bali.compiler.parser.tree.TypeDeclaration;
import bali.compiler.parser.tree.TypeParameter;
import bali.compiler.validation.type.Declaration;
import bali.compiler.validation.type.Method;
import bali.compiler.validation.type.Type;
import bali.compiler.validation.type.Class;
import bali.compiler.validation.type.Interface;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps between the Parsers internal AST representation of a "TypeDeclaration" and the Validators "Type" model
 *
 * User: Richard
 * Date: 28/08/13
 */
public class TypeDeclarationTypeBuilder {

	private TypeLibrary library;

	public TypeDeclarationTypeBuilder(TypeLibrary library) {
		this.library = library;
	}

	public Type build(TypeDeclaration declaration) {
		if (declaration instanceof ClassDeclaration){
			return build((ClassDeclaration) declaration);
		}
		if (declaration instanceof InterfaceDeclaration){
			return build((InterfaceDeclaration) declaration);
		}
		throw new RuntimeException("Cannot build MetaTypes like " + declaration);
	}

	public Type build(ClassDeclaration declaration) {
		return new Class(
				declaration.getQualifiedClassName(),
				getParameters(declaration),
				getArguments(declaration),
				getMethods(declaration)
		);
	}

	public Type build(InterfaceDeclaration declaration) {
		return new Interface(
				declaration.getQualifiedClassName(),
				getParameters(declaration),
				getMethods(declaration)
		);
	}

	private List<Declaration> getParameters(TypeDeclaration declaration) {

		List<Declaration> parameters = new ArrayList<>();
		for (TypeParameter declaredParameter : (List<TypeParameter>) declaration.getParameters()){ // TODO: remove this cast
			parameters.add(new Declaration(
					declaredParameter.getName(),
					library.getType(declaredParameter.getType().getClassName())
			));
		}
		return parameters;
	}

	private List<Declaration> getArguments(ClassDeclaration declaration) {
		List<Declaration> arguments = new ArrayList<>();
		for (ArgumentDeclaration declaredArgument : declaration.getArgumentDeclarations()){
			arguments.add(new Declaration(
					declaredArgument.getName(),
					library.getType(declaredArgument.getType().getClassName())
			));
		}
		return arguments;
	}

	private List<Method> getMethods(TypeDeclaration<? extends bali.compiler.parser.tree.Method> declaration) {
		List<Method> methods = new ArrayList<>();
		for (bali.compiler.parser.tree.Method declaredMethod : declaration.getMethods()){
			List<Declaration> arguments = new ArrayList<>();
			for (ArgumentDeclaration declaredArgument : declaredMethod.getArguments()){
				arguments.add(new Declaration(
						declaredArgument.getName(),
						library.getType(declaredArgument.getType().getClassName())
				));
			}
			methods.add(new Method(
					declaredMethod.getName(),
					library.getType(declaredMethod.getType().getClassName()),
					arguments
			));
		}
		return methods;
	}


}
