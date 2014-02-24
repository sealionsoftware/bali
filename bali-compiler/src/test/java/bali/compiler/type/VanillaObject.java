package bali.compiler.type;

import bali.CharArrayString;
import bali.String;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;

/**
 * User: Richard
 * Date: 23/08/13
 */
@MetaType(Kind.OBJECT)
public class VanillaObject implements VanillaInterface {

	public void aVoidMethod() {
	}

	public void aVoidMethodWithParameter(@Name("parameter") String parameter) {
	}

	public String aStringMethod() {
		return new CharArrayString("".toCharArray());
	}

	private void aPrivateMethod() {
	}

}
