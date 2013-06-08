package greetings;

/**
 * User: Richard
 * Date: 10/05/13
 */
public class GreeterRunner {

	public static void main(String[] args) {
		Greeter greeter = new ConsoleGreeter();
		greeter.greet();
	}

}
