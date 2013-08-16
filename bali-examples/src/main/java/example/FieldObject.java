package example;

import bali.CharArrayString;

public class FieldObject {

	private CharArrayString one = new CharArrayString(new char[]{'A'});

	private void print() {
		_.CONSOLE.writeLine(one);
		_.CONSOLE.writeLine(one);
	}

}
