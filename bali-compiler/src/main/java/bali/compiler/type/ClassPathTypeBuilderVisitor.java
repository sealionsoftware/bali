package bali.compiler.type;

import bali.annotation.Name;
import bali.annotation.Nullable;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 03/09/13
 */
public class ClassPathTypeBuilderVisitor extends ClassVisitor {

	private static String OPERATOR_ANNOTATION_DESC =
			org.objectweb.asm.Type.getType(bali.annotation.Operator.class).getDescriptor();
	private static String NULLABLE_ANNOTATION_DESC =
			org.objectweb.asm.Type.getType(Nullable.class).getDescriptor();
	private static String PARAM_NAME_ANNOTATION_DESC =
			org.objectweb.asm.Type.getType(Name.class).getDescriptor();

	private TypeLibrary library;

	private Type classpathType;
	private MetaType metaType;

	private String className;
	private List<Declaration> typeParameters = new ArrayList<>();
	private List<Site> interfaces = new ArrayList<>();
	private List<Declaration> constructorParameters;
	private List<Method> methods = new ArrayList<>();
	private List<Operator> operators = new ArrayList<>();
	private List<UnaryOperator> unaryOperators = new ArrayList<>();

	private Map<String, Site> typeVariableBounds = new HashMap<>();

	public ClassPathTypeBuilderVisitor(TypeLibrary library) {
		super(Opcodes.ASM4);
		this.library = library;
	}

	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		super.visit(version, access, name, signature, superName, interfaces);

		className = name.replaceAll("/", ".");

		if ((Opcodes.ACC_INTERFACE & access) > 0) {
			metaType = MetaType.INTERFACE;
		} else {
			metaType = MetaType.CLASS;
		}

		if (signature != null) {
			ClassSignatureVisitor visitor = new ClassSignatureVisitor(library, typeVariableBounds);
			new SignatureReader(signature).accept(visitor);

			typeParameters = visitor.getTypeParameters();
			for (Declaration typeParameter : typeParameters){
				typeVariableBounds.put(typeParameter.getName(), typeParameter.getType());
			}
			this.interfaces = visitor.getInterfaces();
		} else {
			List<Site> ifaces = new ArrayList<>();
			for (String iface : interfaces){
				Reference<Type> ref = library.getReference(iface.replaceAll("/", "."));
				ifaces.add(new VanillaSite(ref));
			}
			this.interfaces = ifaces;
		}
	}

	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		return super.visitAnnotation(desc, visible);
	}

	public MethodVisitor visitMethod(int access, final String name, final String desc, final String signature, String[] exceptions) {

		if ((access & Opcodes.ACC_PUBLIC) != Opcodes.ACC_PUBLIC){
			return null;
		}

		return new MethodVisitor(Opcodes.ASM4, super.visitMethod(access, name, desc, signature, exceptions)) {

			private boolean isOperator;
			private String operatorName;
			private Map<Integer, String> parameterNames = new HashMap<>();

			public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

				if (OPERATOR_ANNOTATION_DESC.equals(desc)) {
					isOperator = true;
					return new AnnotationVisitor(Opcodes.ASM4, super.visitAnnotation(desc, visible)) {
						public void visit(String name, Object value) {
							super.visit(name, value);
							operatorName = (String) value;
						}
					};
				}

				return super.visitAnnotation(desc, visible);
			}

			public AnnotationVisitor visitParameterAnnotation(final int index, String desc, boolean visible) {

				if (PARAM_NAME_ANNOTATION_DESC.equals(desc)) {
					return new AnnotationVisitor(Opcodes.ASM4, super.visitAnnotation(desc, visible)) {
						public void visit(String name, Object value) {
							super.visit(name, value);
							parameterNames.put(index, (String) value);
						}
					};
				}

				return super.visitAnnotation(desc, visible);
			}

			public void visitEnd() {
				super.visitEnd();

				List<Declaration> parameterDeclarations;
				Site returnType = null;

				if (signature != null) {
					MethodSignatureVisitor visitor = new MethodSignatureVisitor(library, typeVariableBounds);
					new SignatureReader(signature).accept(visitor);

					returnType = visitor.getReturnType();
					int i = 0;
					parameterDeclarations = new ArrayList<>();
					for (Site parameterType : visitor.getParameterTypes()) {
						parameterDeclarations.add(new Declaration(parameterNames.get(i++), parameterType));
					}
				} else {
					org.objectweb.asm.Type methodType = org.objectweb.asm.Type.getMethodType(desc);
					org.objectweb.asm.Type methodReturnType = methodType.getReturnType();
					if (!methodReturnType.getClassName().equals(void.class.getName())) {
						returnType = new VanillaSite(library.getReference(methodReturnType.getClassName())); //TODO
					}
					parameterDeclarations = new ArrayList<>();
					int i = 0;
					for (org.objectweb.asm.Type parameterType : methodType.getArgumentTypes()) {
						Site parameterSite = new VanillaSite(library.getReference(parameterType.getClassName()));
						parameterDeclarations.add(new Declaration(
								parameterNames.get(i++),
								parameterSite
						));
					}
				}

				if (name.equals("<init>")) {
					// The method is a constructor

					if (constructorParameters == null) {
						constructorParameters = parameterDeclarations;
					}
				} else if (isOperator) switch (parameterDeclarations.size()) {
					case 1:
						// The method is an operator
						operators.add(new Operator(
								operatorName,
								returnType,
								parameterDeclarations.get(0).getType(),
								name
						));
						break;
					case 0:
						// The method is an unary operator
						unaryOperators.add(new UnaryOperator(
								operatorName,
								returnType,
								name
						));
						break;
				} else {
					// The method is an ordinary method
					methods.add(new Method(
							name,
							returnType,
							parameterDeclarations
					));
				}
			}
		};
	}

	public void visitEnd() {
		super.visitEnd();
		switch (metaType) {
			case CLASS:
				classpathType = new Type(
						className,
						typeParameters,
						interfaces,
						constructorParameters,
						methods,
						Collections.<Operator>emptyList(),
						Collections.<UnaryOperator>emptyList(),
						Collections.<Declaration>emptyList(),
						false
				);
				break;
			case INTERFACE:

//				for (Site implementation : new ArrayList<>(interfaces)){
//					interfaces.addAll(implementation.getInterfaces());
//					methods.addAll(implementation.getMethods());
//					operators.addAll(implementation.getOperators());
//					unaryOperators.addAll(implementation.getUnaryOperators());
//				}

				classpathType = new Type(
						className,
						typeParameters,
						interfaces,
						Collections.<Declaration>emptyList(),
						methods,
						operators,
						unaryOperators,
						Collections.<Declaration>emptyList(),
						true
				);
		}

	}

	public Type getClasspathType() {
		return classpathType;
	}

	private enum MetaType {
		CLASS,
		INTERFACE,
		BEAN
	}
}
