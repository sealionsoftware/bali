package example;

import bali.Boolean;
import bali.CharArrayString;
import bali.Console;
import bali.IdentityBoolean;
import bali.Number;
import bali.String;
import bali.collection.Array;
import bali.number.Byte;
import bali.number.Short;

public class _ {

	public static final Number NUMBER_CONSTANT = new Short((short) 567);

	public static final Boolean BOOLEAN_CONSTANT = IdentityBoolean.TRUE;

	public static final String STRING_CONSTANT = new CharArrayString(new char[]{'T', 'E', 'S', 'T', 'I', 'N', 'G'});

	public static final Array LIST_LITERAL = new Array(new Number[]{new Byte((byte) 1), new Byte((byte) 2), new Byte((byte) 3)});

	public static final Console CONSOLE = new Console();

}
