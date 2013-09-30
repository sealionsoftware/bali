package greetings;

import bali.Executable;
import exception.TryDoer;

/**
 * User: Richard
 * Date: 10/05/13
 */
public class ExceptionRunner {

	public static void main(String[] args) throws Throwable {
		Executable tryer = new TryDoer();
		tryer.execute();
	}

}
