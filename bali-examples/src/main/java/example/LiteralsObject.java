package example;

import bali.Executable;

/**
 * User: Richard
 * Date: 09/07/13
 */
public class LiteralsObject implements Executable {

	public void execute() {

		Object o = "Hello World";
		final char[] lit = {'G', 'o', 'o', 'd'};
		o = 1345976757;
		o = true;

		System.out.println(o);

	}
}
