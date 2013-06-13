package example;

import bali.CharArrayString;

public class FieldObject {

	private CharArrayString one = new CharArrayString(new char[]{'A'});
//	private Number two = new Number(2);

	private void print() {
		_.CONSOLE.printLine(one);
		_.CONSOLE.printLine(one);
	}

}
