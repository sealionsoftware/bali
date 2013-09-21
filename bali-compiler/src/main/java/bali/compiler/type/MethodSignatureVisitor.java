package bali.compiler.type;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 04/09/13
 */
public class MethodSignatureVisitor extends SignatureVisitor {

	private TypeLibrary library;

	private Site returnType;
	private List<Declaration> parameterDeclarations = new ArrayList<>();

	public MethodSignatureVisitor(TypeLibrary library) {
		super(Opcodes.ASM4);
		this.library = library;
	}

	public SignatureVisitor visitReturnType() {
		return new SiteSignatureVisitor(library){
			public void visitEnd() {
				super.visitEnd();
				returnType = this.getSite();
			}
		};
	}

	public SignatureVisitor visitParameterType() {
		return new SiteSignatureVisitor(library){
			public void visitEnd() {
				super.visitEnd();
				parameterDeclarations.add(new Declaration(null, this.getSite()));
			}
		};
	}

	public Site getReturnType() {
		return returnType;
	}

	public List<Declaration> getParameterDeclarations() {
		return parameterDeclarations;
	}
}
