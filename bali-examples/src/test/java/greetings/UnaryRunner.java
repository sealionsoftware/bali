package greetings;

import bali.Executable;
import unary.UnaryDoer;

/**
 * User: Richard
 * Date: 10/05/13
 */
public class UnaryRunner {

	public static void main(String[] args) throws Throwable {
		Executable doer = new UnaryDoer();
		doer.execute();
	}

}
