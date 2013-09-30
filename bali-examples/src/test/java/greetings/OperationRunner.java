package greetings;

import bali.Executable;
import operation.OperationDoer;

/**
 * User: Richard
 * Date: 10/05/13
 */
public class OperationRunner {

	public static void main(String[] args) throws Throwable {
		Executable doer = new OperationDoer();
		doer.execute();
	}

}
