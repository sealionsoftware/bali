package bali.compiler.type;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;
import bali.annotation.Nullable;
import bali.annotation.SelfTyped;
import bali.annotation.ThreadSafe;
import bali.compiler.reference.Reference;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
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

	private static final String OPERATOR_ANNOTATION_DESC =
			org.objectweb.asm.Type.getType(bali.annotation.Operator.class).getDescriptor();
	private static final String NULLABLE_ANNOTATION_DESC =
			org.objectweb.asm.Type.getType(Nullable.class).getDescriptor();
	private static final String THREADSAFE_ANNOTATION_DESC =
			org.objectweb.asm.Type.getType(ThreadSafe.class).getDescriptor();
	private static final String PARAM_NAME_ANNOTATION_DESC =
			org.objectweb.asm.Type.getType(Name.class).getDescriptor();
	private static final String SELF_TYPED_ANNOTATION_DESC =
			org.objectweb.asm.Type.getType(SelfTyped.class).getDescriptor();
	private static final String METATYPE_ANNOTATION_DESC =
			org.objectweb.asm.Type.getType(MetaType.class).getDescriptor();

	private static final int PUBLIC_STATIC = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC;

	private ClassLibrary library;

	private Class classpathClass;
	private Kind metaType;

	private String className;
	private List<Declaration<Type>> typeParameters = new ArrayList<>();
	private Type superType;
	private List<Type> interfaces = new ArrayList<>();
	private List<Declaration<Site>> constructorParameters;
	private List<Method> methods = new ArrayList<>();
	private List<Operator> operators = new ArrayList<>();
	private List<UnaryOperator> unaryOperators = new ArrayList<>();
	private List<Declaration<Site>> properties = new ArrayList<>();

	private Map<String, Type> typeVariableBounds = new HashMap<>();

	private Type nullBound;
	private boolean constructorSet = false;

	public ClassPathTypeBuilderVisitor(ClassLibrary library) {
		super(Opcodes.ASM4);
		this.library = library;
		nullBound = new ParameterisedType(library.getReference(Object.class.getName()));
	}

	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		super.visit(version, access, name, signature, superName, interfaces);

		className = name.replaceAll("/", ".");

		if (signature != null) {
			ClassSignatureVisitor visitor = new ClassSignatureVisitor(library, typeVariableBounds);
			new SignatureReader(signature).accept(visitor);

			typeParameters = visitor.getTypeParameters();
			for (Declaration<Type> typeParameter : typeParameters){
				Type bound = typeParameter.getType();
				typeVariableBounds.put(typeParameter.getName(), bound != null ? bound : nullBound);
			}
			this.interfaces = visitor.getInterfaces();
			this.superType = visitor.getSuperType();
		} else {
			List<Type> ifaces = new ArrayList<>();
			for (String iface : interfaces){
				Reference<Class> ref = library.getReference(iface.replaceAll("/", "."));
				ifaces.add(new ParameterisedType(ref));
			}
			this.interfaces = ifaces;
			if (superName != null){
				this.superType = new ParameterisedType(library.getReference(superName.replaceAll("/", ".")));
			}
		}
	}

	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

		if (METATYPE_ANNOTATION_DESC.equals(desc)){
			return new AnnotationVisitor(Opcodes.ASM4) {
				public void visitEnum(String name, String desc, String value) {
					if (name.equals("value")){
						metaType = Kind.valueOf(value);
					}
				}
			};
		}

		return super.visitAnnotation(desc, visible);
	}

	public FieldVisitor visitField(int access, final String name, final String desc, final String signature, final Object value) {
		if ((access & PUBLIC_STATIC) != Opcodes.ACC_PUBLIC){
			return null;
		}

		return new FieldVisitor(Opcodes.ASM4) {

			private SiteData data = new SiteData();

			public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
				if (NULLABLE_ANNOTATION_DESC.equals(desc)){
					data.nullable = true;
				} else if (THREADSAFE_ANNOTATION_DESC.equals(desc)){
					data.threadSafe = true;
				} else if (SELF_TYPED_ANNOTATION_DESC.equals(desc)){
					data.selfTyped = true;
				}
				return super.visitAnnotation(desc, visible);
			}

			public void visitEnd() {

				Site fieldType;
				if (signature != null) {
					SiteSignatureVisitor visitor = new SiteSignatureVisitor(library, typeVariableBounds, data);
					new SignatureReader(signature).accept(visitor);
					fieldType = visitor.getSite();
				} else {
					fieldType = makeSite(
							library.getReference(org.objectweb.asm.Type.getType(desc).getClassName()),
							data
					);
				}
				properties.add(new Declaration<>(
						name,
						fieldType
				));
			}
		};
	}

	public MethodVisitor visitMethod(int access, final String name, final String desc, final String signature, String[] exceptions) {

		if ((access & PUBLIC_STATIC) != Opcodes.ACC_PUBLIC){
			return null;
		}

		final int numberOfParameters = org.objectweb.asm.Type.getArgumentTypes(desc).length;

		return new MethodVisitor(Opcodes.ASM4, super.visitMethod(access, name, desc, signature, exceptions)) {

			private boolean isOperator;
			private String operatorName;
			private SiteData returnData = new SiteData();
			private List<ParameterData> parameterData = new ArrayList<>(numberOfParameters);{
				for (int i = 0 ; i < numberOfParameters ; i++){
					parameterData.add(new ParameterData());
				}
			}

			public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

				if (OPERATOR_ANNOTATION_DESC.equals(desc)) {
					isOperator = true;
					return new AnnotationVisitor(Opcodes.ASM4, super.visitAnnotation(desc, visible)) {
						public void visit(String name, Object value) {
							super.visit(name, value);
							operatorName = (String) value;
						}
					};
				} else if (NULLABLE_ANNOTATION_DESC.equals(desc)){
					returnData.nullable = true;
				} else if (THREADSAFE_ANNOTATION_DESC.equals(desc)){
					returnData.threadSafe = true;
				} else if (SELF_TYPED_ANNOTATION_DESC.equals(desc)){
					returnData.selfTyped = true;
				}

				return super.visitAnnotation(desc, visible);
			}

			public AnnotationVisitor visitParameterAnnotation(final int index, String desc, boolean visible) {

				if (PARAM_NAME_ANNOTATION_DESC.equals(desc)) {
					return new AnnotationVisitor(Opcodes.ASM4, super.visitAnnotation(desc, visible)) {
						public void visit(String name, Object value) {
							super.visit(name, value);
							parameterData.get(index).name = (String) value;
						}
					};
				} else if (NULLABLE_ANNOTATION_DESC.equals(desc)){
					parameterData.get(index).nullable = true;
				} else if (THREADSAFE_ANNOTATION_DESC.equals(desc)){
					parameterData.get(index).threadSafe = true;
				} else if (SELF_TYPED_ANNOTATION_DESC.equals(desc)){
					parameterData.get(index).selfTyped = true;
				}

				return super.visitAnnotation(desc, visible);
			}

			public void visitEnd() {
				super.visitEnd();

				List<Declaration<Site>> parameterDeclarations;
				Site returnType = null;

				if (signature != null) {
					MethodSignatureVisitor visitor = new MethodSignatureVisitor(library, typeVariableBounds, returnData, parameterData);
					new SignatureReader(signature).accept(visitor);
					returnType = visitor.getReturnType();
					int i = 0;
					parameterDeclarations = new ArrayList<>();
					for (Site parameterType : visitor.getParameterTypes()) {
						ParameterData data = parameterData.get(i++);
						parameterDeclarations.add(new Declaration<>(data.name, parameterType));
					}
				} else {
					org.objectweb.asm.Type methodType = org.objectweb.asm.Type.getMethodType(desc);
					org.objectweb.asm.Type methodReturnType = methodType.getReturnType();
					if (!methodReturnType.getClassName().equals(void.class.getName())) {
						returnType = makeSite(
								library.getReference(methodReturnType.getClassName()),
								returnData
						);
					}
					parameterDeclarations = new ArrayList<>();
					int i = 0;
					for (org.objectweb.asm.Type parameterType : methodType.getArgumentTypes()) {
						ParameterData data = parameterData.get(i++);
						Site parameterSite = makeSite(
								library.getReference(parameterType.getClassName()),
								data
						);
						parameterDeclarations.add(new Declaration<>(
								data.name,
								parameterSite
						));
					}
				}

				if (name.equals("<init>")) {
					// The method is a constructor
					if (constructorParameters != null){
						throw new RuntimeException("Cannot load class with multiple constructors as a Bali type");
					}
					constructorParameters = parameterDeclarations;
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

	private Site makeSite(Reference<Class> type, SiteData data){
		Site ret = new ParameterisedSite(type, data.nullable, data.threadSafe);
		if (data.selfTyped){
			ret = new SelfSite(ret);
		}
		return ret;
	}

	public void visitEnd() {
		super.visitEnd();

		if (metaType == null){
			throw new RuntimeException("The type " + className + " has no @MetaType annotation and is therefore not a valid Bali type");
		}

		List<Declaration<Site>> constructorParameters = this.constructorParameters != null ? this.constructorParameters : Collections.<Declaration<Site>>emptyList();

		classpathClass = new MutableClassModel(
				className,
				superType,
				typeParameters,
				interfaces,
				constructorParameters,
				methods,
				operators,
				unaryOperators,
				properties,
				metaType
		);
	}

	public Class getClasspathClass() {
		return classpathClass;
	}
}
