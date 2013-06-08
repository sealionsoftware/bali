package greetings;

import conditional.ConditionalDoer;
import conditional.Doer;

/**
 * User: Richard
 * Date: 10/05/13
 */
public class ConditionalRunner {

	public static void main(String[] args) {
		Doer doer = new ConditionalDoer();
		doer.goDo();
	}

}
