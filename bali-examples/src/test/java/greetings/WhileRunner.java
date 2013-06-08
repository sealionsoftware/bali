package greetings;

import loop.Doer;
import loop.WhileDoer;

/**
 * User: Richard
 * Date: 10/05/13
 */
public class WhileRunner {

	public static void main(String[] args) {
		Doer doer = new WhileDoer();
		doer.goDo();
	}

}
