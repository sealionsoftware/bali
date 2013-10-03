package greetings;

import bali.CharArrayString;
import bali.Executable;
import console.ConsoleDoer;

/**
 * User: Richard
 * Date: 03/10/13
 */
public class ConsoleRunner {

	public static void main(String[] args) throws Throwable {
		Executable doer = new ConsoleDoer(new CharArrayString("Hello".toCharArray()));
		doer.execute();
	}

}
