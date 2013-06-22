package example;

import bali.Boolean;
import bali.String;
import bali.Short;
import bali.Byte;
import bali.Number;
import bali.Array;
import bali.Console;

public class _ {

	public static final Number NUMBER_CONSTANT = new Short((short) 567);

	public static final Boolean BOOLEAN_CONSTANT = Boolean.TRUE;

	public static final String STRING_CONSTANT = new String(new char[]{'T', 'E', 'S', 'T', 'I', 'N', 'G'});

	public static final Array LIST_LITERAL = new Array(new Number[]{new Byte((byte) 1), new Byte((byte) 2), new Byte((byte) 3)});

	public static final Console CONSOLE = new Console();

}
