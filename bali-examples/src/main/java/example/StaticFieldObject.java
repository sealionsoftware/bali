package example;

import com.sealionsoftware.bali.CharArrayString;
import com.sealionsoftware.bali.collections.Array;
import bali.String;

public class StaticFieldObject {

	private static final Array NAMES = new Array(new String[]{new CharArrayString(new char[]{'A'})});

	private void print() {

//		Number length = bali._.NUMBER_FACTORY.forDecimalString();
//		Number i = 0;
//
//		while (i < length){
//			_.CONSOLE.writeLine(NAMES.get(i));
//			i = i.add(1);
//		}
	}

}
