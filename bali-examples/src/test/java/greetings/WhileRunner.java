package greetings;

import bali.Executable;
import loop.WhileDoer;

/**
 * User: Richard
 * Date: 10/05/13
 */
public class WhileRunner {

	public static void main(String[] args) {
		Executable doer = new WhileDoer();
		doer.execute();
	}

}
