package example;

import bali.Boolean;
import com.sealionsoftware.bali.CharArrayString;

import java.lang.Exception;

/**
 * User: Richard
 * Date: 26/06/13
 */
public class TryObject {

	public void goDo() {

		Boolean isNull = null;

		try {
			isNull.not();
		} catch (Exception e){
			bali._.CONSOLE.writeLine(new CharArrayString(e.getMessage().toCharArray()));
		}

	}
}
