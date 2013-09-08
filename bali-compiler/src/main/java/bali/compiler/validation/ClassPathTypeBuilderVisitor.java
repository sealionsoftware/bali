package bali.compiler.validation;

import bali.compiler.validation.type.Class;
import bali.compiler.validation.type.Declaration;
import bali.compiler.validation.type.Interface;
import bali.compiler.validation.type.Method;
import bali.compiler.validation.type.Operator;
import bali.compiler.validation.type.Site;
import bali.compiler.validation.type.Type;
import bali.compiler.validation.type.UnaryOperator;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureReader;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 03/09/13
 */
public class ClassPathTypeBuilderVisitor extends ClassVisitor {

	private Type classpathType;

	private MetaType metaType;
	private String className;
	private List<Declaration> typeParameters;
	private List<Site> interfaces;
	private List<Declaration> constructorParameters;
	private List<Method> methods = new ArrayList<>();
	private List<Operator> operators = new ArrayList<>();
	private List<UnaryOperator> unaryOperators = new ArrayList<>();

	public ClassPathTypeBuilderVisitor() {
		super(Opcodes.ASM4);
	}

	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		super.visit(version, access, name, signature, superName, interfaces);

		className = name.replaceAll("/", ".");

		ClassSignatureVisitor visitor = new ClassSignatureVisitor();
		new SignatureReader(signature).accept(visitor);

		if((Opcodes.ACC_INTERFACE & access) > 0){
			metaType = MetaType.INTERFACE;
		} else {
			metaType = MetaType.BEAN;
		}

		typeParameters = visitor.getTypeParameters();
		this.interfaces = visitor.getInterfaces();
	}

	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		return super.visitAnnotation(desc, visible);
	}

	public MethodVisitor visitMethod(int access, final String name, String desc, final String signature, String[] exceptions) {
		return new MethodVisitor(Opcodes.ASM4, super.visitMethod(access, name, desc, signature, exceptions)) {

			private boolean isOperator;
			private String operatorName;

			public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

				if (desc.equals("bali/annotation/Operator")){
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

			public void visitEnd() {
				super.visitEnd();

				MethodSignatureVisitor visitor = new MethodSignatureVisitor();
				new SignatureReader(signature).accept(visitor);
				List<Declaration> parameterDeclarations = visitor.getParameterDeclarations();

				if (name.equals("<init>")){
					// The method is a constructor

					if (constructorParameters == null){
						constructorParameters = parameterDeclarations;
					}
				} else if (isOperator) switch (parameterDeclarations.size()){
						case 1:
							// The method is an operator
							operators.add(new Operator(
									operatorName,
									visitor.getReturnType(),
									parameterDeclarations.get(0).getType(),
									name
							));
							break;
						case 0:
							// The method is an unary operator
							unaryOperators.add(new UnaryOperator(
									operatorName,
									visitor.getReturnType(),
									name
							));
							break;
				} else {
					// The method is an ordinary method
					methods.add(new Method(
							name,
							visitor.getReturnType(),
							parameterDeclarations
					));
				}
			}
		};
	}

	public void visitEnd() {
		super.visitEnd();
		switch (metaType){
			case CLASS:
				classpathType = new Class(
						className,
						typeParameters,
						constructorParameters,
						methods,
						interfaces
				);
				break;
			case INTERFACE:
				classpathType = new Interface(
						className,
						typeParameters,
						methods,
						interfaces,
						operators,
						unaryOperators
				);
		}

	}

	public Type getClasspathType(){
		return classpathType;
	}

	private enum MetaType {
		CLASS,
		INTERFACE,
		BEAN
	}
}
