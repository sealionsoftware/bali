package greetings;

import bali.Executable;

/**
 * User: Richard
 * Date: 10/05/13
 */
public class TestRunner {

	public static void main(String[] args) throws Throwable {
		((Executable) Class.forName(args[0]).newInstance()).execute();
	}

}
