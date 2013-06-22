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
 * User: Richard
 * Date: 14/05/13
 */
public class TypeResolvingValidator implements Validator<CompilationUnit> {

	private TypeDeclarationLibrary library;

	public TypeResolvingValidator(TypeDeclarationLibrary library) {
		this.library = library;
	}

	// Engages at the root of the AST, constructs a lookup table of class names to declarations
	public List<ValidationFailure> validate(CompilationUnit unit) {

		List<ValidationFailure> ret = new ArrayList<>();

		Map<String, TypeDeclaration> resolvables = new HashMap<>();
		for (Interface iface: unit.getInterfaces()){
			resolvables.put(iface.getClassName(), iface);
		}
		for (bali.compiler.parser.tree.Class clazz: unit.getClasses()){
			resolvables.put(clazz.getClassName(), clazz);
		}
		for (Import iport: unit.getImports()){

			try {
				TypeDeclaration typeDeclaration = library.getTypeDeclaration(iport.getName());
				resolvables.put(typeDeclaration.getClassName(), typeDeclaration);
			} catch (Exception e) {
				ret.add(new ValidationFailure(iport, "Could not resolve imported class"));
			}
		}

		Agent agent = new Agent(resolvables, library);
		walkAgentOverChildren(unit, agent);
		return ret;
	}

//	private TypeDeclaration getTypeDeclaration(java.lang.reflect.Type type){
//
//		if (type instanceof Class){
//			return getTypeDeclarationForClass((Class) type);
//		}
//		if (type instanceof ParameterizedType){
//			return getTypeDeclarationForParameterizedType((ParameterizedType) type);
//		}
//		throw new RuntimeException();
//	}
//
//	private TypeDeclaration getTypeDeclarationForParameterizedType(ParameterizedType type){
//		TypeDeclaration raw = getTypeDeclaration(type.getRawType());
//		for (java.lang.reflect.Type argumentType : type.getActualTypeArguments()){
//			raw.addParameter(getType(argumentType));
//		}
//		return raw;
//	}





	private List<ValidationFailure> walkAgentOverChildren(Node node, Agent agent){
		List<ValidationFailure> ret = new ArrayList<>();
		if (node instanceof Type){
			ret.addAll(agent.validate((Type) node));
		}
		for (Node child : node.getChildren()){
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

			if (declaration == null){
				try {
					declaration = library.getTypeDeclaration(type.getClassName());
				} catch (ClassNotFoundException e) {
				}
			}

			if (declaration == null){
				ret.add(new ValidationFailure(type, "Cannot resolve type of declaration"));
			}
			type.setDeclaration(declaration);
			return ret;
		}
	}



}
