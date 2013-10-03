package bali.compiler.bytecode.types;

import bali.*;
import bali.String;

/**
 * User: Richard
 * Date: 01/10/13
 */
public class C implements B {

	public String aMethod(String argument) {
		return new CharArrayString("".toCharArray());
	}
}
