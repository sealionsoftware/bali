package bali.compiler.signature;

import org.objectweb.asm.signature.SignatureReader;

/**
 * User: Richard
 * Date: 06/03/14
 */
public class SigReverseEngineer {

	public static void main(String[] args) {

		String signature = "Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;>;";

		SignatureReader reader = new SignatureReader(signature);
		reader.accept(new PrintVisitor(0));

	}
}
