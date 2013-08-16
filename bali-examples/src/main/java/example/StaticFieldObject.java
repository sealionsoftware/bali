package example;

import bali.CharArrayString;
import bali.IdentityBoolean;
import bali.Number;
import bali.String;
import bali.collection.Array;

import static bali.number.NumberFactory.NUMBER_FACTORY;

public class StaticFieldObject {

	private static final Array<String> NAMES = new Array(new String[]{new CharArrayString(new char[]{'A'})});

	private void print() {

		Number length = NAMES.size();
		Number i = NUMBER_FACTORY.forInt(0);

		while (i.lessThan(length) == IdentityBoolean.TRUE) {
			_.CONSOLE.writeLine(NAMES.get(i));
			i = i.add(NUMBER_FACTORY.forInt(1));
		}
	}

}
