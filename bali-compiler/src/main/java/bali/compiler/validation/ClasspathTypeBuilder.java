package bali.compiler.validation;

import bali.compiler.validation.type.Declaration;
import bali.compiler.validation.type.Method;
import bali.compiler.validation.type.Site;
import bali.compiler.validation.type.Type;
import bali.compiler.validation.type.Class;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds a Validator "Type" model for referenced types on the classpath
 *
 * User: Richard
 * Date: 23/08/13
 */
public class ClasspathTypeBuilder {

	private TypeLibrary library;

	public ClasspathTypeBuilder(TypeLibrary library) {
		this.library = library;
	}

	public Type build(String typeToBuild){

		ClassReader reader;
		try {
			reader = new ClassReader(typeToBuild);
		} catch (IOException e) {
			throw new RuntimeException("Cannot read bytecode for class " + typeToBuild, e);
		}

		ClassNode node = new ClassNode();
		reader.accept(node, ClassReader.SKIP_CODE);

		return build(node);
	}

	public Type build(ClassNode node){
		return new Class(
			node.name,
			getParameters(node),
			getArguments(node),
			getMethods(node),
			getInterfaces(node)
		);
	}

	private List<Declaration> getParameters(ClassNode node) {

		final List<Declaration> parameters = new ArrayList<>();

		SignatureReader sr = new SignatureReader(node.signature);
		sr.acceptType(new SignatureVisitor(Opcodes.ASM4) {
			public void visitTypeVariable(String name) {
				parameters.add(new Declaration(
					name, null
				));
			}
		});

		return parameters;
	}

	private List<Declaration> getArguments(ClassNode node) {
		final List<Declaration> arguments = new ArrayList<>();
		for (MethodNode method : (List<MethodNode>) node.methods){
			if (method.name.equals("<init>")){
				org.objectweb.asm.Type methodType = org.objectweb.asm.Type.getMethodType(method.signature);
				for (org.objectweb.asm.Type argumentType : methodType.getArgumentTypes()){
					arguments.add(new Declaration(
							null,
							getType(argumentType)
					));
				}
				break;
			}
		}
		return arguments;
	}

	private List<Method> getMethods(ClassNode node) {
		final List<Method> methods = new ArrayList<>();

		for (MethodNode method : (List<MethodNode>) node.methods){

			Site returnType = null;
			String methodName = null;
			List<Declaration> argumentTypes = null;


			SignatureReader sr = new SignatureReader(method.signature);
			sr.acceptType(new SignatureVisitor(Opcodes.ASM4) {
				public void visitTypeVariable(String name) {
					super.visitTypeVariable(name);
				}
			});

//			org.objectweb.asm.Type methodType = org.objectweb.asm.Type.getMethodType(method.signature);
//			List<Declaration> arguments = new ArrayList<>();
//			for (org.objectweb.asm.Type argumentType : methodType.getArgumentTypes()){
//
//				argumentType;
//
//
//				arguments.add(new Declaration(
//						node.sourceDebug,
//						getType(argumentType)
//				));
//			}

			methods.add(new Method(
					methodName,
					returnType,
					argumentTypes
			));
		}
		return methods;
	}

	private Site getType(org.objectweb.asm.Type asmType){
		Type base = library.getType(asmType.getClassName());
		List<Site> params = asmType.getDescriptor()
		return new Site(base);
	}


//	public Type build(ClassNode node){
//
//		ClasspathTypeDeclaration declaration = new ClasspathTypeDeclaration();
//
//		Type classType = Type.getType(node.name);
//		String className = convertASMClassName(classType);
//
//		declaration.setClassName(className.substring(className.lastIndexOf(".") + 1));
//		declaration.setAbstract((node.access & Opcodes.ACC_ABSTRACT) == Opcodes.ACC_ABSTRACT);
//		declaration.setQualifiedClassName(className);
//
////		Map<String, Method> methods = new HashMap<>();
////
////		for(Type genericInterface : clazz.getGenericInterfaces()){
//////			Class genericInterfaceClass = getClassForType(genericInterface);
////			bali.compiler.parser.tree.Type typeReference = getReferenceForType(genericInterface);
////			for (Method method : typeReference.getDeclaration().getMethods()){
////				declaration.addMethod(method);
////			}
////			declaration.addImplementation(typeReference);
////		}
//
//		for (MethodNode method : (List<MethodNode>) node.methods){
//			if (!method.name.equals("<init>")){
//				declaration.addMethod(build(method));
//			}
//		}
//
//		return declaration;
//	}

//	private Method build(MethodNode methodNode){
//
//		Method constructedMethod = new Method();
//		constructedMethod.setName(methodNode.name);
////		constructedMethod.setType(methodNode.signature);
//
////		Operator operator = reflectedMethod.getAnnotation(Operator.class);
////		if (operator != null){
////			constructedMethod.setOperator(operator.value());
////		}
//
//		return constructedMethod;
//	}



//	private bali.compiler.parser.tree.Type getReferenceForType(Type type){
//
//		bali.compiler.parser.tree.Type typeReference = new bali.compiler.parser.tree.Type();
//
//		Class clazz;
//		if (type instanceof Class){
//			clazz = (Class) type;
//		} else if (type instanceof ParameterizedType){
//			ParameterizedType parameterizedType = (ParameterizedType) type;
//			clazz = (Class) parameterizedType.getRawType();
//			for (Type argType : parameterizedType.getActualTypeArguments()){
//				typeReference.addParameter(getReferenceForType(argType));
//			}
//		} else {
//			throw new RuntimeException("Cannot Create reference for type " + type);
//		}
//
//		typeReference.setClassName(clazz.getName());
//		typeReference.setDeclaration(library.getTypeDeclaration(clazz));
//		return typeReference;
//	}
//
//	private Class getClassForType(Type type){
//		if (type instanceof Class){
//			return (Class) type;
//		}
//		if (type instanceof ParameterizedType){
//			return (Class) ((ParameterizedType) type).getRawType();
//		}
//		throw new RuntimeException("Cannot get the actual class of type " + type);
//	}

//	public ClasspathTypeDeclaration build(ParameterizedType parameterizedType){
//		ClasspathTypeDeclaration declaration = new ClasspathTypeDeclaration();
//
//		declaration.set
//
//		return declaration;
//	}

}
