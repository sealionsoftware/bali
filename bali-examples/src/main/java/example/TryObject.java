package example;

import bali.Boolean;
import bali.String;

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
			bali._.CONSOLE.printLine(new String(e.getMessage().toCharArray()));
		}

	}
}
