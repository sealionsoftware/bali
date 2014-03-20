package bali.compiler.bytecode;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;
import bali.annotation.Nullable;
import bali.annotation.Parameters;
import bali.annotation.SelfTyped;
import bali.annotation.ThreadSafe;
import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.BeanNode;
import bali.compiler.parser.tree.PropertyNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.type.Class;
import bali.compiler.type.Declaration;
import bali.compiler.type.SelfSite;
import bali.compiler.type.Site;
import bali.compiler.type.Type;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 13/05/13
 */
public class ASMBeanGenerator implements Generator<BeanNode, GeneratedClass> {

	private ASMConverter converter = new ASMConverter();
	private String nameAnnotationDescriptor = converter.getTypeDescriptor(Name.class.getName());
	private String nullableAnnotationDescriptor = converter.getTypeDescriptor(Nullable.class.getName());
	private String threadSafeAnnotationDescriptor = converter.getTypeDescriptor(ThreadSafe.class.getName());
	private String selfTypedAnnotationDescriptor = converter.getTypeDescriptor(SelfTyped.class.getName());

	public GeneratedClass build(BeanNode input) throws Exception {

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		Class superClass = input.getSuperType() != null ? input.getSuperType().getSite().getTemplate() : null;
		String hostClassInternalName = converter.getInternalName(input.getQualifiedClassName());
		String superClassInternalName = superClass != null ? converter.getInternalName(superClass) : "java/lang/Object";

		cw.visit(V1_7,
				ACC_PUBLIC + ACC_SUPER,
				hostClassInternalName,
				null,
				superClassInternalName,
				null);

		AnnotationVisitor av = cw.visitAnnotation(converter.getTypeDescriptor(MetaType.class.getName()), false);
		av.visitEnum("value", converter.getTypeDescriptor(Kind.class.getName()), Kind.BEAN.name());
		av.visitEnd();

		buildConstructor(cw, superClassInternalName, hostClassInternalName, input);

		for (PropertyNode property : input.getProperties()) {
			buildProperty(property, cw);
		}

		cw.visitEnd();

		return new GeneratedClass(input.getClassName(), cw.toByteArray());
	}

	private void buildConstructor(ClassWriter cw, String superClassInternalName, String hostClassInternalName, BeanNode input) {

		List<Declaration<Site>> superParameters = new ArrayList<>();
		SiteNode superClass = input.getSuperType();
		if (superClass != null){
			superParameters.addAll(getAllParameters(superClass.getSite()));
		}
		List<Declaration<Site>> allParams = new ArrayList<>(superParameters);
		for (PropertyNode propertyNode : input.getProperties()){
			allParams.add(new Declaration<>(propertyNode.getName(), propertyNode.getType().getSite()));
		}

		List<Site> allParamSites = new ArrayList<>();
		for (Declaration<Site> param : allParams){
			Site paramType = param.getType();
			allParamSites.add(paramType);
		}
		MethodVisitor initv = cw.visitMethod(ACC_PUBLIC,
				"<init>",
				converter.getMethodDescriptor(null, allParamSites),
				converter.getMethodSignature(null, allParamSites),
				null
		);

		initv.visitAnnotation(converter.getTypeDescriptor(Parameters.class.getName()), true).visitEnd();

		int i = 0;
		for (Declaration<Site> param : allParams){
			initv.visitParameterAnnotation(i, nameAnnotationDescriptor, true).visit("value", param.getName());
			Site paramType = param.getType();
			if (paramType.isNullable()){
				initv.visitParameterAnnotation(i, nullableAnnotationDescriptor, true);
			}
			if (paramType.isThreadSafe()){
				initv.visitParameterAnnotation(i, threadSafeAnnotationDescriptor, true);
			}
			if (paramType instanceof SelfSite){
				initv.visitParameterAnnotation(i, selfTypedAnnotationDescriptor, true);
			}
			i++;
		}
		List<Site> superParamTypes = new ArrayList<>();
		for (Declaration<Site> param : superParameters){
			superParamTypes.add(param.getType());
		}

		initv.visitCode();
		initv.visitVarInsn(ALOAD, 0);
		initv.visitInsn(DUP);
		int j = 1;
		for (; j <= superParameters.size() ;){
			initv.visitVarInsn(ALOAD, j++);
		}
		initv.visitMethodInsn(INVOKESPECIAL, superClassInternalName, "<init>", converter.getMethodDescriptor(null, superParamTypes));

		for (PropertyNode propertyNode : input.getProperties()){
			initv.visitVarInsn(ALOAD, 0);
			initv.visitVarInsn(ALOAD, j++);
			initv.visitFieldInsn(PUTFIELD, hostClassInternalName, propertyNode.getName(), converter.getTypeDescriptor(propertyNode.getType().getSite()));
		}

		initv.visitInsn(RETURN);
		initv.visitMaxs(1, 1);
		initv.visitEnd();

	}

	private List<Declaration<Site>> getAllParameters(Type type){
		List<Declaration<Site>> ret = new ArrayList<>();
		for (Type superType : type.getSuperTypes()){
			ret.addAll(getAllParameters(superType));
		}
		ret.addAll(type.getParameters());
		return ret;
	}

	private void buildProperty(PropertyNode property, ClassWriter cw) {

		String propertyName = property.getName();
		Site site = property.getType().getSite();

		FieldVisitor visitor = cw.visitField(ACC_PUBLIC,
				propertyName,
				converter.getTypeDescriptor(property.getType().getSite()),
				converter.getSignature(site),
				null
		);

		if (site.isNullable()){
			visitor.visitAnnotation(nullableAnnotationDescriptor, true);
		}
		if (site.isThreadSafe()){
			visitor.visitAnnotation(threadSafeAnnotationDescriptor, true);
		}
		if (site instanceof SelfSite){
			visitor.visitAnnotation(selfTypedAnnotationDescriptor, true);
		}

		visitor.visitEnd();
	}



}
