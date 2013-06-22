package example;

import bali.String;

public class ConstantInvocation {

	public void sayHello() {
		bali._.CONSOLE.printLine(new String(new char[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'}));
	}

}
