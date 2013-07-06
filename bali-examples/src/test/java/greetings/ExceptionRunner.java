package greetings;

import exception.Doer;
import exception.TryDoer;

/**
 * User: Richard
 * Date: 10/05/13
 */
public class ExceptionRunner {

	public static void main(String[] args) {
		Doer doer = new TryDoer();
		doer.goDo();
	}

}
