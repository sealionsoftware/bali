package greetings;

import bali.CharArrayString;
import bali.Executable;
import construct.ObjectConstructor;
import nullcheck.NullChecker;

/**
 * User: Richard
 * Date: 10/05/13
 */
public class NullCheckRunner {

	public static void main(String[] args) throws Throwable {
		Executable doer = new NullChecker(new CharArrayString("GDay".toCharArray()));
		doer.execute();
		Executable doer2 = new NullChecker(null);
		doer2.execute();
	}

}
