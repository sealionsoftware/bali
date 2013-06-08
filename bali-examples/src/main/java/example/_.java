package example;

import bali.Boolean;
import bali.Console;
import bali.List;
import bali.Number;
import bali.String;

public class _ {

	public static final Number NUMBER_CONSTANT = new Number(567);

	public static final Boolean BOOLEAN_CONSTANT = Boolean.TRUE;

	public static final String STRING_CONSTANT = new String(new char[]{'T', 'E', 'S', 'T', 'I', 'N', 'G'});

	public static final List LIST_LITERAL = new List(new Object[]{new Number(1), new Number(2), new Number(3)});

	public static final Console CONSOLE = new Console();

}
