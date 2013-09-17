package bali.compiler.type;

import bali.CharArrayString;
import bali.String;
import bali.annotation.Name;

/**
 * User: Richard
 * Date: 23/08/13
 */
public class VanillaObject implements VanillaInterface {

	public void aVoidMethod() {
	}

	public void aVoidMethodWithArgument(@Name("argument") String argument) {
	}

	public String aStringMethod() {
		return new CharArrayString("".toCharArray());
	}

	private void aPrivateMethod() {
	}

}
