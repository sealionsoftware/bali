package example;

import bali.Boolean;
import com.sealionsoftware.bali.CharArrayString;
import com.sealionsoftware.bali.IdentityBoolean;
import bali.String;
import com.sealionsoftware.bali.number.Short;
import com.sealionsoftware.bali.number.Byte;
import bali.Number;
import com.sealionsoftware.bali.collections.Array;
import com.sealionsoftware.bali.Console;

public class _ {

	public static final Number NUMBER_CONSTANT = new Short((short) 567);

	public static final Boolean BOOLEAN_CONSTANT = IdentityBoolean.TRUE;

	public static final String STRING_CONSTANT = new CharArrayString(new char[]{'T', 'E', 'S', 'T', 'I', 'N', 'G'});

	public static final Array LIST_LITERAL = new Array(new Number[]{new Byte((byte) 1), new Byte((byte) 2), new Byte((byte) 3)});

	public static final Console CONSOLE = new Console();

}
