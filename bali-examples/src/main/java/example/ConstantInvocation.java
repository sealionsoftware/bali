package example;

import bali.CharArrayString;

public class ConstantInvocation {

	public void sayHello() {
		bali._.CONSOLE.writeLine(new CharArrayString(new char[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'}));
	}

}
