package bali.compiler.type;

import bali.compiler.reference.Reference;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 03/09/13
 */
public class PackageConstantsBuilderVisitor extends ClassVisitor {

	private TypeLibrary library;

	private List<Declaration> declaredConstants = new ArrayList<>();

	public PackageConstantsBuilderVisitor(TypeLibrary library) {
		super(Opcodes.ASM4);
		this.library = library;
	}

	public FieldVisitor visitField(int access, String name, String desc,
	                               String signature, Object value) {

		if (signature != null){
			SignatureReader signatureReader = new SignatureReader(signature);
			SiteSignatureVisitor fieldVisitor = new SiteSignatureVisitor(library, Collections.<String, Site>emptyMap());
			signatureReader.accept(fieldVisitor);
			declaredConstants.add(new Declaration(name, fieldVisitor.getSite()));
		} else {
			Type asmType = Type.getType(desc);
			Reference<bali.compiler.type.Type> ref = library.getReference(asmType.getClassName().replaceAll("/", "."));
			declaredConstants.add(new Declaration(name, new VanillaSite(ref)));
		}

		return super.visitField(access, name, desc, signature, value);
	}

	public List<Declaration> getDeclaredConstants() {
		return declaredConstants;
	}
}
