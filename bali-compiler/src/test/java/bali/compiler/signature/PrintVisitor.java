package bali.compiler.signature;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

/**
 * User: Richard
 * Date: 06/03/14
 */
public class PrintVisitor extends SignatureVisitor {

	private static int ids = 0;
	private int id;

	public PrintVisitor(int id) {
		super(Opcodes.ASM4);
		this.id = id;
	}

	private void log(String method){
		log(method, null);
	}

	private void log(String method, int arg){
		log(method, Integer.toString(arg));
	}

	private void log(String method, String arg){
		StringBuilder sb = new StringBuilder()
				.append(id)
				.append(" ")
				.append(method);
		if (arg != null){
			sb.append(" ").append(arg);
		}
		System.out.println(sb.toString());
	}

	public void visitFormalTypeParameter(String name) {
		log("visitFormalTypeParameter", name);
	}

	private SignatureVisitor spawn(String method){
		int ret = ++ids;
		log(method, ret);
		return new PrintVisitor(ret);
	}

	public SignatureVisitor visitClassBound() {
		return spawn("visitClassBound");
	}

	public SignatureVisitor visitInterfaceBound() {
		return spawn("visitInterfaceBound");
	}

	public SignatureVisitor visitSuperclass() {
		return spawn("visitSuperclass");
	}

	public SignatureVisitor visitInterface() {
		return spawn("visitInterface");
	}

	public SignatureVisitor visitParameterType() {
		return spawn("visitParameterType");
	}

	public SignatureVisitor visitReturnType() {
		return spawn("visitReturnType");
	}

	public SignatureVisitor visitExceptionType() {
		return spawn("visitExceptionType");
	}

	public void visitBaseType(char descriptor) {
		log("visitBaseType", Character.toString(descriptor));
	}

	public void visitTypeVariable(String name) {
		log("visitTypeVariable", name);
	}

	public SignatureVisitor visitArrayType() {
		return spawn("visitArrayType");
	}

	public void visitClassType(String name) {
		log("visitClassType", name);
	}

	public void visitInnerClassType(String name) {
		log("visitInnerClassType", name);
	}

	public void visitTypeArgument() {
		log("visitTypeArgument");
	}

	public SignatureVisitor visitTypeArgument(char wildcard) {
		return spawn("visitTypeArgument " + Character.toString(wildcard));
	}

	public void visitEnd() {
		log("visitEnd");
	}
}
