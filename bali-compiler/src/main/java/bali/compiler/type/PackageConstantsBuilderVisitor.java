package bali.compiler.type;

import bali.annotation.Nullable;
import bali.annotation.ThreadSafe;
import bali.compiler.reference.Reference;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 03/09/13
 */
public class PackageConstantsBuilderVisitor extends ClassVisitor {

	private static final String NULLABLE_ANNOTATION_NAME =
			org.objectweb.asm.Type.getType(Nullable.class).getDescriptor();
	private static final String THREADSAFE_ANNOTATION_NAME  =
			org.objectweb.asm.Type.getType(ThreadSafe.class).getDescriptor();

	private ClassLibrary library;

	private List<Declaration<Site>> declaredConstants = new ArrayList<>();

	public PackageConstantsBuilderVisitor(ClassLibrary library) {
		super(Opcodes.ASM4);
		this.library = library;
	}

	public FieldVisitor visitField(int access, final String name, final String desc,
	                               final String signature, final Object value) {

		return new FieldVisitor(Opcodes.ASM4) {

			private boolean isNullable = false;
			private boolean isThreadSafe = false;

			public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
				if (desc.equals(NULLABLE_ANNOTATION_NAME)){
					isNullable = true;
				} else if (desc.equals(THREADSAFE_ANNOTATION_NAME)){
					isThreadSafe = true;
				}
				return super.visitAnnotation(desc, visible);
			}

			public void visitEnd() {
				if (signature != null){
					SignatureReader signatureReader = new SignatureReader(signature);
					SiteSignatureVisitor fieldVisitor = new SiteSignatureVisitor(library, Collections.<String, Type>emptyMap(), isNullable, isThreadSafe);
					signatureReader.accept(fieldVisitor);
					declaredConstants.add(new Declaration<>(name, fieldVisitor.getSite()));
				} else {
					org.objectweb.asm.Type asmType = org.objectweb.asm.Type.getType(desc);
					Reference<Class> ref = library.getReference(asmType.getClassName().replaceAll("/", "."));
					declaredConstants.add(new Declaration<Site>(name, new ParameterisedSite(ref, isNullable, isThreadSafe)));
				}
				super.visitEnd();
			}
		};
	}

	public List<Declaration<Site>> getDeclaredConstants() {
		return declaredConstants;
	}
}
