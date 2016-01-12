package com.sealionsoftware.bali.compiler.type;

import com.sealionsoftware.bali.compiler.Method;
import com.sealionsoftware.bali.compiler.Operator;
import com.sealionsoftware.bali.compiler.Parameter;
import com.sealionsoftware.bali.compiler.Type;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class ClassPathTypeBuilderVisitor extends ClassVisitor implements Opcodes {

    private static final String OPERATOR_ANNOTATION_DESC =
            org.objectweb.asm.Type.getType(bali.annotation.Operator.class).getDescriptor();

    private final ClasspathClassFactory library;

    private Class classpathClass;

    private List<Parameter> typeParameters = new ArrayList<>();
    private Type superType;
    private List<Type> interfaces = new ArrayList<>();
    private List<Method> methods = new ArrayList<>();
    private List<Operator> operators = new ArrayList<>();

    private Map<String, Type> typeVariableBounds = new HashMap<>();

    public ClassPathTypeBuilderVisitor(ClasspathClassFactory library, String name) {
        super(ASM5);
        this.library = library;
        classpathClass = new Class(name);
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {

        if (signature != null) {
            ClassSignatureVisitor visitor = new ClassSignatureVisitor(library, typeVariableBounds);
            new SignatureReader(signature).accept(visitor);

            typeParameters = visitor.getTypeParameters();
            for (Parameter typeParameter : typeParameters){
                Type bound = typeParameter.type;
                typeVariableBounds.put(typeParameter.name, bound);
            }

            this.interfaces = visitor.getInterfaces();
            this.superType = visitor.getSuperType();
        } else {
            List<Type> ifaces = new ArrayList<>();
            for (String iface : interfaces){
                Class ref = library.get(fromLocalName(iface));
                ifaces.add(new ClassBasedType(ref));
            }

            this.interfaces = ifaces;
            if (superName != null){
                String superClassName = fromLocalName(superName);
                if (!superClassName.equals(Object.class.getName())){
                    superType = new ClassBasedType(library.get(superClassName));
                }
            }
        }
    }

    public MethodVisitor visitMethod(int access, final String name, final String desc, final String signature, String[] exceptions) {

        final Type returnType;
        final List<Parameter> parameters;

        if(signature != null) {
            MethodSignatureVisitor visitor = new MethodSignatureVisitor(library, typeVariableBounds);
            new SignatureReader(signature).accept(visitor);
            returnType = visitor.getReturnType();
            parameters = visitor.getParameterTypes().stream().map((type) -> new Parameter(null, type)).collect(Collectors.toList());

        }  else {
            org.objectweb.asm.Type methodType = org.objectweb.asm.Type.getMethodType(desc);
            org.objectweb.asm.Type methodReturnType = methodType.getReturnType();
            returnType = methodReturnType.getClassName().equals(void.class.getName()) ? null : new ClassBasedType(library.get(methodReturnType.getClassName()));
            parameters = stream(methodType.getArgumentTypes()).map((type) -> new Parameter(null, new ClassBasedType(library.get(type.getClassName())))).collect(Collectors.toList());
        }

        return new MethodVisitor(Opcodes.ASM4, super.visitMethod(access, name, desc, signature, exceptions)) {

            private boolean isOperator;
            private String operatorName;

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


            public void visitEnd() {
                super.visitEnd();

                if (isOperator) {
                    operators.add(new Operator(
                            name,
                            returnType,
                            parameters,
                            operatorName
                    ));
                } else if (!name.equals("<init>")){
                    methods.add(new Method(
                            name,
                            returnType,
                            parameters
                    ));
                }
            }
        };
    }

    public void visitEnd() {
        super.visitEnd();

        classpathClass.initialise(
                typeParameters,
                superType,
                interfaces,
                methods,
                operators
        );
    }

    public Class getClasspathClass() {
        return classpathClass;
    }

    private static String fromLocalName(String in){
        return in.replaceAll("/", ".");
    }


}
