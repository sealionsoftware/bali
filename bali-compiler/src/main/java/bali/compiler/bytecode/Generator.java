package bali.compiler.bytecode;


import org.objectweb.asm.Opcodes;

/**
 * User: Richard
 * Date: 30/04/13
 */
public interface Generator<In, Out> extends Opcodes {

	public static final String PACKAGE_CLASS_NAME = "_";

	public Out build(In input) throws Exception;

}
