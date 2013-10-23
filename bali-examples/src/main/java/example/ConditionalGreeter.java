package example;

import bali.CharArrayString;
import bali.Executable;

/**
 * User: Richard
 * Date: 23/10/13
 */
public class ConditionalGreeter implements Executable {

	public void execute() throws Throwable {
		if(true){
			_.CONSOLE.writeLine(new CharArrayString("Hello World".toCharArray()));
		}
	}
}
