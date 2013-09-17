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

	private Site returnType;
	private List<Declaration> parameterDeclarations = new ArrayList<>();

	public MethodSignatureVisitor() {
		super(Opcodes.ASM4);
	}

	public SignatureVisitor visitReturnType() {
		//TODO
		return super.visitReturnType();
	}

	public SignatureVisitor visitParameterType() {
		//TODO
		return super.visitParameterType();
	}

	public Site getReturnType() {
		return returnType;
	}

	public List<Declaration> getParameterDeclarations() {
		return parameterDeclarations;
	}
}
