package greetings;

import switcher.Doer;
import switcher.SwitchDoer;

/**
 * User: Richard
 * Date: 10/05/13
 */
public class SwitchRunner {

	public static void main(String[] args) {
		Doer doer = new SwitchDoer();
		doer.goDo();
	}

}
